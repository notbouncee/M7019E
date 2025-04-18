package com.example.labassignment.ui.files.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.labassignment.model.Movie
import com.example.labassignment.utils.Constants



@Composable
fun MovieListScreen(
    popularMovies: List<Movie>,
    topRatedMovies: List<Movie>,
    onMovieClick: (Movie) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        if (popularMovies.isEmpty() && topRatedMovies.isEmpty()) {
            Text(
                text = "Loading movies...",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                item(span = { GridItemSpan(2) }) {
                    Text(
                        text = "Popular Movies",
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }
                items(popularMovies) { movie ->
                    MovieListItemCard(movie = movie, onClick = { onMovieClick(movie) })
                }
                item(span = { GridItemSpan(2) }) {
                    Text(
                        text = "Top Rated Movies",
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }
                items(topRatedMovies) { movie ->
                    MovieListItemCard(movie = movie, onClick = { onMovieClick(movie) })
                }
            }
        }
    }
//    Column (modifier = modifier.fillMaxSize()){
//        // For the Header
//        Box(modifier = Modifier
//            .fillMaxWidth()
//            .height(60.dp)
//            .background(Color(0xFF2196F3)),
//            contentAlignment = Alignment.CenterStart)
//        {
//            Text(
//                text = "Top Movie Picks",
//                style = MaterialTheme.typography.headlineMedium
//            )
//        }
//    }
//    LazyColumn(modifier = modifier) {
//        items(movieList) { movie ->
//            MovieListItemCard(
//                movie = movie,
//                onMovieListItemClicked,
//                modifier = Modifier.padding(8.dp))
//
//        }
//    }
}
@Composable
fun MovieListItemCard(
    movie: Movie, onClick: () -> Unit
    ) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(260.dp)
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            contentColor = MaterialTheme.colorScheme.onSurfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            AsyncImage(
                model = "https://image.tmdb.org/t/p/w500${movie.poster_path}",
                contentDescription = "${movie.title} poster",
                modifier = Modifier
                    .fillMaxHeight(),
                contentScale = ContentScale.Crop
            )

        }
    }
}