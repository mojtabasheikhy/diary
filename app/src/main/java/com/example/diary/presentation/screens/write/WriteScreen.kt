package com.example.diary.presentation.screens.write

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.example.diary.model.Diary
import com.example.diary.model.Mood
import java.time.ZonedDateTime

@OptIn(ExperimentalFoundationApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun WriteScreen(
    onBackPress :()-> Unit ,
    onDeleteConfirmed : ()->Unit ,
    moodName : ()->String,
    pageState : PagerState,
    uiState: UiState,
    onTitleChange :(String)->Unit,
    onDescription :(String)->Unit ,
    onSavedClicked : (Diary)->Unit,
    onDateTimeUpdated : (ZonedDateTime)->Unit
) {

    LaunchedEffect(key1 = uiState.mood ){
        pageState.scrollToPage(Mood.valueOf(uiState.mood.name).ordinal)
    }
    Scaffold (
        topBar = {
           WriteTopBar(
               onBackPress = onBackPress ,
               onDeleteConfirmed = onDeleteConfirmed ,
               selectedDiary = uiState.selectedDiary ,
               moodName =moodName ,
               onDateTimeUpdated = onDateTimeUpdated)
        }, content = {
            WriteContent(
                uiState = uiState,
                paddingValues = it ,
                pagerState = pageState ,
                title = uiState.title,
                description = uiState.description ,
                onDescriptionChange = onDescription ,
                onTitleChange =onTitleChange ,
                onSaveClicked = onSavedClicked
                )
        }
    )
}