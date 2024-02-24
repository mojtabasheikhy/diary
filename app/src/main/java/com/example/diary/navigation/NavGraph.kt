package com.example.diary.navigation

import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.diary.util.Constants
import com.example.diary.util.Constants.WriteScreenArgumentKey
import com.example.diary.R
import com.example.diary.data.repository.MongoDB
import com.example.diary.presentation.components.DisplayAlertDialog
import com.example.diary.presentation.screens.auth.AuthenticationScreen
import com.example.diary.presentation.screens.home.HomeScreen
import com.example.diary.presentation.viewModel.AuthViewModel
import com.example.diary.presentation.viewModel.HomeViewModel
import com.example.diary.util.RequestState
import com.stevdzasan.messagebar.rememberMessageBarState
import com.stevdzasan.onetap.rememberOneTapSignInState
import io.realm.kotlin.mongodb.App
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun NavGraph(startDestination: String, navController: NavHostController,onDataLoaded: () -> Unit) {
    NavHost(
        startDestination = startDestination,
        navController = navController
    ) {
        authenticationRoute(navigateToHome = {
            navController.popBackStack()
            navController.navigate(Screen.Home.route)
        },onDataLoaded = onDataLoaded)
        homeRoute(navigateToWrite = {
            navController.navigate(Screen.Write.route)
        }, navigateToAuth ={
             navController.popBackStack()
                 navController.navigate(Screen.Authentication.route)
        },onDataLoaded = onDataLoaded)
        writeRoute()
    }

}

fun NavGraphBuilder.authenticationRoute(navigateToHome: () -> Unit,onDataLoaded: () -> Unit) {
    composable(route = Screen.Authentication.route) {
        val oneTapState = rememberOneTapSignInState()
        val messageBarState = rememberMessageBarState()

        val viewModel: AuthViewModel = viewModel()
        val authenticated by viewModel.authenticated
        val loadingState by viewModel.loadingState
        LaunchedEffect(key1 = Unit ){
            onDataLoaded
        }

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

fun NavGraphBuilder.homeRoute(navigateToWrite: () -> Unit ,navigateToAuth : ()->Unit,onDataLoaded : ()->Unit) {
    composable(route = Screen.Home.route) {
        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
        val viewModel :HomeViewModel = viewModel()
        val diaries by viewModel.diaries
        var signOutDialogState by remember {
            mutableStateOf(false)
        }
        val scope = rememberCoroutineScope()
        LaunchedEffect(key1 = diaries ){
            if (diaries !is RequestState.Loading){
                onDataLoaded()
            }
        }

        HomeScreen(diaries = diaries,onMenuClicked = {
            scope.launch {
                drawerState.open()
            }
        }, navigateToWrite = navigateToWrite,
            onSignOutClicked = {
            signOutDialogState = true
        }, drawerState = drawerState)

        LaunchedEffect(key1 = Unit ){
            MongoDB.configureTheRealm()

        }
        DisplayAlertDialog(
            title = stringResource(id = R.string.SignOut),
            message = stringResource(id = R.string.SignOutMessage),
            dialogOpened = signOutDialogState,
            onYesClicked = {
                scope.launch (Dispatchers.IO){
                    val user = App.create(Constants.AppIdMongoDbServices).currentUser
                    if (user !=null) {
                        user.logOut()
                        withContext(Dispatchers.Main){
                            navigateToAuth()
                        }

                    }
                }

                signOutDialogState = false},
            closeDialog = {signOutDialogState =false}
        )
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