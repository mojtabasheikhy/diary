package com.example.diary

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.navigation.compose.rememberNavController
import com.example.diary.navigation.NavGraph
import com.example.diary.navigation.Screen
import com.example.diary.ui.theme.DiaryTheme
import io.realm.kotlin.mongodb.App

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        WindowCompat.setDecorFitsSystemWindows(window , false )
        setContent {
            DiaryTheme {
                val navController = rememberNavController()
                NavGraph(startDestination = getStartDestination(), navController = navController)
            }
        }
    }
}
fun getStartDestination():String{
    val user = App.Companion.create(Constants.AppIdMongoDbServices).currentUser
    return if (user!=null && user.loggedIn) Screen.Home.route
    else Screen.Authentication.route

}

