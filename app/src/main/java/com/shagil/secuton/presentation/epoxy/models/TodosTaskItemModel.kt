package com.shagil.secuton.presentation.epoxy.models

import android.content.Context
import android.util.Log
import android.view.View
import android.widget.Toast
import com.shagil.secuton.R
import com.shagil.secuton.databinding.TodosItemLayoutBinding
import com.shagil.secuton.domain.model.SlotItem
import com.shagil.secuton.domain.model.TODOTask
import com.shagil.secuton.util.ViewBindingKotlinModel

data class TodosTaskItemModel(
    private val context:Context,
    private val todosTask:TODOTask,
    private val onEditTodoClicked: (TODOTask)->Unit,
    private val onTODOCompletedClicked: (String)->Unit
):ViewBindingKotlinModel<TodosItemLayoutBinding>(R.layout.todos_item_layout) {
    override fun TodosItemLayoutBinding.bind() {
        todoTitleTextview.text = todosTask.title
        todoDescriptionTextview.text = todosTask.description
        deadlineTextview.text = todosTask.deadline

        editTodoBtn.setOnClickListener {
            postNewTodoBtn.visibility = View.VISIBLE
            editTodoTitleEdittext.visibility = View.VISIBLE
            editTodoTitleEdittext.setText(todoTitleTextview.text)
            todoTitleTextview.visibility = View.GONE

            todoDescriptionTextview.visibility = View.VISIBLE
            editTodoDescriptionEdittext.setText(todoDescriptionTextview.text)
            todoDescriptionTextview.visibility = View.GONE
        }

        postNewTodoBtn.setOnClickListener {
            val title = editTodoTitleEdittext.text.toString().trim()
            val description = editTodoDescriptionEdittext.text.toString().trim()
            if(title.isEmpty() || description.isEmpty()) {
                Toast.makeText(context, "Title/Description cannot be empty!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            Log.d("TodosTaskItemModel", "$title $description")

            todoTitleTextview.visibility = View.VISIBLE
            todoDescriptionTextview.visibility = View.VISIBLE

            editTodoTitleEdittext.visibility = View.GONE
            editTodoDescriptionEdittext.visibility = View.GONE

            editTodoBtn.visibility = View.VISIBLE
            postNewTodoBtn.visibility = View.GONE

            onEditTodoClicked(
                TODOTask(
                    tid = todosTask.tid,
                    title = title,
                    description = description,
                    deadline = todosTask.deadline,
                    priority = todosTask.priority,
                    broadcastId = todosTask.broadcastId
                )
            )
        }

        finishTodoBtn.setOnClickListener {
            todosTask.tid?.let { it1 -> onTODOCompletedClicked(it1) }
        }
    }

}