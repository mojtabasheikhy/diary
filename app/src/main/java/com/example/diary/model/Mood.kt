package com.example.diary.model

import androidx.compose.ui.graphics.Color
import com.example.diary.R
import com.example.diary.ui.theme.AngryColor
import com.example.diary.ui.theme.BoredColor
import com.example.diary.ui.theme.CalmColor
import com.example.diary.ui.theme.DepressedColor
import com.example.diary.ui.theme.DisappointedColor
import com.example.diary.ui.theme.HappyColor
import com.example.diary.ui.theme.HumorousColor
import com.example.diary.ui.theme.NeutralColor
import com.example.diary.ui.theme.ShamefulColor

enum class Mood(val icon: Int, val contentColor: Color, val containerColor: Color) {

    Neutral(
        icon = R.drawable.ic_neutral,
        contentColor = Color.Black,
        containerColor = NeutralColor
    ),
    Happy(
    icon = R.drawable.ic_happy,
    contentColor = Color.Black,
    containerColor = HappyColor
    ),
    Angry(
    icon = R.drawable.ic_angry,
    contentColor = Color.White,
    containerColor = AngryColor
    ),
    Bored(
    icon = R.drawable.ic_confused_mood,
    contentColor = Color.White,
    containerColor = BoredColor
    ),
    Calm(
        icon = R.drawable.ic_tongue_mood,
        contentColor = Color.Black,
        containerColor = CalmColor
    ),
    Depressed(
        icon = R.drawable.ic_depresed,
        contentColor = Color.Black,
        containerColor = DepressedColor
    ),
    Disappointed(
        icon = R.drawable.ic_sad,
        contentColor = Color.Black,
        containerColor = DisappointedColor
    ),
    Humorous(
        icon = R.drawable.ic_humorous,
        contentColor = Color.Black,
        containerColor = HumorousColor
    ),
    Shameful(
    icon = R.drawable.ic_shameful,
    contentColor = Color.Black,
    containerColor = ShamefulColor
    )




}