package com.shagil.secuton.presentation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shagil.secuton.domain.repository.RoutineTaskRepository
import com.shagil.secuton.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val routineTaskRepository: RoutineTaskRepository
):ViewModel() {

    fun initRoutineTask(uid:String) {
        viewModelScope.launch(Dispatchers.IO) {
            routineTaskRepository.initRoutineTask(uid)
        }
    }
}