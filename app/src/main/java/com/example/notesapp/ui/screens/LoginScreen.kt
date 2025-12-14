package com.example.notesapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.notesapp.viewmodel.AuthViewModel

@Composable
fun LoginScreen(vm: AuthViewModel, onLoggedIn: () -> Unit) {
    val state = vm.ui.collectAsState().value
    if (state.isLoggedIn) onLoggedIn()

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("NotesApp", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = state.email,
            onValueChange = vm::onEmailChange,
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(12.dp))

        OutlinedTextField(
            value = state.password,
            onValueChange = vm::onPasswordChange,
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(16.dp))

        state.error?.let {
            Text(it, color = MaterialTheme.colorScheme.error)
            Spacer(Modifier.height(8.dp))
        }

        Button(
            onClick = vm::login,
            enabled = !state.isLoading,
            modifier = Modifier.fillMaxWidth()
        ) { Text(if (state.isLoading) "Logging in..." else "Login") }

        Spacer(Modifier.height(10.dp))

        OutlinedButton(
            onClick = vm::register,
            enabled = !state.isLoading,
            modifier = Modifier.fillMaxWidth()
        ) { Text("Create Account") }
    }
}
