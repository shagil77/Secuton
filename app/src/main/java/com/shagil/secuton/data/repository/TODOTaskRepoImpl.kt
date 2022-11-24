package com.shagil.secuton.data.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.shagil.secuton.domain.model.RoutineTask
import com.shagil.secuton.domain.model.SlotItem
import com.shagil.secuton.domain.model.TODOTask
import com.shagil.secuton.domain.repository.TODOTaskRepository
import com.shagil.secuton.util.Resource
import javax.inject.Inject

class TODOTaskRepoImpl @Inject constructor(
    private val database:FirebaseDatabase
):TODOTaskRepository {
    private val databaseReference = database.getReference("todo_tasks")

    override suspend fun fetchAllTODOTasks(
        uid: String,
        todoTasksLiveData: MutableLiveData<Resource<List<TODOTask>>>
    ) {
        databaseReference.child(uid)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val result = snapshot.children.map { dataSnapshot ->
                        dataSnapshot.getValue(TODOTask::class.java)
                    }
                    val todoTaskResult = mutableListOf<TODOTask>()
                    result.forEach { todoTask ->
                        if(todoTask!=null) {
                            todoTaskResult.add(todoTask)
                        }
                    }

                    Log.d(TAG, "fetchAllTODOTasks(): $todoTaskResult")

                    todoTasksLiveData.postValue(Resource.Success(
                        data = todoTaskResult
                    ))
                }

                override fun onCancelled(error: DatabaseError) {
                    todoTasksLiveData.postValue(Resource.Error(message = error.message))
                }
            })
    }

    override suspend fun createNewTODOTask(uid: String, todoTask: TODOTask) {
        todoTask.tid?.let { tid->
            databaseReference.child(uid)
                .child(tid)
                .setValue(todoTask)
                .addOnSuccessListener {
                    Log.d(TAG, "createNewTODOTask(): Success")
                }
                .addOnFailureListener {
                    Log.e(TAG, "createNewTODOTask(): Failure: ${it.message}")
                }
        }
    }

    override suspend fun updateNewTODOTask(uid: String, todoTask: TODOTask) {
        todoTask.tid?.let { tid->
            databaseReference.child(uid)
                .child(tid)
                .setValue(todoTask)
                .addOnSuccessListener {
                    Log.d(TAG, "updateNewTODOTask(): Success")
                }
                .addOnFailureListener {
                    Log.e(TAG, "updateNewTODOTask(): Failure: ${it.message}")
                }
        }
    }

    override suspend fun deleteTODOTask(uid: String, tid: String) {
        databaseReference.child(uid).child(tid).removeValue()
            .addOnSuccessListener {
                Log.d(TAG, "deleteTODOTask(): Success")
            }
            .addOnFailureListener {
                Log.e(TAG, "deleteTODOTask(): Failure: ${it.message}")
            }
    }

    companion object {
        private const val TAG = "TODOTaskRepoImpl"
    }
}