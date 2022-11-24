package com.shagil.secuton.presentation.home.leisureTask

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.shagil.secuton.R
import androidx.lifecycle.Observer
import com.shagil.secuton.databinding.FragmentLeisureTasksBinding
import com.shagil.secuton.databinding.FragmentTodosBinding
import com.shagil.secuton.domain.model.LeisureTask
import com.shagil.secuton.presentation.epoxy.controllers.LeisureTasksItemController
import com.shagil.secuton.presentation.epoxy.controllers.TodosTaskItemController
import com.shagil.secuton.util.Resource
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class LeisureTasksFragment : Fragment() {

    private var _binding: FragmentLeisureTasksBinding?=null
    private val binding get() = _binding!!

    private lateinit var firebaseAuth: FirebaseAuth

    private val leisureTaskViewModel by viewModels<LeisureTasksViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firebaseAuth = Firebase.auth
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLeisureTasksBinding.inflate(inflater, container, false)

        leisureTaskViewModel.fetchAllLeisureTasks(firebaseAuth.uid.toString())

        newLeisureItem()

        val controller = LeisureTasksItemController(
            requireContext()
        ) { updatedLeisureTask ->
            leisureTaskViewModel.updateLeisureTask(firebaseAuth.uid.toString(), updatedLeisureTask)
        }

        leisureTaskViewModel.leisureTasks.observe(viewLifecycleOwner, Observer {
            when(it) {
                is Resource.Success -> {
                    if(it.data!=null) {
                        controller.leisureTasksList = it.data.toMutableList()
                    }
                }
                else -> {}
            }
        })

        binding.leisureTaskEpoxyRecyclerView.setControllerAndBuildModels(controller)

        return binding.root
    }

    private fun newLeisureItem() {
        binding.createNewLeisureActivity.setOnClickListener {
            binding.createNewLeisureActivity.visibility = View.GONE
            binding.leisureTitleTextview.visibility = View.GONE
            binding.leisureDescriptionTextview.visibility = View.GONE

            binding.postNewLeisureContainer.visibility = View.VISIBLE
            binding.leisureTitleEdittext.visibility = View.VISIBLE
            binding.leisureDescriptionEdittext.visibility = View.VISIBLE
        }

        binding.postNewLeisureBtn.setOnClickListener {
            val title = binding.leisureTitleEdittext.text.toString()
            val description = binding.leisureDescriptionEdittext.text.toString()

            if(title.isEmpty() || description.isEmpty()) {
                Toast.makeText(context, "Title/Description cannot be empty!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            binding.postNewLeisureContainer.visibility = View.GONE
            binding.leisureTitleEdittext.visibility = View.GONE
            binding.leisureDescriptionEdittext.visibility = View.GONE

            binding.leisureTitleTextview.visibility = View.VISIBLE
            binding.leisureDescriptionTextview.visibility = View.VISIBLE
            binding.createNewLeisureActivity.visibility = View.VISIBLE

            val uid = firebaseAuth.uid.toString()
            val lid = uid+System.currentTimeMillis().toString()

            leisureTaskViewModel.createLeisureTask(
                uid,
                LeisureTask(
                    lid,
                    title,
                    description
                )
            )
        }
    }

    companion object {
        private const val TAG = "LeisureTasksFragment"
    }
}