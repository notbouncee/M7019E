package com.example.bingchilling.screens


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.bingchilling.database.AppDatabase
import com.example.bingchilling.model.Movie
import com.example.bingchilling.model.toMovie
import com.example.bingchilling.viewmodel.MovieDBViewModel


@Composable
fun FavoriteMoviesTab(
                      viewModel: MovieDBViewModel,
                      isConnected: Boolean,
                      db: AppDatabase) {

    // Own navhost
    val favNavController = rememberNavController()
    var favoriteMovies by remember { mutableStateOf<List<Movie>>(emptyList()) }


    LaunchedEffect(Unit) {
        db.movieDao().getAllFavoriteMovies().collect { favs ->
            favoriteMovies = favs.map { it.toMovie() }
        }
    }


    // 3) Host for "favList" → "Detail"
    NavHost(
        navController    = favNavController,
        startDestination = "favList",
        modifier         = Modifier.fillMaxSize()
    ) {
        // The favourites list screen
        composable("favList") {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Favourites",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                if (favoriteMovies.isEmpty()) {
                    Text(
                        text = "No favorite movies saved.",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(16.dp)
                    )
                } else {
                    LazyVerticalGrid(
                        columns               = GridCells.Fixed(2),
                        verticalArrangement   = Arrangement.spacedBy(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        modifier              = Modifier.fillMaxSize()
                    ) {
                        items(favoriteMovies) { movie ->
                            MovieListItemCard(movie = movie) {
                                // 4) Set in VM and navigate *this* controller
                                viewModel.setSelectedMovie(movie)
                                favNavController.navigate(MovieDBScreen.Detail.name)
                            }
                        }
                    }
                }
            }
        }

        // The shared detail screen
        composable(MovieDBScreen.Detail.name) {
            val selected = viewModel.uiState.collectAsState().value.selectedMovie
            if (selected != null) {
                MovieDetailScreen(
                    movie       = selected,
                    onBackClick = { favNavController.popBackStack() },
                    db          = db,
                    isConnected = isConnected
                )
            } else {
                // Fallback if somehow no movie was set
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Movie not found", Modifier.padding(16.dp))
                }
            }
        }
    }
}
