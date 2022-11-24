package com.shagil.secuton.data.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.shagil.secuton.domain.model.LeisureTask
import com.shagil.secuton.domain.model.RoutineTask
import com.shagil.secuton.domain.model.SlotItem
import com.shagil.secuton.domain.model.TODOTask
import com.shagil.secuton.domain.repository.LeisureTasksRepository
import com.shagil.secuton.util.Resource
import javax.inject.Inject

class LeisureTaskRepoImpl @Inject constructor(
    private val database: FirebaseDatabase
):LeisureTasksRepository {
    private val databaseReference = database.getReference("leisure_tasks")

    companion object {
        private const val TAG = "LeisureTaskRepoImpl"
    }

    override suspend fun createNewLeisureTask(uid: String, leisureTask: LeisureTask) {
        leisureTask.lid?.let { lid->
            databaseReference.child(uid).child(lid).setValue(leisureTask)
                .addOnSuccessListener {
                    Log.d(TAG, "createNewLeisureTask(): Success")
                }
                .addOnFailureListener {
                    Log.e(TAG, "createNewLeisureTask(): Failure: ${it.message}")
                }
        }
    }

    override suspend fun fetchAllLeisureTasks(
        uid: String,
        leisureTasksLiveData: MutableLiveData<Resource<List<LeisureTask>>>
    ) {
        databaseReference.child(uid)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val result = snapshot.children.map { dataSnapshot ->
                        dataSnapshot.getValue(LeisureTask::class.java)
                    }
                    val leisureTaskResult = mutableListOf<LeisureTask>()
                    result.forEach { leisureTask ->
                        if(leisureTask!=null) {
                            leisureTaskResult.add(leisureTask)
                        }
                    }

                    Log.d(TAG, "fetchAllLeisureTasks(): $leisureTaskResult")

                    leisureTasksLiveData.postValue(Resource.Success(
                        data = leisureTaskResult
                    ))
                }

                override fun onCancelled(error: DatabaseError) {
                    leisureTasksLiveData.postValue(Resource.Error(message = error.message))
                }
            })
    }

    override suspend fun updateLeisureTask(uid: String, leisureTask: LeisureTask) {
        leisureTask.lid?.let { lid->
            databaseReference.child(uid).child(lid).setValue(leisureTask)
                .addOnSuccessListener {
                    Log.d(TAG, "updateLeisureTask(): Success")
                }
                .addOnFailureListener {
                    Log.e(TAG, "updateLeisureTask(): Failure: ${it.message}")
                }
        }
    }
}