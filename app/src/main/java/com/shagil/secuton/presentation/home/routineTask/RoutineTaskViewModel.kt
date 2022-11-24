package com.shagil.secuton.presentation.home.routineTask

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shagil.secuton.domain.model.RoutineImportantTask
import com.shagil.secuton.domain.model.RoutineTask
import com.shagil.secuton.domain.model.SlotItem
import com.shagil.secuton.domain.repository.RoutineTaskRepository
import com.shagil.secuton.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RoutineTaskViewModel @Inject constructor(
    private val routineTaskRepository:RoutineTaskRepository
):ViewModel() {
    private val _routineImportantTask = MutableLiveData<Resource<RoutineImportantTask>>()
    val routineImportantTask:LiveData<Resource<RoutineImportantTask>>
    get() = _routineImportantTask

    private val _routineDayTasks = MutableLiveData<Resource<List<RoutineTask>>>()
    val routineDayTasks:LiveData<Resource<List<RoutineTask>>>
    get() = _routineDayTasks

    fun fetchRoutineImportantTask(uid:String) {
        viewModelScope.launch(Dispatchers.IO) {

            routineTaskRepository.fetchRoutineImportantTask(uid, _routineImportantTask)
        }
    }

    fun updateRoutineImpTask(uid:String, task:String) {
        viewModelScope.launch(Dispatchers.IO) {
            routineTaskRepository.updateRoutineImportantTask(uid, task)
        }
    }

    fun fetchAllRoutineTasks(uid:String) {
        viewModelScope.launch(Dispatchers.IO) {
            routineTaskRepository.fetchAllRoutineTasks(uid, _routineDayTasks)
        }
    }

    fun updateRoutineTask(uid:String, dayIndex:Int, slotIndex:Int, slotItem:SlotItem) {
        viewModelScope.launch(Dispatchers.IO) {
            routineTaskRepository.updateRoutineTask(uid, dayIndex, slotIndex, slotItem)
        }
    }
}