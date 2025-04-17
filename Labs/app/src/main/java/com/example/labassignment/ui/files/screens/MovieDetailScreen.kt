package com.example.labassignment.ui.files.screens

import android.content.ContentValues.TAG
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import coil3.compose.AsyncImage
import com.example.labassignment.model.Movie
import com.example.labassignment.model.Review
import com.example.labassignment.model.Video
import com.example.labassignment.utils.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun MovieDetailScreen(movie: Movie, onBackClick: () -> Unit) {
    val context = LocalContext.current
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
                            imageVector = Icons.Default.ArrowBack,
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
//            Text("Runtime: ${movie.runtime} mins")
//            Spacer(modifier = Modifier.height(8.dp))
//            Text("Tagline: ${movie.tagline ?: "N/A"}")
//            Spacer(modifier = Modifier.height(8.dp))
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

suspend fun fetchReviews(movieId: Int): List<Review> {
    val apiKey = Constants.API_KEY
    val url = "https://api.themoviedb.org/3/movie/$movieId/reviews?api_key=$apiKey&language=en-US&page=1"
    val client = OkHttpClient()
    val request = Request.Builder().url(url).build()

    return try {
        val response = client.newCall(request).execute()
        val json = response.body?.string() ?: return emptyList()
        val jsonObject = JSONObject(json)
        val results = jsonObject.getJSONArray("results")
        val reviews = mutableListOf<Review>()

        for (i in 0 until results.length()) {
            val reviewJson = results.getJSONObject(i)
            reviews.add(
                Review(
                    author = reviewJson.getString("author"),
                    content = reviewJson.getString("content")
                )
            )
        }
        Log.d(TAG, "Fetched ${reviews.size} reviews for movie $movieId")
        reviews
    } catch (e: Exception) {
        Log.e(TAG, "Error fetching reviews for movie $movieId: ${e.message}")
        emptyList()
    }
}

suspend fun fetchMovieVideos(movieId: Int): List<Video> {
    val apiKey = Constants.API_KEY
    val url = "https://api.themoviedb.org/3/movie/$movieId/videos?api_key=$apiKey&language=en-US"
    val client = OkHttpClient()
    val request = Request.Builder().url(url).build()

    return try {
        val response = client.newCall(request).execute()
        val json = response.body?.string() ?: return emptyList()
        val jsonObject = JSONObject(json)
        val results = jsonObject.getJSONArray("results")
        val videos = mutableListOf<Video>()

        for (i in 0 until results.length()) {
            val videoJson = results.getJSONObject(i)
            val site = videoJson.getString("site")
            val key = videoJson.getString("key")
            val videoUrl = when (site) {
                "YouTube" -> "https://www.youtube.com/watch?v=$key"
                "Vimeo" -> "https://vimeo.com/$key"
                else -> null
            }
            videoUrl?.let {
                videos.add(
                    Video(
                        id = videoJson.getString("id"),
                        name = videoJson.getString("name"),
                        site = site,
                        key = key,
                        url = it
                    )
                )
            }
        }
        Log.d(TAG, "Fetched ${videos.size} videos for movie $movieId")
        videos
    } catch (e: Exception) {
        Log.e(TAG, "Error fetching videos for movie $movieId: ${e.message}")
        emptyList()
    }
}