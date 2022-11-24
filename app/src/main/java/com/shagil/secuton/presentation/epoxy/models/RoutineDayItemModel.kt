package com.shagil.secuton.presentation.epoxy.models

import androidx.recyclerview.widget.LinearSnapHelper
import com.shagil.secuton.R
import com.shagil.secuton.databinding.ModelRoutineDayItemBinding
import com.shagil.secuton.domain.model.RoutineTask
import com.shagil.secuton.presentation.epoxy.controllers.RoutineDaySlotsController
import com.shagil.secuton.util.ViewBindingKotlinModel

data class RoutineDayItemModel(
    private val routineDaySlotsController: RoutineDaySlotsController,
    private val dayName:String
):ViewBindingKotlinModel<ModelRoutineDayItemBinding>(R.layout.model_routine_day_item) {
    override fun ModelRoutineDayItemBinding.bind() {
        dayNameTextview.text = dayName

        modelRoutineDayEpoxyRecyclerView.setControllerAndBuildModels(routineDaySlotsController)
    }
}