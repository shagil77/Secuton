package com.shagil.secuton.presentation.epoxy.controllers

import android.content.Context
import com.airbnb.epoxy.AsyncEpoxyController
import com.airbnb.epoxy.CarouselModel_
import com.shagil.secuton.domain.model.RoutineTask
import com.shagil.secuton.domain.model.SlotItem
import com.shagil.secuton.presentation.epoxy.models.RoutineDayItemModel

class RoutineTaskController(
    private val context:Context,
    private val onUpdateReminderClicked: (Int, Int, SlotItem) -> Unit // (day, slot, data)
):AsyncEpoxyController() {
    var routineTaskList : MutableList<RoutineTask> = mutableListOf()
    set(value) {
        field = value
        requestModelBuild()
    }
    override fun buildModels() {
        if (routineTaskList.isEmpty()) return

        val controllers:MutableList<RoutineDayItemModel> = mutableListOf()
        routineTaskList.forEach { routineTask->
            val routineDaySlotsController = RoutineDaySlotsController(context) { dayIndex, slotIndex, updatedSlotItem ->
                onUpdateReminderClicked(dayIndex, slotIndex, updatedSlotItem)
            }
            routineDaySlotsController.slotItemList = routineTask.slots?.toMutableList() ?: mutableListOf()
            routineDaySlotsController.dayIndex = getDayNameIndex(routineTask.dayName!!)
            controllers.add(
                RoutineDayItemModel(
                    routineDaySlotsController,
                    routineTask.dayName
                )
            )
        }

        val items = controllers.mapIndexed { index, routineDayItemModel ->
            routineDayItemModel.id("routineDay$index")
        }

        CarouselModel_()
            .id("routineTasksCarousel")
            .models(items)
            .numViewsToShowOnScreen(1.05f)
            .addTo(this)
    }

    private fun getDayNameIndex(dayName:String):Int {
        return when(dayName) {
            "Sunday" -> 0
            "Monday" -> 1
            "Tuesday" -> 2
            "Wednesday" -> 3
            "Thursday" -> 4
            "Friday" -> 5
            "Saturday" -> 6
            else -> -1
        }
    }
}