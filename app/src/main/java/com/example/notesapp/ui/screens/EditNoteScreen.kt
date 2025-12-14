package com.example.notesapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.notesapp.viewmodel.NotesViewModel
@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)

@Composable
fun EditNoteScreen(
    notesVm: NotesViewModel,
    noteId: Long?,
    onDone: () -> Unit
) {
    val editing = notesVm.editing.collectAsState().value

    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }

    LaunchedEffect(noteId) {
        if (noteId != null) notesVm.loadForEdit(noteId) else notesVm.clearEdit()
    }

    LaunchedEffect(editing?.id) {
        if (editing != null && noteId != null) {
            title = editing.title
            content = editing.content
        }
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text(if (noteId == null) "New Note" else "Edit Note") }) }
    ) { padding ->
        Column(Modifier.padding(padding).padding(16.dp).fillMaxSize()) {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Title") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = content,
                onValueChange = { content = it },
                label = { Text("Content") },
                modifier = Modifier.fillMaxWidth().height(220.dp)
            )
            Spacer(Modifier.height(16.dp))

            Button(
                onClick = {
                    notesVm.save(noteId, title, content)
                    notesVm.clearEdit()
                    onDone()
                },
                enabled = title.isNotBlank() || content.isNotBlank(),
                modifier = Modifier.fillMaxWidth()
            ) { Text("Save") }
        }
    }
}
