package com.example.diary.presentation.screens.write

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import com.example.diary.model.Diary

@OptIn(ExperimentalFoundationApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun WriteScreen(onBackPress :()-> Unit ,selectedDiary :Diary ? , onDeleteConfirmed : ()->Unit ,pageState : PagerState) {
    Scaffold (
        topBar = {
           WriteTopBar(onBackPress = onBackPress , onDeleteConfirmed = onDeleteConfirmed , selectedDiary = selectedDiary)
        }, content = {
            WriteContent(paddingValues = it , pagerState = pageState , title = "", description ="" , onDescriptionChange ={} , onTitleChange ={} )
        }
    )
}