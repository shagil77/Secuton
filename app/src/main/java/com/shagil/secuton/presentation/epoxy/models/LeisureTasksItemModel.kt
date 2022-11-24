package com.shagil.secuton.presentation.epoxy.models

import android.content.Context
import android.util.Log
import android.view.View
import android.widget.Toast
import com.shagil.secuton.R
import com.shagil.secuton.databinding.ModelLeisureTaskItemBinding
import com.shagil.secuton.domain.model.LeisureTask
import com.shagil.secuton.domain.model.TODOTask
import com.shagil.secuton.util.ViewBindingKotlinModel

data class LeisureTasksItemModel(
    private val context: Context,
    private val leisureTask: LeisureTask,
    private val onEditLeisureTaskClicked: (LeisureTask)->Unit
):ViewBindingKotlinModel<ModelLeisureTaskItemBinding>(R.layout.model_leisure_task_item) {
    override fun ModelLeisureTaskItemBinding.bind() {
        leisureTitleTextview.text = leisureTask.title
        leisureDescriptionTextview.text = leisureTask.description

        editLeisureBtn.setOnClickListener {
            postNewLeisureBtn.visibility = View.VISIBLE
            editLeisureTitleEdittext.visibility = View.VISIBLE
            editLeisureTitleEdittext.setText(leisureTitleTextview.text)
            leisureTitleTextview.visibility = View.GONE

            editLeisureDescriptionEdittext.visibility = View.VISIBLE
            editLeisureDescriptionEdittext.setText(leisureDescriptionTextview.text)
            leisureDescriptionTextview.visibility = View.GONE
        }

        postNewLeisureBtn.setOnClickListener {
            val title = editLeisureTitleEdittext.text.toString().trim()
            val description = editLeisureDescriptionEdittext.text.toString().trim()
            if(title.isEmpty() || description.isEmpty()) {
                Toast.makeText(context, "Title/Description cannot be empty!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            Log.d("TodosTaskItemModel", "$title $description")

            leisureTitleTextview.visibility = View.VISIBLE
            leisureDescriptionTextview.visibility = View.VISIBLE

            editLeisureTitleEdittext.visibility = View.GONE
            editLeisureDescriptionEdittext.visibility = View.GONE

            editLeisureBtn.visibility = View.VISIBLE
            postNewLeisureBtn.visibility = View.GONE

            onEditLeisureTaskClicked(
                LeisureTask(
                    lid = leisureTask.lid,
                    title = title,
                    description = description
                )
            )
        }
    }

}
