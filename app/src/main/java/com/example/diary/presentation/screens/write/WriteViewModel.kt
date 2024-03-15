package com.example.diary.presentation.screens.write

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.diary.data.repository.MongoDB
import com.example.diary.model.Diary
import com.example.diary.model.Mood
import com.example.diary.util.Constants
import com.example.diary.util.RequestState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.mongodb.kbson.BsonObjectId
import org.mongodb.kbson.ObjectId

class WriteViewModel(private  val savedStateHandle : SavedStateHandle) : ViewModel() {
    var uiState by mutableStateOf(UiState())
      private set
    init {
        getDiaryIdArgument()
        fetchSelectedId()
    }
    private fun getDiaryIdArgument(){
        uiState = uiState.copy(
            selectedDiaryId = savedStateHandle.get<String>(key = Constants.WriteScreenArgumentKey)
        )
    }
    private fun fetchSelectedId(){
        if (uiState.selectedDiaryId != null) {
            viewModelScope.launch(Dispatchers.Main) {
                MongoDB.getSelectedDiary(diaryId = ObjectId(uiState.selectedDiaryId!!)).collect{
                    if (it is RequestState.Success){
                        setSelectedDiary(diary = it.data)
                        setTitle(title = it.data.title)
                        setDescription(des = it.data.description)
                        setMode(mood = Mood.valueOf(it.data.mood))

                    }
                }

            }
        }
    }
    fun setTitle(title: String){
        uiState = uiState.copy(title = title)
    }
    fun setDescription(des: String){
        uiState = uiState.copy(description = des)
    }

    private fun setMode(mood: Mood){
        uiState = uiState.copy(mood = mood)
    }
    private fun setSelectedDiary(diary: Diary){
        uiState  =uiState.copy(selectedDiary = diary)
    }
    fun insertDiary(
        diary: Diary ,
        onSuccess :()->Unit ,
        onError: (String)->Unit ){

        viewModelScope.launch(Dispatchers.IO) {
            val result = MongoDB.addNewDiary(diary)
            if (result is RequestState.Success){
                withContext(Dispatchers.Main) {
                    onSuccess()
                }
            }else if (result is RequestState.Error){
                withContext(Dispatchers.Main) {
                    onError(result.error.message.toString())
                }
            }
        }

    }

}
data class UiState(
    val selectedDiaryId :String? = null,
    val selectedDiary :Diary?  = null,
    val title : String = "" ,
    val description : String = "",
    val mood : Mood = Mood.Neutral
)