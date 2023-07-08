package com.example.comity

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.comity.ui.home.StudyGroup

class SelectedStudyGroupsViewModel : ViewModel() {
    val selectedStudyGroups: MutableLiveData<List<StudyGroup>> = MutableLiveData()
}
