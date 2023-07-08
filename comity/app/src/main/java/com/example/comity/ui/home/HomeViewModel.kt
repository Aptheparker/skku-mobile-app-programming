package com.example.comity.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.comity.R

class HomeViewModel : ViewModel() {

    private val _studyGroupList = MutableLiveData<List<StudyGroup>>()
    val studyGroupList: LiveData<List<StudyGroup>> get() = _studyGroupList

    private val _text = MutableLiveData<String>().apply {
        value = "Home"
    }
    val text: LiveData<String> = _text

    init {
        // Initialize the studyGroupList with the initial data
        val initialStudyGroupList = getStudyGroupList()
        _studyGroupList.value = initialStudyGroupList
    }

    private fun getStudyGroupList(): List<StudyGroup> {
        return listOf(
            StudyGroup("Javascript", "ap", 3, 5,false, R.drawable.js, "JS study for web development.", "Seoul"),
            StudyGroup("React.js", "David", 2, 3,false,R.drawable.react,"To make a great web with React", "Paris"),
            StudyGroup("Vue.js", "Kevin", 2, 4,false,R.drawable.vue,"Best library forever","San Francisco"),
            StudyGroup("Python", "Elvis", 3, 8,false,R.drawable.python,"Basic Python grammar","Tokyo"),
            StudyGroup("PS", "Ian", 2, 5,false,R.drawable.app_logo,"Data Structure and Algorithm","Shanghai"),
            StudyGroup("C Language", "James", 9, 10,false,R.drawable.c,"Let's make a console terminal with C","Washington"),
            StudyGroup("AI", "Alex", 4, 5,false,R.drawable.ai,"Learn how to use AI models","Paris"),
            StudyGroup("Kotlin", "Lisa", 2, 2,false,R.drawable.kotlin,"Make an app with Kotlin","Texas"),
        )
    }
}
