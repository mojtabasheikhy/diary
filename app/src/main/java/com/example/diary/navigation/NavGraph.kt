package com.example.diary.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.diary.Constants
import com.example.diary.Constants.WriteScreenArgumentKey
import com.example.diary.presentation.screens.auth.AuthenticationScreen
import com.example.diary.presentation.screens.home.HomeScreen
import com.example.diary.presentation.viewModel.AuthViewModel
import com.stevdzasan.messagebar.rememberMessageBarState
import com.stevdzasan.onetap.rememberOneTapSignInState
import io.realm.kotlin.mongodb.App
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun NavGraph(startDestination: String, navController: NavHostController) {
    NavHost(
        startDestination = startDestination,
        navController = navController
    ) {
        authenticationRoute(navigateToHome = {
            navController.popBackStack()
            navController.navigate(Screen.Home.route)
        })
        homeRoute(navigateToWrite = {
            navController.navigate(Screen.Write.route)
        })
        writeRoute()
    }

}

fun NavGraphBuilder.authenticationRoute(navigateToHome: () -> Unit) {
    composable(route = Screen.Authentication.route) {
        val oneTapState = rememberOneTapSignInState()
        val messageBarState = rememberMessageBarState()

        val viewModel: AuthViewModel = viewModel()
        val authenticated by viewModel.authenticated
        val loadingState by viewModel.loadingState
        AuthenticationScreen(
            authenticated = authenticated,
            oneTapSignInState = oneTapState,
            messageBarState = messageBarState,
            loadingState = loadingState,
            onClick = {
                oneTapState.open()
                viewModel.setLoading(true)
            },
            onDialogDismissed = {
                messageBarState.addError(Exception(it))

            },
            onTokenIdReceived = { tokenId ->
                viewModel.signInWithMongoDbAtlas(
                    tokenId = tokenId,
                    onSuccess = {

                        messageBarState.addSuccess("Hi")
                        viewModel.setLoading(false)
                    },
                    onError = {
                        messageBarState.addError(Exception(it))
                        viewModel.setLoading(false)
                    }
                )

            },
            navigateToHome = navigateToHome
        )
    }
}

fun NavGraphBuilder.homeRoute(navigateToWrite: () -> Unit) {
    composable(route = Screen.Home.route) {
        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
        val scope = rememberCoroutineScope()
        HomeScreen(onMenuClicked = {
            scope.launch {
                drawerState.open()
            }
        }, navigateToWrite = navigateToWrite, onSignOutClicked = {}, drawerState = drawerState)
    }
}

fun NavGraphBuilder.writeRoute() {
    composable(
        route = Screen.Write.route,
        arguments = listOf(navArgument(name = WriteScreenArgumentKey) {
            type = NavType.StringType
            nullable = true
            defaultValue = null
        })
    ) {

    }
}