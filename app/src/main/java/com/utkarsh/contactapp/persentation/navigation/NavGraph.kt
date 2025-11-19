package com.utkarsh.contactapp.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.utkarsh.contactapp.persentation.Screen.HomeScreen
import com.utkarsh.contactapp.persentation.navigation.Routes
import com.utkarsh.contactapp.presentation.screen.AddEditScreen
import com.utkarsh.contactapp.presentation.ContactViewModel

@Composable
fun NavGraph(navController: NavHostController, viewModel: ContactViewModel) {
    val state by viewModel.state.collectAsState()

    NavHost(
        navController = navController,
        startDestination = Routes.Home.route
    ) {
        composable(Routes.Home.route) {
            HomeScreen(
                navHostController = navController,
                state = state,
                viewModel = viewModel
            )
        }

        composable(Routes.AddEdit.route) {
            AddEditScreen(
                state = state,
                onEvent = { viewModel.saveContact() },
                navHostController = navController
            )
        }
    }
}
