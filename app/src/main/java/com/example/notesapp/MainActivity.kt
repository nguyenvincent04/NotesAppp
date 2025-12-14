package com.example.notesapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.example.notesapp.data.local.NotesDatabase
import com.example.notesapp.data.repository.AuthRepository
import com.example.notesapp.data.repository.NotesRepository
import com.example.notesapp.ui.navigation.AppNavGraph
import com.example.notesapp.ui.theme.NotesAppTheme
import com.example.notesapp.viewmodel.AuthViewModel
import com.example.notesapp.viewmodel.NotesViewModel
import com.google.firebase.auth.FirebaseAuth

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = Room.databaseBuilder(
            applicationContext,
            NotesDatabase::class.java,
            "notes.db"
        ).build()

        val notesVm = NotesViewModel(NotesRepository(db.noteDao()))
        val authVm = AuthViewModel(AuthRepository(FirebaseAuth.getInstance()))

        setContent {
            NotesAppTheme {
                val navController = rememberNavController()
                AppNavGraph(navController, authVm, notesVm)
            }
        }
    }
}
