package com.shagil.secuton.data.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.shagil.secuton.domain.model.RoutineImportantTask
import com.shagil.secuton.domain.model.RoutineTask
import com.shagil.secuton.domain.model.SlotItem
import com.shagil.secuton.domain.model.routineTaskData
import com.shagil.secuton.domain.repository.RoutineTaskRepository
import com.shagil.secuton.util.Resource
import javax.inject.Inject


class RoutineTaskRepoImpl @Inject constructor(
    private val database: FirebaseDatabase
):RoutineTaskRepository {
    private val dataBaseReference = database.getReference("routine_tasks");

    companion object {
        private const val TAG = "RoutineTaskRepoImpl"
    }

    override suspend fun initRoutineTask(uid: String) {
        dataBaseReference.child(uid).setValue(routineTaskData).addOnSuccessListener {
            Log.d(TAG, "initRoutineTask(): Success")
        }.addOnFailureListener {
            Log.e(TAG, "initRoutineTask(): Failure: ${it.message}")
        }

        dataBaseReference.child("routineImportantTask").child(uid).setValue(RoutineImportantTask(task = "This is an important task!"))
            .addOnSuccessListener {
                Log.d(TAG, "initRoutineTask(routineImportantTask): Success")
            }.addOnFailureListener {
                Log.e(TAG, "initRoutineTask(routineImportantTask): Failure")
            }
    }

    override suspend fun fetchAllRoutineTasks(uid:String, routineDayTasks:MutableLiveData<Resource<List<RoutineTask>>>) {
        dataBaseReference.child(uid)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val result = snapshot.children.map { dataSnapshot ->
                        dataSnapshot.getValue(RoutineTask::class.java)
                    }
                    val routineDayTaskResult = mutableListOf<RoutineTask>()
                    result.forEach {
                        if(it!=null) {
                            val slotList = mutableListOf<SlotItem>()
                            if(it.slots!=null) {
                                slotList.addAll(it.slots)
                            }
                            routineDayTaskResult.add(RoutineTask(
                                dayName = it.dayName,
                                slots = slotList
                            ))
                        }
                    }

                    Log.d(TAG, "fetchAllRoutineTasks(): $routineDayTaskResult")

                    routineDayTasks.postValue(Resource.Success(
                        data = routineDayTaskResult
                    ))
                }

                override fun onCancelled(error: DatabaseError) {
                    routineDayTasks.postValue(Resource.Error(message = error.message))
                }

            })
    }

    override suspend fun updateRoutineTask(uid:String, dayIndex: Int, slotIndex: Int, slotItem: SlotItem) {
        dataBaseReference.child(uid)
            .child(dayIndex.toString())
            .child("slots")
            .child(slotIndex.toString())
            .setValue(slotItem)
            .addOnSuccessListener {
                Log.d(TAG, "updateRoutineTask(): Success")
            }
            .addOnFailureListener {
                Log.e(TAG, "updateRoutineTask(): Failure: ${it.message}")
            }
    }

    override suspend fun updateRoutineImportantTask(uid: String, task: String) {
        dataBaseReference.child("routineImportantTask").child(uid).setValue(
            RoutineImportantTask(task = task)
        ).addOnSuccessListener {
            Log.d(TAG, "updateRoutineImportantTask(): Success")
        }.addOnFailureListener {
            Log.e(TAG, "updateRoutineImportantTask(): Failure: ${it.message}")
        }
    }

    override suspend fun fetchRoutineImportantTask(uid:String, routineImportantTask:MutableLiveData<Resource<RoutineImportantTask>>) {
        dataBaseReference.child("routineImportantTask").child(uid)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    /*val routineImpTasksResult = snapshot.children.map { dataSnapshot ->
                        dataSnapshot.getValue(RoutineImportantTask::class.java)
                    }*/
                    val routineImpTasksResult = snapshot.getValue(RoutineImportantTask::class.java)
                    Log.d(TAG, "getAllFeeds(): $routineImpTasksResult")
                    if(routineImpTasksResult!=null) {
                        routineImportantTask.postValue(Resource.Success(
                            data = routineImpTasksResult
                        ))
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    routineImportantTask.postValue(Resource.Error(message = error.message))
                }
            })
    }


}