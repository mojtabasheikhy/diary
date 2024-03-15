package com.example.diary.data.repository

import android.util.Log
import com.example.diary.model.Diary
import com.example.diary.util.Constants
import com.example.diary.util.RequestState
import com.example.diary.util.toInstant
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import io.realm.kotlin.log.LogLevel
import io.realm.kotlin.mongodb.App
import io.realm.kotlin.mongodb.sync.SyncConfiguration
import io.realm.kotlin.query.Sort
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import org.mongodb.kbson.ObjectId
import java.time.ZoneId

object MongoDB : MongoRepository {
    private val app = App.Companion.create(Constants.AppIdMongoDbServices)
    private val user = app.currentUser
    private lateinit var realm: Realm

    init {
        configureTheRealm()
    }

    override fun configureTheRealm() {
        if (user != null) {
            val config = SyncConfiguration.Builder(user, setOf(Diary::class))
                .initialSubscriptions { sub ->
                    add(
                        //query = sub.query("ownerId == $0 AND title==$1",user.identities )
                        query = sub.query<Diary>("ownerId == $0", user.id),
                        name = "User's Diaries"
                    )
                }
                .log(LogLevel.ALL)
                .build()

            realm = Realm.open(config)

        }
    }

    override fun getAllDiaries(): Flow<diaries> {
        return if (user != null) {
            try {
                realm.query<Diary>(query = "ownerId == $0", user.id)
                    .sort(property = "date", sortOrder = Sort.DESCENDING)
                    .asFlow()
                    .map { result ->
                        RequestState.Success(data = result.list.groupBy {
                            it.date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
                        })

                    }

            } catch (e: Exception) {
                flow { emit(RequestState.Error(e)) }
            }
        } else {
            flow { emit(RequestState.Error(UserNotAuthenticatedException())) }
        }
    }

    override fun getSelectedDiary(diaryId: ObjectId): Flow<RequestState<Diary>> {
      return  if(user != null){
            try {
                realm.query<Diary>(query = "_id == $0" , diaryId).asFlow().map {
                    RequestState.Success(data = it.list.first())
                }
            }catch (e:Exception){
                flow { emit(RequestState.Error(e)) }
            }
        }else {
            flow { emit(RequestState.Error(UserNotAuthenticatedException())) }
        }
    }

    override suspend  fun addNewDiary(diary: Diary): RequestState<Diary> {
        return if(user != null){

                realm.write {
                    try {
                        val addedDiary =copyToRealm(diary.apply {
                            ownerId = user.identity

                        })
                        RequestState.Success(data = addedDiary)
                    }catch (e :Exception){

                    }
                }
                RequestState.Success(diary)

        }else {
            RequestState.Error(UserNotAuthenticatedException())
        }
    }

}

private class UserNotAuthenticatedException : Exception("User is not Logged in.")