package com.example.bingchilling.screens


import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.HorizontalDivider
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.bingchilling.model.Movie




@Composable
fun MovieListScreen(
    popularMovies: List<Movie>,
    topRatedMovies: List<Movie>,
    onMovieClick: (Movie) -> Unit,
    selectedView: String,
    onViewTypeChange: (String) -> Unit,
    isConnected: Boolean) {

    val configuration = LocalConfiguration.current
    val cols = if (configuration.orientation ==
        Configuration.ORIENTATION_LANDSCAPE) 4 else 2

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        if (popularMovies.isEmpty() && topRatedMovies.isEmpty()) {
            if (!isConnected) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator()
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "No Internet Connection",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                return@Column
            } else {
                Text(
                    text = "Loading movies...",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(16.dp)
                )
                return@Column
            }
        }
        val moviesToShow = if (selectedView == "Popular Movies") popularMovies else topRatedMovies

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            FilterChip(
                selected = selectedView == "Popular Movies",
                onClick = { onViewTypeChange("Popular Movies") },
                label = { Text("Popular Movies") },
                colors = FilterChipDefaults.filterChipColors()
            )
            FilterChip(
                selected = selectedView == "Top Rated Movies",
                onClick = { onViewTypeChange("Top Rated Movies") },
                label = { Text("Top Rated Movies") },
                colors = FilterChipDefaults.filterChipColors()
            )
        }

        HorizontalDivider(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.outline
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(cols),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(moviesToShow) { movie ->
                MovieListItemCard(movie = movie, onClick = { onMovieClick(movie) })
            }
        }
    }
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