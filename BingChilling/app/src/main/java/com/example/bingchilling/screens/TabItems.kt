package com.example.bingchilling.screens


import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import com.example.bingchilling.database.AppDatabase
import com.example.bingchilling.viewmodel.MovieDBViewModel

sealed class TabItem(val title: String,
                     val icon: ImageVector,
                     val screen: @Composable (NavController, MovieDBViewModel, AppDatabase, Boolean) -> Unit) {
    data object Home : TabItem("Home", Icons.Default.Home, { _, viewModel, _, _ ->
        TheMovieDBApp(viewModel = viewModel)
    })

    data object Favourites : TabItem("Favourites", Icons.Default.Favorite, { _, viewModel, db, isConnected ->
        FavoriteMoviesTab(
            viewModel = viewModel,
            isConnected = isConnected,
            db = db
        )
    })
}