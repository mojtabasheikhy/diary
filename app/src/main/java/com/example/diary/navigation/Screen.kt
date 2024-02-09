package com.example.diary.navigation

import com.example.diary.Constants.WriteScreenArgumentKey

sealed class Screen (val route :String) {
    data object Authentication:Screen(route = "authentication_screen")
    data object Home:Screen(route = "home_screen")
    data object Write:Screen(route = "write_screen?$WriteScreenArgumentKey=" +
            "{$WriteScreenArgumentKey}"){
        fun passDiaryId(diaryId :String) = "write_screen?${WriteScreenArgumentKey}=$diaryId"
    }
}