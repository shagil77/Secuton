package com.shagil.secuton.presentation.epoxy.controllers

import android.content.Context
import com.airbnb.epoxy.AsyncEpoxyController
import com.shagil.secuton.domain.model.TODOTask
import com.shagil.secuton.presentation.epoxy.models.TodosTaskItemModel

class TodosTaskItemController(
    private val context: Context,
    private val onEditTODOClicked: (TODOTask)-> Unit,
    private val onTODOComppletedClicked: (String)->Unit
):AsyncEpoxyController(){
    var todosItemList : MutableList<TODOTask> = mutableListOf()
    set(value) {
        field = value
        requestModelBuild()
    }
    override fun buildModels() {
        if(todosItemList.isEmpty()) return

        todosItemList.forEach { todoTask->
            TodosTaskItemModel(
                context,
                todoTask,
                onEditTodoClicked = { updatedTodosTask ->
                    onEditTODOClicked(updatedTodosTask)
                },
                onTODOCompletedClicked = { tid->
                    onTODOComppletedClicked(tid)
                }
            ).id(todoTask.tid).addTo(this)
        }
    }
}