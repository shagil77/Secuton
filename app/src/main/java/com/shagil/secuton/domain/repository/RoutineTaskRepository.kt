package com.shagil.secuton.domain.repository

import androidx.lifecycle.MutableLiveData
import com.shagil.secuton.domain.model.RoutineImportantTask
import com.shagil.secuton.domain.model.RoutineTask
import com.shagil.secuton.domain.model.SlotItem
import com.shagil.secuton.util.Resource
import kotlinx.coroutines.flow.Flow

interface RoutineTaskRepository {
    suspend fun initRoutineTask(uid:String)

    suspend fun fetchAllRoutineTasks(uid:String, routineDayTasks:MutableLiveData<Resource<List<RoutineTask>>>)

    suspend fun updateRoutineTask(uid:String, dayIndex:Int, slotIndex:Int, slotItem: SlotItem)

    suspend fun updateRoutineImportantTask(uid:String, task:String)

    suspend fun fetchRoutineImportantTask(uid:String, routineImpTasks:MutableLiveData<Resource<RoutineImportantTask>>)
}