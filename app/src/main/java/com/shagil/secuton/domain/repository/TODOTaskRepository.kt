package com.shagil.secuton.domain.repository

import androidx.lifecycle.MutableLiveData
import com.shagil.secuton.domain.model.TODOTask
import com.shagil.secuton.util.Resource

interface TODOTaskRepository {
    suspend fun fetchAllTODOTasks(uid:String, todoTasksLiveData:MutableLiveData<Resource<List<TODOTask>>>)

    suspend fun createNewTODOTask(uid:String, todoTask: TODOTask)

    suspend fun updateNewTODOTask(uid:String, todoTask: TODOTask)

    suspend fun deleteTODOTask(uid:String, tid:String)
}