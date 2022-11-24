package com.shagil.secuton.domain.repository

import androidx.lifecycle.MutableLiveData
import com.shagil.secuton.domain.model.LeisureTask
import com.shagil.secuton.util.Resource

interface LeisureTasksRepository {
    suspend fun createNewLeisureTask(uid:String, leisureTask: LeisureTask)

    suspend fun fetchAllLeisureTasks(uid:String, leisureTasksLiveData:MutableLiveData<Resource<List<LeisureTask>>>)

    suspend fun updateLeisureTask(uid:String, leisureTask: LeisureTask)
}