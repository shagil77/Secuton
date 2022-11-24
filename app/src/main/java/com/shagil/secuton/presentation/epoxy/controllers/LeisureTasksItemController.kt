package com.shagil.secuton.presentation.epoxy.controllers

import android.content.Context
import com.airbnb.epoxy.AsyncEpoxyController
import com.shagil.secuton.domain.model.LeisureTask
import com.shagil.secuton.presentation.epoxy.models.LeisureTasksItemModel

class LeisureTasksItemController(
    private val context: Context,
    private val onEditLeisureTaskClicked:(LeisureTask)->Unit
):AsyncEpoxyController() {
    var leisureTasksList : MutableList<LeisureTask> = mutableListOf()
    set(value) {
        field = value
        requestModelBuild()
    }

    override fun buildModels() {
        if(leisureTasksList.isEmpty()) return

        leisureTasksList.forEach { leisureTask ->
            LeisureTasksItemModel(
                context,
                leisureTask,
                onEditLeisureTaskClicked = { updatedLeisureTask->
                    onEditLeisureTaskClicked(updatedLeisureTask)
                }
            ).id(leisureTask.lid).addTo(this)
        }
    }
}