package com.shagil.secuton.presentation.home.todoTask

import android.app.*
import android.app.DatePickerDialog.OnDateSetListener
import android.content.Context.ALARM_SERVICE
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.shagil.secuton.databinding.FragmentTodosBinding
import com.shagil.secuton.domain.model.TODOTask
import com.shagil.secuton.presentation.epoxy.controllers.TodosTaskItemController
import com.shagil.secuton.util.Resource
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class TodosFragment : Fragment() {
    companion object {
        private const val TAG = "TodosFragment"
    }

    private var _binding: FragmentTodosBinding?=null
    private val binding get() = _binding!!

    private lateinit var calendar:Calendar
    private lateinit var firebaseAuth: FirebaseAuth

    private val todoTasksViewModel by viewModels<TodoTaskViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        calendar = Calendar.getInstance()
        firebaseAuth = Firebase.auth
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTodosBinding.inflate(inflater, container, false)

        newTODOItem()

        todoTasksViewModel.fetchAllTODOTasks(firebaseAuth.uid.toString())

        val controller = TodosTaskItemController(
            requireContext(),
            { updatedTodotask->
            todoTasksViewModel.updateTODOTask(firebaseAuth.uid.toString(), updatedTodotask)
            },
            { tidToBeDeleted->
                todoTasksViewModel.deleteTODOTask(firebaseAuth.uid.toString(), tidToBeDeleted)
            }
        )

        todoTasksViewModel.todoTasks.observe(viewLifecycleOwner, Observer {
            when(it) {
                is Resource.Success -> {
                    if(it.data!=null) {
                        controller.todosItemList = it.data.toMutableList()
                    }
                }
                else -> {}
            }
        })

        binding.todoTaskEpoxyRecyclerView.setControllerAndBuildModels(controller)

        return binding.root
    }

    private fun newTODOItem() {
        binding.createNewTodo.setOnClickListener {
            binding.createNewTodo.visibility = View.GONE
            binding.todoTitleTextview.visibility = View.GONE
            binding.todoDescriptionTextview.visibility = View.GONE

            binding.postNewTodoContainer.visibility = View.VISIBLE
            binding.todoTitleEdittext.visibility = View.VISIBLE
            binding.todoDescriptionEdittext.visibility = View.VISIBLE
        }

        binding.setTimeTextview.setOnClickListener{
            showTimePicker()
        }

        binding.setDateTextview.setOnClickListener{
            showDatePicker()
        }

        binding.postNewTodoBtn.setOnClickListener {
            val title = binding.todoTitleEdittext.text.toString()
            val description = binding.todoDescriptionEdittext.text.toString()
            val date = binding.setDateTextview.text.toString().trim()
            val time = binding.setTimeTextview.text.toString().trim()

            if(title.isEmpty() || description.isEmpty()) {
                Toast.makeText(context, "Title/Description cannot be empty!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if(date == "Set Date" || time == "Set Time") {
                Toast.makeText(context, "Select time/date of new TODO!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            binding.setDateTextview.text = "Set Date "
            binding.setTimeTextview.text = "Set Time "
            binding.postNewTodoContainer.visibility = View.GONE

            setAlarm(title, description, date, time)
        }
    }

    private fun showDatePicker() {
        // Get Current Date
        val mDay = calendar.get(Calendar.DAY_OF_MONTH)
        val mMonth = calendar.get(Calendar.MONTH)
        val mYear = calendar.get(Calendar.YEAR)
        val datePickerDialog =
            DatePickerDialog(
                requireContext(),
                object : OnDateSetListener {
                    override fun onDateSet(p0: DatePicker?, year: Int, month: Int, day: Int) {
                        calendar.set(Calendar.YEAR, year)
                        calendar.set(Calendar.MONTH, month)
                        calendar.set(Calendar.DAY_OF_MONTH, day)

                        val myFormat = "MM/dd/yyyy" // mention the format you need
                        val sdf = SimpleDateFormat(myFormat, Locale.US)
                        val dateString = sdf.format(calendar.time)
                        binding.setDateTextview.text = dateString
                    }
                },
                mYear, mMonth,mDay)
        datePickerDialog.datePicker.minDate = System.currentTimeMillis() - 1000
        datePickerDialog.show()
    }

    private fun showTimePicker() {
        val mHour = calendar.get(Calendar.HOUR_OF_DAY)
        val mMinute = calendar.get(Calendar.MINUTE)

        val mTimePicker = TimePickerDialog(requireContext(),
            { view, hourOfDay, minute ->
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                calendar.set(Calendar.MINUTE, minute)
                val timeString = String.format("%d : %d", hourOfDay, minute)

                binding.setTimeTextview.text = timeString
            }, mHour, mMinute, false)

        mTimePicker.show()
    }

    private fun setAlarm(title:String, description:String, date:String, time:String) {
        // public static final String ALARM_SERVICE = "alarm";
        val alarmManager = context?.getSystemService(ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)
        intent.putExtra("title", title)
        intent.putExtra("description", description)

        val uid = firebaseAuth.uid.toString()
        val timestamp = calendar.timeInMillis
        val broadcastId = timestamp.toInt()

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            broadcastId,
            intent,
            PendingIntent.FLAG_ONE_SHOT
        )

        alarmManager.set(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            pendingIntent
        )

        todoTasksViewModel.createNewTODOTask(
            uid,
            TODOTask(
                tid = uid+System.currentTimeMillis().toString(),
                title = title,
                description = description,
                deadline = "$date $time",
                priority = 0,
                broadcastId = broadcastId.toString()
            )
        )
    }

    private fun cancelAlarm(broadcastId: Int, title:String, description:String) {
        val alarmManager = context?.getSystemService(ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            broadcastId,
            intent,
            PendingIntent.FLAG_ONE_SHOT
        )

        alarmManager.cancel(pendingIntent)
    }
}