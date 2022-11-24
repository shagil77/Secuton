package com.shagil.secuton.presentation.home.leisureTask

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shagil.secuton.domain.model.LeisureTask
import com.shagil.secuton.domain.repository.LeisureTasksRepository
import com.shagil.secuton.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LeisureTasksViewModel @Inject constructor(
    private val leisureTasksRepository: LeisureTasksRepository
):ViewModel() {
    private val _leisureTasks = MutableLiveData<Resource<List<LeisureTask>>>()
    val leisureTasks:LiveData<Resource<List<LeisureTask>>>
    get() = _leisureTasks

    fun fetchAllLeisureTasks(uid:String) {
        viewModelScope.launch(Dispatchers.IO) {
            leisureTasksRepository.fetchAllLeisureTasks(uid, _leisureTasks)
        }
    }

    fun updateLeisureTask(uid:String, leisureTask: LeisureTask) {
        viewModelScope.launch(Dispatchers.IO) {
            leisureTasksRepository.updateLeisureTask(uid, leisureTask)
        }
    }

    fun createLeisureTask(uid:String, leisureTask: LeisureTask) {
        viewModelScope.launch(Dispatchers.IO) {
            leisureTasksRepository.createNewLeisureTask(uid, leisureTask)
        }
    }
}