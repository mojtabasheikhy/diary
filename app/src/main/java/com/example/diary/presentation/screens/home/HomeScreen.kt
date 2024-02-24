package com.example.diary.presentation.screens.home

import android.annotation.SuppressLint
import android.content.res.Resources
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerState
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.example.diary.R
import com.example.diary.data.repository.diaries
import com.example.diary.util.RequestState
import io.realm.kotlin.internal.platform.createDefaultSystemLogger

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(
    diaries: diaries,
    drawerState: DrawerState,
    onSignOutClicked: () -> Unit,
    onMenuClicked: () -> Unit, navigateToWrite: () -> Unit
) {
    var padding by remember {
        mutableStateOf(PaddingValues())
    }
    NavigationDrawer(drawerState = drawerState, onSignOutClicked = onSignOutClicked) {
        Scaffold(topBar = {
            HomeTopBar(onMenuClicked)
        }, floatingActionButton = {
            FloatingActionButton(
                modifier =  Modifier
                    .padding(end = padding.calculateEndPadding(layoutDirection = LayoutDirection.Ltr)),
                onClick = navigateToWrite) {
                Icon(imageVector = Icons.Default.Edit, contentDescription = "float Action button")
            }
        }, content = {
            padding = it
            when(diaries){
                is RequestState.Success->{

                    HomeContent(paddingValues = it,diaryNotes = diaries.data, onClick = {})
                }
                is RequestState.Loading->{
                    Log.e("sd","loading")
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
                        CircularProgressIndicator()
                    }
                }
                is RequestState.Error->{
                    Log.e("sd","e")
                    EmptyPage(
                        title = "error" ,
                        subtitle = diaries.error.message.toString())
                }
                else->{
                    Log.e("sd","else")
                }
            }
        })
    }
}

@Composable
fun NavigationDrawer(
    drawerState: DrawerState, onSignOutClicked: () -> Unit, content: @Composable () -> Unit
) {
    ModalNavigationDrawer(
        drawerState = drawerState, drawerContent = {
            ModalDrawerSheet(content = {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        modifier = Modifier.size(250.dp),
                        painter = painterResource(id = R.drawable.logo),
                        contentDescription = "App logo"
                    )
                }
                NavigationDrawerItem(
                    label = {
                        Row(modifier = Modifier.padding(horizontal = 12.dp)) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_google),
                                contentDescription = "googleLogo",
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = stringResource(id = R.string.logout),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    },
                    selected = false,
                    onClick = onSignOutClicked)
            })
        }, content = content
    )
}