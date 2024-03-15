package com.example.diary.presentation.screens.write

import androidx.activity.OnBackPressedCallback
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.example.diary.model.Diary
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.toUpperCase
import com.example.diary.R
import com.example.diary.presentation.components.DisplayAlertDialog
import com.example.diary.util.toInstant
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WriteTopBar(
    selectedDiary: Diary?,
    onBackPress :()-> Unit,
    onDeleteConfirmed : ()->Unit,
    moodName :()-> String
    ) {
    val currentDate by remember { mutableStateOf(LocalDate.now()) }
    val currentTime by remember { mutableStateOf(LocalTime.now())}
    val formattedDate = remember (key1 = currentDate){
        DateTimeFormatter
            .ofPattern("dd MMM yyyy")
            .format(currentDate)
            .uppercase()
    }
    val formattedTime = remember (key1 = currentDate){
        DateTimeFormatter
            .ofPattern("hh:mm a")
            .format(currentTime)
            .uppercase()
    }
    val selectedDiaryDateTime = remember(selectedDiary) {
           if(selectedDiary!=null){
               SimpleDateFormat("dd MMM yyyy , hh:mm a", Locale.getDefault())
                   .format(Date.from(selectedDiary.date.toInstant())).uppercase()
           }else {

               "$formattedDate ,$formattedTime"
           }

    }
    CenterAlignedTopAppBar( navigationIcon = {
        IconButton(onClick = {
            onBackPress()
        }) {
            Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back Arrow Icon")
            
        }
    },title = {
        Column {
            Text(
                modifier = Modifier.fillMaxWidth(), text = moodName() ,
                style = TextStyle(fontSize = MaterialTheme.typography.titleLarge.fontSize,   fontWeight = FontWeight.Bold),
                textAlign =  TextAlign.Center

            )
            Text(
                modifier = Modifier.fillMaxWidth(), text =selectedDiaryDateTime ,
                style = TextStyle(fontSize = MaterialTheme.typography.bodySmall.fontSize),
                textAlign =  TextAlign.Center
            )
        }
    } , actions = {
        IconButton(onClick = {}) {
            Icon(
                imageVector = Icons.Default.DateRange, 
                contentDescription = "Date Icon",
                tint = MaterialTheme.colorScheme.onSurface)
        }
        if (selectedDiary != null){
           DeleteDiaryAction(selectedDiary = selectedDiary , onDeleteConfirmed = onDeleteConfirmed )
        }

    } )
}
@Composable
fun DeleteDiaryAction(selectedDiary: Diary? , onDeleteConfirmed : ()->Unit){
    var expanded by remember {
        mutableStateOf(false)
    }
    var openDialog by remember {
        mutableStateOf(false)
    }
    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
        DropdownMenuItem(text = {Text(text = "Delete")}  , onClick = {
            openDialog = true
            expanded = false
        })
    }

    DisplayAlertDialog(
        title = stringResource(id = R.string.Delete),
        message = "Are you Suer you Want to permanently delete this diary note '${selectedDiary?.title}'",
        dialogOpened = openDialog,
        closeDialog = { openDialog = false }, onYesClicked = {
            onDeleteConfirmed() }
    )
    IconButton(onClick = {expanded = !expanded}) {
        Icon(
            imageVector = Icons.Default.MoreVert , contentDescription = "overflow Menu Icon",
            tint = MaterialTheme.colorScheme.onSurface
        )
    }

}