package com.example.comity.ui.home

data class StudyGroup(
    val groupName: String,
    val leaderName: String,
    var currentNum: Int,
    val maxNum: Int,
    var joined: Boolean = false,
    val imageResource: Int,
    val groupDescription:String,
    val groupLocation: String,

)
