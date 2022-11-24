package com.shagil.secuton.presentation.epoxy.controllers

import android.content.Context
import com.airbnb.epoxy.AsyncEpoxyController
import com.shagil.secuton.domain.model.SlotItem
import com.shagil.secuton.presentation.epoxy.models.RoutineDaySlotItemModel

class RoutineDaySlotsController(
    private val context: Context,
    private val onUpdateReminderClicked: (Int, Int, SlotItem) -> Unit
):AsyncEpoxyController() {
    var slotItemList : MutableList<SlotItem> = mutableListOf()
        set(value) {
            field = value
            requestModelBuild()
        }

    var dayIndex : Int = -1
        set(value) {
            field = value
            requestModelBuild()
        }

    override fun buildModels() {
        slotItemList.forEachIndexed { index, slotItem ->
            RoutineDaySlotItemModel(
                context,
                slotItem,
                onEditReminderClicked = { updatedSlotItem->
                    onUpdateReminderClicked(dayIndex, index, updatedSlotItem)
                }
            ).id("slotItem-$dayIndex-$index").addTo(this)
        }
    }
}