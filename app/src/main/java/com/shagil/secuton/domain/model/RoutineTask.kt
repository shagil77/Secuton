package com.shagil.secuton.domain.model

data class RoutineTask(
    val dayName:String?="",
    val slots: List<SlotItem>? = emptyList()
)
