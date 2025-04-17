package com.example.labassignment.ui.files.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.material3.Text
import androidx.compose.runtime.setValue
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.labassignment.model.Movie
import com.example.labassignment.model.Video
import com.example.labassignment.utils.Constants
import com.example.labassignment.viewmodel.MovieDBViewModel
import com.example.labassignment.model.Review

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieDetailScreen(modifier: Modifier = Modifier,
                      movie: Movie,
                      viewModel: MovieDBViewModel = MovieDBViewModel()
                      ) {
    val context = LocalContext.current
    var videos by remember { mutableStateOf<List<Video>>(emptyList()) }
    var reviews by remember { mutableStateOf<List<Review>>(emptyList()) }

    LaunchedEffect(movie.id) {
        videos = viewModel.fetchMovieVideos(movie.id)
        reviews = viewModel.fetchMovieReviews(movie.id)
    }


    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = movie.title)
                }
            )
        }
    ) { padding ->
        Column(
            modifier = modifier
                .padding(padding)
                .padding(16.dp)
        ) {
            //Box {
            //Spacer(modifier = Modifier.size(20.dp))
            AsyncImage(
                model = Constants.BACKDROP_IMAGE_BASE_URL + Constants.BACKDROP_IMAGE_BASE_WIDTH + movie.backdrop_path,
                contentDescription = movie.title,
                modifier = Modifier.fillMaxWidth().height(200.dp)
            )
            //}
            Spacer(modifier = Modifier.height(8.dp))

            Text("Genres: ${movie.genres.joinToString { it.name }}",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis)

            Spacer(modifier = Modifier.height(8.dp))

            Text("Overview: ${movie.overview ?: "N/A"}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis)

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Release Date",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = movie.release_date,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Rating",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "${movie.vote_average}/10",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Reviews",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(8.dp))
            if (reviews.isEmpty()) {
                Text(
                    text = "No reviews available",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            } else {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(reviews) { review ->
                        ReviewDisplay(review = review)
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Trailers",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(8.dp))
            if (videos.isEmpty()) {
                Text(
                    text = "No trailers available",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            } else {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(videos) { video ->
                        VideoDisplay(video = video)
                    }
                }
            }
//            movie.homepage?.let {
//                Text(
//                    text = "Homepage",
//                    style = MaterialTheme.typography.bodyMedium.copy(
//                        color = MaterialTheme.colorScheme.primary,
//                        textDecoration = TextDecoration.Underline
//                    ),
//                    modifier = Modifier.clickable {
//                        context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(it)))
//                    }
//                )
//            }
//            Text(
//                text = "Open in IMDb",
//                style = MaterialTheme.typography.bodyMedium.copy(
//                    color = MaterialTheme.colorScheme.primary,
//                    textDecoration = TextDecoration.Underline
//                ),
//                modifier = Modifier.clickable {
//                    val imdbUrl = "https://www.imdb.com/title/${movie.imdb_id}/"
//                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(imdbUrl))
//                    context.startActivity(intent)
//                }
//            )
        }
    }
}
@Composable
fun MovieCard(movie: Movie, onClick: () -> Unit) {
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