package com.example.labassignment.ui.files.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.labassignment.viewmodel.MovieDBViewModel



@Composable
fun MovieListScreen(modifier: Modifier = Modifier,
    viewModel: MovieDBViewModel = viewModel(),
    onMovieListItemClicked: (Movie) -> Unit
) {
    val movies by viewModel.popularMovies.collectAsState()

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier.padding(16.dp).fillMaxSize()
    ) {
        item(span = { GridItemSpan(2) }) {
            Text(
                text = "Popular Movies",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }
        items(movies) { movie ->
            MovieCard(movie = movie, onClick = { onMovieListItemClicked(movie) })
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
//
//@Composable
//fun MovieListItemCard(
//    movie: Movie, onClick: () -> Unit) {
//    Card(
//        modifier = Modifier.fillMaxSize(),
//        onClick = onClick
//    ) {
//        AsyncImage(
//            model = "https://image.tmdb.org/t/p/w500${movie.poster_path}",
//            contentDescription = movie.title,
//            modifier = Modifier.fillMaxSize()
//        )
//    }
//    Card(modifier = modifier, onClick = {
//        onMovieListItemClicked(movie)
//    })
//    {
//        Row {
//            Box {
//                Spacer(modifier = Modifier.size(20.dp))
//                AsyncImage(
//                    model = Constants.POSTER_IMAGE_BASE_URL + Constants.POSTER_IMAGE_WIDTH + movie.poster_path,
//                    contentDescription = movie.title,
//                    modifier = modifier.width(92.dp).height(138.dp),
//                    contentScale = ContentScale.Crop
//                )
//            }
//
//            Column {
//                Text(
//                    text = movie.title,
//                    style = MaterialTheme.typography.headlineSmall,
//                )
//                Spacer(modifier = Modifier.size(8.dp))
//                Text(
//                    text = movie.release_date,
//                    style = MaterialTheme.typography.bodySmall,
//                )
//                Spacer(modifier = Modifier.size(8.dp))
//                Text(
//                    text = movie.overview,
//                    style = MaterialTheme.typography.bodySmall,
//                    maxLines = 3,
//                    overflow = TextOverflow.Ellipsis
//                )
//                Spacer(modifier = Modifier.size(8.dp))
//                Text(
//                    text = movie.genres.joinToString { it.name },
//                    style = MaterialTheme.typography.bodySmall
//                )
//            }
//        }
//    }
//}

