package com.example.diary.presentation.screens.home

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.diary.data.repository.MongoDB
import com.example.diary.data.repository.diaries
import com.example.diary.util.RequestState
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    var diaries: MutableState<diaries> = mutableStateOf(RequestState.Idle)

    init {
        observeAllDiaries()
    }

    private fun observeAllDiaries() {
        viewModelScope.launch {
            MongoDB.getAllDiaries().collect { result ->
                diaries.value = result
            }
        }
    }
}