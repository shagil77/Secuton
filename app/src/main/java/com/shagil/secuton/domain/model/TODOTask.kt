package com.shagil.secuton.domain.model

data class TODOTask(
    val tid:String?="",
    val title:String?="",
    val description:String?="",
    val deadline:String?="",
    val priority:Int?=0,
    val broadcastId:String?=""
)
