package com.example.notesapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.notesapp.ui.screens.EditNoteScreen
import com.example.notesapp.ui.screens.LoginScreen
import com.example.notesapp.ui.screens.NotesListScreen
import com.example.notesapp.viewmodel.AuthViewModel
import com.example.notesapp.viewmodel.NotesViewModel

@Composable
fun AppNavGraph(
    navController: NavHostController,
    authVm: AuthViewModel,
    notesVm: NotesViewModel
) {
    val start = if (authVm.ui.value.isLoggedIn) Routes.NOTES else Routes.LOGIN

    NavHost(navController = navController, startDestination = start) {
        composable(Routes.LOGIN) {
            LoginScreen(
                vm = authVm,
                onLoggedIn = {
                    navController.navigate(Routes.NOTES) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                }
            )
        }
        composable(Routes.NOTES) {
            NotesListScreen(
                authVm = authVm,
                notesVm = notesVm,
                onAdd = { navController.navigate(Routes.EDIT) },
                onEdit = { id -> navController.navigate("${Routes.EDIT}?noteId=$id") }
            )
        }
        composable(
            route = "${Routes.EDIT}?noteId={noteId}",
            arguments = listOf(navArgument("noteId") {
                type = NavType.LongType
                defaultValue = -1L
            })
        ) { backStack ->
            val noteId = backStack.arguments?.getLong("noteId") ?: -1L
            EditNoteScreen(
                notesVm = notesVm,
                noteId = noteId.takeIf { it != -1L },
                onDone = { navController.popBackStack() }
            )
        }
    }
}
