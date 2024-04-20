package com.courses.notesapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.courses.notesapp.models.note.NoteRequest
import com.courses.notesapp.repository.NoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(private val noteRepository: NoteRepository):ViewModel(){
    val noteLiveData get() = noteRepository.noteLiveData

    //todo for create, update and delete
    val statusLiveData get() = noteRepository.noteStatus

    fun getNotes() {
        viewModelScope.launch {
            noteRepository.getNotes()
        }
    }


    fun createNote(noteRequest: NoteRequest) {
        viewModelScope.launch {
            noteRepository.createNote(noteRequest)
        }
    }

    fun updateNote(noteId: String, noteRequest: NoteRequest) {
        viewModelScope.launch {
            noteRepository.updateNote(noteId, noteRequest)
        }
    }

    fun deleteNote(noteId:String){
        viewModelScope.launch {
            noteRepository.deleteNote(noteId)
        }
    }
}