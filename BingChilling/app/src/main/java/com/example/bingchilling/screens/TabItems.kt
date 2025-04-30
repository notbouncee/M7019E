package com.example.bingchilling.screens


import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.bingchilling.viewmodel.MovieDBViewModel

sealed class TabItem(val title: String,
                     val icon: ImageVector,
                     val screen: @Composable () -> Unit) {
    object Home : TabItem("Home", Icons.Default.Home, { TheMovieDBApp() })
    data object Favourites : TabItem("Favourites", Icons.Default.Favorite, {
        val navController = rememberNavController()
        val viewModel: MovieDBViewModel = viewModel()

        FavoriteMoviesTab(onMovieClick = { movie ->
            viewModel.setSelectedMovie(movie)
            navController.navigate(MovieDBScreen.Detail.name)
        })
    })
}