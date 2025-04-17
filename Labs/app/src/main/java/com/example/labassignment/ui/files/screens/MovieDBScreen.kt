@file:kotlin.OptIn(ExperimentalMaterial3Api::class)
@file:Suppress("INFERRED_TYPE_VARIABLE_INTO_EMPTY_INTERSECTION_WARNING")

package com.example.labassignment.ui.files.screens

import android.content.ContentValues.TAG
import android.util.Log
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.labassignment.R
import com.example.labassignment.ui.files.aesthetics.LabAssignmentTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.example.labassignment.model.Movie
import com.example.labassignment.model.Genre
import com.example.labassignment.utils.Constants
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject


enum class MovieDBScreen(@StringRes val title: Int){
    List(title = R.string.app_name),
    Detail(title = R.string.movie_detail)
}

@Composable
fun TheMovieDBApp() {
    val navController = rememberNavController()
    val scope = rememberCoroutineScope()
    var popularMovies by remember { mutableStateOf<List<Movie>>(emptyList()) }
    var topRatedMovies by remember { mutableStateOf<List<Movie>>(emptyList()) }

    LaunchedEffect(Unit) {
        scope.launch(Dispatchers.IO) {
            popularMovies = fetchPopularMovies()
            topRatedMovies = fetchTopRatedMovies()
        }
    }

    NavHost(
        navController = navController,
        startDestination = "movie_list",
        modifier = Modifier.fillMaxSize()
    ) {
        composable("movie_list") {
            MovieListScreen(
                popularMovies = popularMovies,
                topRatedMovies = topRatedMovies,
                onMovieClick = { movie ->
                    navController.navigate("movie_detail/${movie.id}")
                }
            )
        }
        composable("movie_detail/{movieId}") { backStackEntry ->
            val movieId = backStackEntry.arguments?.getString("movieId")?.toIntOrNull()
            val allMovies = popularMovies + topRatedMovies
            val movie = allMovies.find { it.id == movieId }
            if (movie != null) {
                MovieDetailScreen(
                    movie = movie,
                    onBackClick = { navController.popBackStack() }
                )
            } else {
                Text(
                    text = "Movie not found",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}

suspend fun fetchPopularMovies(): List<Movie> {
    val apiKey = Constants.API_KEY
    val url = "https://api.themoviedb.org/3/movie/popular?api_key=$apiKey&language=en-US&page=1"
    val client = OkHttpClient()
    val request = Request.Builder().url(url).build()

    return try {
        val response = client.newCall(request).execute()
        val json = response.body?.string() ?: return emptyList()
        val jsonObject = JSONObject(json)
        val results = jsonObject.getJSONArray("results")
        val movies = mutableListOf<Movie>()

        for (i in 0 until minOf(results.length(), 20)) {
            val movieJson = results.getJSONObject(i)
            val genres = mutableListOf<Genre>()
            val genreIds = movieJson.getJSONArray("genre_ids")
            for (j in 0 until genreIds.length()) {
                val genreId = genreIds.getInt(j)
                val genreName = genreMap[genreId] ?: "Unknown"
                genres.add(Genre(genreId, genreName))
            }
            movies.add(
                Movie(
                    adult = movieJson.optBoolean("adult", false),
                    backdrop_path = movieJson.optString("backdrop_path", null),
                    genres = genres,
                    id = movieJson.getInt("id"),
                    original_language = movieJson.getString("original_language"),
                    original_title = movieJson.getString("original_title"),
                    overview = movieJson.optString("overview", null),
                    popularity = movieJson.getDouble("popularity"),
                    poster_path = movieJson.optString("poster_path", null),
                    release_date = movieJson.getString("release_date"),
                    title = movieJson.getString("title"),
                    video = movieJson.optBoolean("video", false),
                    vote_average = movieJson.getDouble("vote_average"),
                    vote_count = movieJson.getInt("vote_count")
                )
            )
        }
        Log.i(TAG, "Fetched ${movies.size} popular movies")
        movies
    } catch (e: Exception) {
        Log.e(TAG, "Error fetching popular movies: ${e.message}")
        emptyList()
    }
}

suspend fun fetchTopRatedMovies(): List<Movie> {
    val apiKey = Constants.API_KEY
    val url = "https://api.themoviedb.org/3/movie/top_rated?api_key=$apiKey&language=en-US&page=1"
    val client = OkHttpClient()
    val request = Request.Builder().url(url).build()

    return try {
        val response = client.newCall(request).execute()
        val json = response.body?.string() ?: return emptyList()
        val jsonObject = JSONObject(json)
        val results = jsonObject.getJSONArray("results")
        val movies = mutableListOf<Movie>()

        for (i in 0 until minOf(results.length(), 20)) {
            val movieJson = results.getJSONObject(i)
            val genres = mutableListOf<Genre>()
            val genreIds = movieJson.getJSONArray("genre_ids")
            for (j in 0 until genreIds.length()) {
                val genreId = genreIds.getInt(j)
                val genreName = genreMap[genreId] ?: "Unknown"
                genres.add(Genre(genreId, genreName))
            }
            movies.add(
                Movie(
                    adult = movieJson.optBoolean("adult", false),
                    backdrop_path = movieJson.optString("backdrop_path", null),
                    genres = genres,
                    id = movieJson.getInt("id"),
                    original_language = movieJson.getString("original_language"),
                    original_title = movieJson.getString("original_title"),
                    overview = movieJson.optString("overview", null),
                    popularity = movieJson.getDouble("popularity"),
                    poster_path = movieJson.optString("poster_path", null),
                    release_date = movieJson.getString("release_date"),
                    title = movieJson.getString("title"),
                    video = movieJson.optBoolean("video", false),
                    vote_average = movieJson.getDouble("vote_average"),
                    vote_count = movieJson.getInt("vote_count")
                )
            )
        }
        Log.i(TAG, "Fetched ${movies.size} top-rated movies")
        movies
    } catch (e: Exception) {
        Log.e(TAG, "Error fetching top-rated movies: ${e.message}")
        emptyList()
    }
}


val genreMap = mapOf(
    28 to "Action",
    12 to "Adventure",
    16 to "Animation",
    35 to "Comedy",
    80 to "Crime",
    99 to "Documentary",
    18 to "Drama",
    10751 to "Family",
    14 to "Fantasy",
    36 to "History",
    27 to "Horror",
    10402 to "Music",
    9648 to "Mystery",
    10749 to "Romance",
    878 to "Science Fiction",
    10770 to "TV Movie",
    53 to "Thriller",
    10752 to "War",
    37 to "Western"
)

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    LabAssignmentTheme {
        TheMovieDBApp()
    }
}
