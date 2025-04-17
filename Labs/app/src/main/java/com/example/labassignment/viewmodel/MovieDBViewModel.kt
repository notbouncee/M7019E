package com.example.labassignment.viewmodel

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.labassignment.database.MovieDBUIState
import com.example.labassignment.model.Genre
import com.example.labassignment.model.Movie
import com.example.labassignment.model.Video
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import com.example.labassignment.utils.Constants
import com.example.labassignment.model.Review

class MovieDBViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(MovieDBUIState())
    val uiState: StateFlow<MovieDBUIState> = _uiState.asStateFlow()

    private val _popularMovies = MutableStateFlow<List<Movie>>(emptyList())
    val popularMovies: StateFlow<List<Movie>> = _popularMovies.asStateFlow()

    init {
        fetchPopularMovies()
    }

    fun setSelectedMovie(movie: Movie) {
        _uiState.update { currentState ->
            currentState.copy(selectedMovie = movie)
        }
    }

    fun fetchPopularMovies() {
        viewModelScope.launch {
            val apiKey = Constants.API_KEY
            val url = "https://api.themoviedb.org/3/movie/popular?api_key=$apiKey&language=en-US&page=1"
            val client = OkHttpClient()
            val request = Request.Builder().url(url).build()

            try {
                val response = client.newCall(request).execute()
                val json = response.body?.string() ?: return@launch
                val results = JSONObject(json).getJSONArray("results")
                val genreMap = mapOf(
                    28 to "Action", 12 to "Adventure", 16 to "Animation", 35 to "Comedy", 80 to "Crime",
                    99 to "Documentary", 18 to "Drama", 10751 to "Family", 14 to "Fantasy", 36 to "History",
                    27 to "Horror", 10402 to "Music", 9648 to "Mystery", 10749 to "Romance",
                    878 to "Science Fiction", 10770 to "TV Movie", 53 to "Thriller", 10752 to "War", 37 to "Western"
                )
                val movies = mutableListOf<Movie>()

                for (i in 0 until results.length()) {
                    val movieJson = results.getJSONObject(i)
                    val genreIds = movieJson.getJSONArray("genre_ids")
                    val genres = mutableListOf<Genre>()
                    for (j in 0 until genreIds.length()) {
                        val id = genreIds.getInt(j)
                        genres.add(Genre(id, genreMap[id] ?: "Unknown"))
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
                _popularMovies.value = movies
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    suspend fun fetchMovieVideos(movieId: Int): List<Video> {
        val apiKey = Constants.API_KEY
        val url = "https://api.themoviedb.org/3/movie/$movieId/videos?api_key=$apiKey&language=en-US"
        val client = OkHttpClient()
        val request = Request.Builder().url(url).build()

        return try {
            withContext(Dispatchers.IO) {
                val response = client.newCall(request).execute()
                val json = response.body?.string() ?: return@withContext emptyList()
                val results = JSONObject(json).getJSONArray("results")
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
                videos
            }
        } catch (e: Exception) {
            Log.e("TMDB", "Failed to fetch videos: ${e.message}")
            emptyList()
        }
    }
    suspend fun fetchMovieReviews (movieId: Int): List<Review> {
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
}

