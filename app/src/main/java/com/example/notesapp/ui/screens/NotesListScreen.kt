package com.example.notesapp.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.notesapp.data.local.Note
import com.example.notesapp.viewmodel.AuthViewModel
import com.example.notesapp.viewmodel.NotesViewModel

@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
@Composable
fun NotesListScreen(
    authVm: AuthViewModel,
    notesVm: NotesViewModel,
    onAdd: () -> Unit,
    onEdit: (Long) -> Unit
) {
    val state = notesVm.ui.collectAsState().value

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Notes") },
                actions = { TextButton(onClick = authVm::logout) { Text("Logout") } }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAdd) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        }
    ) { padding ->
        if (state.notes.isEmpty()) {
            Box(Modifier.padding(padding).fillMaxSize()) {
                Text("No notes yet. Tap + to create one.", Modifier.padding(16.dp))
            }
        } else {
            LazyColumn(
                modifier = Modifier.padding(padding).fillMaxSize(),
                contentPadding = PaddingValues(12.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(state.notes) { note ->
                    NoteCard(
                        note = note,
                        onClick = { onEdit(note.id) },
                        onDelete = { notesVm.delete(note) }
                    )
                }
            }
        }
    }
}

@Composable
private fun NoteCard(note: Note, onClick: () -> Unit, onDelete: () -> Unit) {
    Card(Modifier.fillMaxWidth().clickable { onClick() }) {
        Column(Modifier.padding(12.dp)) {
            Text(note.title, style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(6.dp))
            Text(note.content, maxLines = 2)
            Spacer(Modifier.height(8.dp))
            TextButton(onClick = onDelete) { Text("Delete") }
        }
    }
}
