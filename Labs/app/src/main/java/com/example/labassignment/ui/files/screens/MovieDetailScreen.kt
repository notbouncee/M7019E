package com.example.labassignment.ui.files.screens

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import coil3.compose.AsyncImage
import com.example.labassignment.model.Movie
import com.example.labassignment.model.Review
import com.example.labassignment.model.Video
import com.example.labassignment.network.RetrofitInstance
import com.example.labassignment.utils.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun MovieDetailScreen(movie: Movie, onBackClick: () -> Unit) {

    val scope = rememberCoroutineScope()
    var reviews by remember { mutableStateOf<List<Review>>(emptyList()) }
    var videos by remember { mutableStateOf<List<Video>>(emptyList()) }
    val scrollState = rememberScrollState()
    val density = LocalDensity.current
    val threshold = with(density) { 400.dp.toPx() }
    val opacity = (scrollState.value / threshold).coerceIn(0f, 1f)

    LaunchedEffect(movie.id) {
        scope.launch(Dispatchers.IO) {
            reviews = fetchReviews(movie.id)
            videos = fetchMovieVideos(movie.id)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { /* null */ },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White // White for contrast
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface.copy(alpha = opacity),
                    navigationIconContentColor = Color.White
                ),
                modifier = Modifier
                    .zIndex(1f)
            )
        },
        containerColor = Color.Transparent,
        modifier = Modifier.background(MaterialTheme.colorScheme.background)
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
            ) {
                // Cover Image
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(350.dp)
                ) {
                    AsyncImage(
                        model = movie.backdrop_path?.let { "https://image.tmdb.org/t/p/w780$it" }
                            ?: "https://image.tmdb.org/t/p/w500${movie.poster_path}",
                        contentDescription = "${movie.title} cover",
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(MaterialTheme.shapes.medium),
                        contentScale = ContentScale.Crop
                    )

                    // gradient overlay
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        MaterialTheme.colorScheme.background
                                    ),
                                    startY = 0.2f * 500f,
                                    endY = 900f
                                )
                            )
                    )
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .padding(bottom = 16.dp)
                        .padding(top = paddingValues.calculateTopPadding())
                ) {
                    Text(
                        text = movie.title,
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        movie.genres.forEach { genre ->
                            Surface(
                                shape = MaterialTheme.shapes.small,
                                color = MaterialTheme.colorScheme.secondaryContainer,
                                contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                                modifier = Modifier.padding(vertical = 2.dp)
                            ) {
                                Text(
                                    text = genre.name,
                                    style = MaterialTheme.typography.bodySmall,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = movie.overview ?: "N/A",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(16.dp))

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
                }
            }
        }
    }
}

suspend fun fetchReviews(movieId: Int): List<Review> {
    return try {
        RetrofitInstance.api.getMovieReviews(movieId, Constants.API_KEY).results
    } catch (e: Exception) {
        Log.e(TAG, "Error fetching reviews for movie $movieId: ${e.message}")
        emptyList()
    }
}

suspend fun fetchMovieVideos(movieId: Int): List<Video> {
    return try {
        val videos = RetrofitInstance.api.getMovieVideos(movieId, Constants.API_KEY).results

        // Map Video to include full URL if site is YouTube/Vimeo
        videos.mapNotNull { video ->
            val videoUrl = when (video.site) {
                "YouTube" -> "https://www.youtube.com/watch?v=${video.key}"
                "Vimeo" -> "https://vimeo.com/${video.key}"
                else -> null
            }
            videoUrl?.let {
                video.copy(url = it)
            }
        }
    } catch (e: Exception) {
        Log.e(TAG, "Error fetching videos for movie $movieId: ${e.message}")
        emptyList()
    }
}

