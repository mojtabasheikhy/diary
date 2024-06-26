package com.example.diary.presentation.screens.write

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.diary.R
import com.example.diary.model.Diary
import com.example.diary.model.Mood
import kotlinx.coroutines.launch
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun WriteContent(
    uiState: UiState,
    title:String,
    onTitleChange :(String)->Unit,
    description:String,
    onDescriptionChange :(String)->Unit,
    paddingValues: PaddingValues ,
    pagerState: PagerState,
    onSaveClicked:(Diary)->Unit){
    val scrollState = rememberScrollState()
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    var coroutineScope = rememberCoroutineScope()
    LaunchedEffect(key1 = scrollState.maxValue ){
        scrollState.scrollTo(scrollState.maxValue)
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
            .navigationBarsPadding()
            .padding(top = paddingValues.calculateTopPadding())
            .padding(bottom = 24.dp)
            .padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
           Column(modifier = Modifier
               .weight(1f)
               .verticalScroll(state = scrollState)) {
               Spacer(modifier = Modifier.height(30.dp) )
               HorizontalPager(state = pagerState, ) {page->
                   Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                       AsyncImage(
                           modifier = Modifier.size(120.dp),
                           model = ImageRequest
                               .Builder(LocalContext.current)
                               .data(Mood.entries[page].icon)
                               .crossfade(true)
                               .build(),
                           contentDescription = "Mood images"
                       )
                   }


               }
               Spacer(modifier = Modifier.height(30.dp))
               TextField(
                   modifier = Modifier.fillMaxWidth(),
                   value = title,
                   onValueChange = onTitleChange,
                   placeholder = {
                       Text(text = "Title")
                   },
                   colors = TextFieldDefaults.colors(
                     //  containerColor = Color.Transparent,
                       focusedIndicatorColor = Color.Unspecified ,
                       disabledIndicatorColor = Color.Unspecified,
                       unfocusedIndicatorColor = Color.Unspecified,
                       focusedPlaceholderColor =  MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
                   ),
                   keyboardOptions = KeyboardOptions(
                       imeAction = ImeAction.Next
                   ),
                   keyboardActions = KeyboardActions(
                       onNext = {
                           focusManager.moveFocus(FocusDirection.Down)
                           coroutineScope.launch {
                               scrollState.scrollTo(Int.MAX_VALUE )

                           }
                       }
                   ),
                   maxLines = 1,
                   singleLine = true
               )
               TextField(
                   modifier = Modifier
                       .fillMaxWidth()
                       .padding(top = 10.dp),
                   value = description,
                   onValueChange = onDescriptionChange,
                   placeholder = {
                       Text(text = "tell me about it")
                   },
                   colors = TextFieldDefaults.colors(
                       //  containerColor = Color.Transparent,
                       focusedIndicatorColor = Color.Unspecified ,
                       disabledContainerColor = Color.Unspecified,
                       disabledIndicatorColor = Color.Unspecified,
                       unfocusedIndicatorColor = Color.Unspecified,
                       focusedPlaceholderColor =  MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
                   ),
                   keyboardOptions = KeyboardOptions(
                       imeAction = ImeAction.Next
                   ),
                   keyboardActions = KeyboardActions(
                       onNext = {
                           focusManager.clearFocus()
                       }
                   )
               )

           }
        Column(verticalArrangement = Arrangement.Bottom) {
            Spacer(modifier = Modifier
                .fillMaxWidth()
                .height(12.dp)
            )
            Button(modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
                onClick = {
                     if (uiState.title.isNotEmpty() && uiState.description.isNotEmpty()){
                         onSaveClicked(Diary().apply {
                             this.title = uiState.title
                             this.description = uiState.description

                         })
                   }else {
                       Toast.makeText(context , "the title or description is empty" , Toast.LENGTH_LONG).show()
                   }
                },
                shape = Shapes().small
            ){
                Text(text = stringResource(id = R.string.save))
            }
        }
    }

}