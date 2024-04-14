package com.courses.notesapp.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.courses.notesapp.api.NoteApi
import com.courses.notesapp.models.note.NoteRequest
import com.courses.notesapp.models.note.NoteResponse
import com.courses.notesapp.util.ErrorHandling
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject

class NoteRepository @Inject constructor(private val noteApi: NoteApi) {
    private val _noteLiveData = MutableLiveData<ErrorHandling<List<NoteResponse>>>()
    val noteLiveData: LiveData<ErrorHandling<List<NoteResponse>>>
        get() = _noteLiveData

    private val _noteStatus = MutableLiveData<ErrorHandling<String>>()
    val noteStatus: LiveData<ErrorHandling<String>>
        get() = _noteStatus

    suspend fun getNotes() {
        _noteLiveData.postValue(ErrorHandling.Loading())

        val response = noteApi.getNotes()
        if (response.isSuccessful && response.body() != null) {
            _noteLiveData.postValue(ErrorHandling.Success(response.body()!!))
        } else if(response.errorBody() != null){
            val errorObject = JSONObject(response.errorBody()!!.charStream().readText())
            _noteLiveData.postValue(
                ErrorHandling.Error(errorObject.getString("message"))
            )
        }else{
            _noteLiveData.postValue(ErrorHandling.Error("Something went wrong"))
        }
    }

    suspend fun createNote(noteRequest: NoteRequest){
        _noteLiveData.postValue(ErrorHandling.Loading())

        val response = noteApi.createNote(noteRequest)

        handledResponse(response, "Note Created")
    }

    suspend fun updateNote(noteId:String, noteRequest: NoteRequest){
        val response = noteApi.updateNote(noteId, noteRequest)
        handledResponse(response, "Note Updated")
    }

    suspend fun deleteNote(noteId:String){
        val response = noteApi.deleteNote(noteId)
        handledResponse(response, "Note Deleted")
    }

    private fun handledResponse(response: Response<NoteResponse>, message:String) {
        if (response.isSuccessful && response.body() != null) {
            _noteStatus.postValue(ErrorHandling.Success(message))
        } else {
            _noteStatus.postValue(ErrorHandling.Error("Something went wrong"))
        }
    }
}