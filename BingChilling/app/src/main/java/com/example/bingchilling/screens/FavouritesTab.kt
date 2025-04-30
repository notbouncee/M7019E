package com.example.bingchilling.screens


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.bingchilling.database.AppDatabase
import com.example.bingchilling.model.Movie
import com.example.bingchilling.model.toMovie
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun FavoriteMoviesTab(onMovieClick: (Movie) -> Unit) {
    val context = LocalContext.current
    val db = remember { AppDatabase.getDatabase(context) }
    val scope = rememberCoroutineScope()
    var favoriteMovies by remember { mutableStateOf<List<Movie>>(emptyList()) }

    LaunchedEffect(Unit) {
        scope.launch(Dispatchers.IO) {
            db.movieDao().getAllFavoriteMovies().collect {
                favoriteMovies = it.map { entity -> entity.toMovie() }
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
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
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(favoriteMovies) { movie ->
                    MovieListItemCard(movie = movie) { onMovieClick(movie) }
                }
            }
        }
    }
}
