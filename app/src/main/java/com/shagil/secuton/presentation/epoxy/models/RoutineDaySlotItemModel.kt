package com.shagil.secuton.presentation.epoxy.models

import android.content.Context
import android.util.Log
import android.view.View
import android.widget.Toast
import com.shagil.secuton.R
import com.shagil.secuton.databinding.RoutineDaySlotsItemBinding
import com.shagil.secuton.domain.model.SlotItem
import com.shagil.secuton.util.ViewBindingKotlinModel

data class RoutineDaySlotItemModel(
    private val context: Context,
    private val slotItem:SlotItem,
    private val onEditReminderClicked : (SlotItem) -> Unit
):ViewBindingKotlinModel<RoutineDaySlotsItemBinding>(R.layout.routine_day_slots_item) {
    override fun RoutineDaySlotsItemBinding.bind() {
        startTimeView.text = slotItem.startTime
        endTimeView.text = slotItem.endTime
        reminderTitleTextview.text = slotItem.title
        reminderDescriptionTextview.text = slotItem.description

        editSlotReminderBtn.setOnClickListener {
            postSlotReminderBtn.visibility = View.VISIBLE
            editSlotReminderTitleEdittext.visibility = View.VISIBLE
            editSlotReminderTitleEdittext.setText(reminderTitleTextview.text)
            reminderTitleTextview.visibility = View.GONE

            editSlotReminderDescriptionEdittext.visibility = View.VISIBLE
            editSlotReminderDescriptionEdittext.setText(reminderDescriptionTextview.text)
            reminderDescriptionTextview.visibility = View.GONE
        }

        postSlotReminderBtn.setOnClickListener {
            val title = editSlotReminderTitleEdittext.text.toString().trim()
            val description = editSlotReminderDescriptionEdittext.text.toString().trim()
            if(title.isEmpty() || description.isEmpty()) {
                Toast.makeText(context, "Title/Description cannot be empty!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            Log.d("RoutineDaySlotItemModel", "$title $description")

            reminderTitleTextview.visibility = View.VISIBLE
            reminderDescriptionTextview.visibility = View.VISIBLE

            editSlotReminderTitleEdittext.visibility = View.GONE
            editSlotReminderDescriptionEdittext.visibility = View.GONE

            editSlotReminderBtn.visibility = View.VISIBLE
            postSlotReminderBtn.visibility = View.GONE

            onEditReminderClicked(
                SlotItem(
                    startTime = slotItem.startTime,
                    endTime = slotItem.endTime,
                    title = title,
                    description = description
                )
            )
        }
    }

}
