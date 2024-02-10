package com.example.diary.presentation.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.diary.R

@Composable
fun DisplayAlertDialog(
    title: String,
    message: String,
    dialogOpened: Boolean,
    closeDialog: () -> Unit,
    onYesClicked: () -> Unit
) {
    if (dialogOpened){
        AlertDialog(

            title = {
                Text(
                    text = title,
                    fontSize = MaterialTheme.typography.headlineSmall.fontSize ,
                    fontWeight =  FontWeight.Bold
                    )
            },
            text = {
                Text(
                    text = message,
                    fontSize = MaterialTheme.typography.bodyMedium.fontSize ,
                    fontWeight =  FontWeight.Normal
                )
            },
            confirmButton = {
                Button(onClick =  onYesClicked) {
                    Text(
                        text = stringResource(id = R.string.yes)

                    )
                }
            } , dismissButton = {
                Button(onClick =  closeDialog) {
                    Text(
                        text = stringResource(id = R.string.no)

                    )
                }
            }, onDismissRequest = closeDialog
        )
    }
}