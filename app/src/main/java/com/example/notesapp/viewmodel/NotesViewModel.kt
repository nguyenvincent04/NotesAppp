package com.example.notesapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notesapp.data.local.Note
import com.example.notesapp.data.repository.NotesRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class NotesUiState(val notes: List<Note> = emptyList())

class NotesViewModel(private val repo: NotesRepository) : ViewModel() {

    val ui: StateFlow<NotesUiState> =
        repo.observeNotes()
            .map { NotesUiState(it) }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), NotesUiState())

    private val _editing = MutableStateFlow<Note?>(null)
    val editing: StateFlow<Note?> = _editing

    fun loadForEdit(id: Long) {
        viewModelScope.launch { _editing.value = repo.getNote(id) }
    }

    fun clearEdit() { _editing.value = null }

    fun delete(note: Note) {
        viewModelScope.launch { repo.delete(note) }
    }

    fun save(id: Long?, title: String, content: String) {
        viewModelScope.launch {
            repo.upsert(
                Note(
                    id = id ?: 0,
                    title = title,
                    content = content,
                    updatedAt = System.currentTimeMillis()
                )
            )
        }
    }
}
