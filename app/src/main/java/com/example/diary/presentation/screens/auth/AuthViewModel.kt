package com.example.diary.presentation.screens.auth

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.diary.util.Constants
import io.realm.kotlin.mongodb.App
import io.realm.kotlin.mongodb.Credentials
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.Exception

class AuthViewModel:ViewModel() {

    var loadingState = mutableStateOf(false)
        private set

    var authenticated = mutableStateOf(false)
        private set


    fun setLoading(loading :Boolean){
        loadingState.value = loading
    }
    fun signInWithMongoDbAtlas(tokenId: String , onSuccess : ()-> Unit , onError :(Exception)->Unit ){
        viewModelScope.launch {
            try {
               var result : Boolean = withContext(Dispatchers.IO){
                  App.Companion
                      .create(Constants.AppIdMongoDbServices)
                      .login(
                          //Credentials.google(token = tokenId , GoogleAuthType.ID_TOKEN)
                       Credentials.jwt(tokenId)
                      ) .loggedIn

               }
                withContext(Dispatchers.Main){
                    if (result) {
                        onSuccess()
                        delay(600)
                        authenticated.value = true
                    }else{
                        onError(Exception("User is not logged in."))
                    }
                }
            }catch (e:Exception){
              onError(e)
            }
        }
    }
}