package com.example.diary.data.repository

import com.example.diary.model.Diary
import com.example.diary.util.RequestState
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

typealias diaries = RequestState <Map<LocalDate , List<Diary>>>

interface MogoRepository {
    fun configureTheRealm()
    fun getAllDiaries(): Flow<diaries>
}