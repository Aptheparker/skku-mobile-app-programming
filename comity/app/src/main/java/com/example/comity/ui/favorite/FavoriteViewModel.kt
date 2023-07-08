
package com.example.comity.ui.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.comity.ui.home.StudyGroup

class FavoriteViewModel : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "Favorite"
    }
    val text: LiveData<String> = _text
}
