package com.example.diary.model

import androidx.room.PrimaryKey
import com.example.diary.util.toRealmInstant
import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.ObjectId
import io.realm.kotlin.types.RealmInstant
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import java.time.Instant

open class Diary  :RealmObject {
    @io.realm.kotlin.types.annotations.PrimaryKey
    var _id:ObjectId = ObjectId.create()
    var ownerId:String = ""
    var mood = Mood.Neutral.name
    var title :String =""
    var description : String = ""
    var images:RealmList<String> = realmListOf()
    var date:RealmInstant = Instant.now().toRealmInstant()

}