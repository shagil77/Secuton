package com.shagil.secuton.presentation.home.routineTask

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearSnapHelper
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.shagil.secuton.databinding.FragmentRoutineTaskBinding
import com.shagil.secuton.domain.model.SlotItem
import com.shagil.secuton.presentation.epoxy.controllers.RoutineTaskController
import com.shagil.secuton.util.Resource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RoutineTaskFragment : Fragment() {
    companion object {
        private const val TAG = "RoutineTaskFragment"
    }

    private var _binding:FragmentRoutineTaskBinding?=null
    private val binding get() = _binding!!

    private val routineTaskViewModel by viewModels<RoutineTaskViewModel>()
    private lateinit var firebaseAuth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        firebaseAuth = Firebase.auth
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRoutineTaskBinding.inflate(inflater, container, false)

        routineTaskViewModel.fetchRoutineImportantTask(firebaseAuth.uid.toString())
        routineTaskViewModel.fetchAllRoutineTasks(firebaseAuth.uid.toString())

        routineTaskViewModel.routineImportantTask.observe(viewLifecycleOwner, Observer {
            when(it) {
                is Resource.Success -> {
                    binding.impReminderTextview.text = it.data?.task ?: ""
                }
                else -> {}
            }
        })

        binding.editNewImportantReminder.setOnClickListener {
            binding.impReminderEditText.visibility = View.VISIBLE
            binding.postNewImportantReminder.visibility = View.VISIBLE

            binding.impReminderTextview.visibility = View.GONE
            binding.editNewImportantReminder.visibility = View.GONE
        }

        binding.postNewImportantReminder.setOnClickListener {
            val newImpReminder = binding.impReminderEditText.text.toString()
            if(newImpReminder.isEmpty()) {
                Toast.makeText(context, "Reminder cannot be empty!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            binding.impReminderTextview.visibility = View.VISIBLE
            binding.editNewImportantReminder.visibility = View.VISIBLE

            binding.impReminderEditText.visibility = View.GONE
            binding.postNewImportantReminder.visibility = View.GONE

            routineTaskViewModel.updateRoutineImpTask(firebaseAuth.uid.toString(), newImpReminder)
        }

        val routineTaskController = RoutineTaskController(requireContext()) { dayIndex, slotIdx, updatedSlotItem ->
            Log.d(TAG, "$dayIndex $slotIdx $updatedSlotItem")
            routineTaskViewModel.updateRoutineTask(firebaseAuth.uid.toString(), dayIndex, slotIdx, updatedSlotItem)
        }

        routineTaskViewModel.routineDayTasks.observe(viewLifecycleOwner, Observer {

            when (it) {
                is Resource.Success-> {
                    routineTaskController.routineTaskList = it.data?.toMutableList() ?: mutableListOf()
                }
                else -> {}
            }
        })

        binding.routineTaskHorEpoxyRecyclerView.setControllerAndBuildModels(routineTaskController)

        return binding.root
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun getDayName(dayIndex:Int):String {
        return when(dayIndex) {
            0-> "Sunday"
            1-> "Monday"
            2-> "Tuesday"
            3-> "Wednesday"
            4-> "Thursday"
            5-> "Friday"
            6-> "Saturday"
            else -> ""
        }
    }

}