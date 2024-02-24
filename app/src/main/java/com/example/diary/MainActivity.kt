package com.example.diary

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.navigation.compose.rememberNavController
import com.example.diary.data.repository.MongoDB
import com.example.diary.navigation.NavGraph
import com.example.diary.navigation.Screen
import com.example.diary.ui.theme.DiaryTheme
import com.example.diary.util.Constants
import io.realm.kotlin.mongodb.App

class MainActivity : ComponentActivity() {
    private var keepSplashOpened = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().setKeepOnScreenCondition{
            keepSplashOpened
        }
        WindowCompat.setDecorFitsSystemWindows(window , false )
        setContent {
            DiaryTheme {
                val navController = rememberNavController()
                NavGraph(startDestination = getStartDestination(), navController = navController , onDataLoaded = {
                    keepSplashOpened = false
                })
            }
        }
    }
}
fun getStartDestination():String{
    val user = App.Companion.create(Constants.AppIdMongoDbServices).currentUser
    return if (user!=null && user.loggedIn) Screen.Home.route
    else Screen.Authentication.route

}

