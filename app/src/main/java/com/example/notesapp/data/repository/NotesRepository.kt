package com.example.notesapp.data.repository


import com.example.notesapp.data.local.Note
import com.example.notesapp.data.local.NoteDao
import kotlinx.coroutines.flow.Flow

class NotesRepository(private val dao: NoteDao) {
    fun observeNotes(): Flow<List<Note>> = dao.observeAll()
    suspend fun getNote(id: Long): Note? = dao.getById(id)
    suspend fun upsert(note: Note): Long = dao.upsert(note)
    suspend fun delete(note: Note) = dao.delete(note)
}
