package com.example.diary.data.repository

import com.example.diary.model.Diary
import com.example.diary.util.RequestState
import kotlinx.coroutines.flow.Flow
import org.mongodb.kbson.ObjectId
import java.time.LocalDate

typealias diaries = RequestState <Map<LocalDate , List<Diary>>>

interface MongoRepository {
    fun configureTheRealm()
    fun getAllDiaries(): Flow<diaries>
    fun getSelectedDiary(diaryId : ObjectId):Flow<RequestState<Diary>>
    suspend fun addNewDiary(diary: Diary):RequestState<Diary>
    suspend fun updateDiary(diary: Diary):RequestState<Diary>
    suspend fun deleteDiary(id : ObjectId):RequestState<Diary>
}