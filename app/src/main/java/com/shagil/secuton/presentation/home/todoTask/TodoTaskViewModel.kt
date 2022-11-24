package com.shagil.secuton.presentation.home.todoTask

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shagil.secuton.domain.model.TODOTask
import com.shagil.secuton.domain.repository.TODOTaskRepository
import com.shagil.secuton.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TodoTaskViewModel @Inject constructor(
    private val todoTaskRepository: TODOTaskRepository
) :ViewModel() {
    private val _todoTasks = MutableLiveData<Resource<List<TODOTask>>>()
    val todoTasks:LiveData<Resource<List<TODOTask>>>
    get() = _todoTasks

    fun fetchAllTODOTasks(uid:String) {
        viewModelScope.launch(Dispatchers.IO) {
            todoTaskRepository.fetchAllTODOTasks(uid, _todoTasks)
        }
    }

    fun createNewTODOTask(uid:String, todoTask: TODOTask) {
        viewModelScope.launch {
            todoTaskRepository.createNewTODOTask(uid, todoTask)
        }
    }

    fun updateTODOTask(uid:String, todoTask: TODOTask) {
        viewModelScope.launch {
            todoTaskRepository.updateNewTODOTask(uid, todoTask)
        }
    }

    fun deleteTODOTask(uid:String, tid:String) {
        viewModelScope.launch(Dispatchers.IO) {
            todoTaskRepository.deleteTODOTask(uid, tid)
        }
    }
}