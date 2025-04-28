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
import androidx.compose.runtime.collectAsState
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
import com.example.labassignment.utils.Constants
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.labassignment.model.toMovie
import com.example.labassignment.network.RetrofitInstance
import com.example.labassignment.viewmodel.MovieDBViewModel



enum class MovieDBScreen(@StringRes val title: Int){
    List(title = R.string.app_name),
    Detail(title = R.string.movie_detail)
}

@Composable
fun TheMovieDBApp(viewModel: MovieDBViewModel = viewModel()) {
    val navController = rememberNavController()
    val scope = rememberCoroutineScope()
    var popularMovies by remember { mutableStateOf<List<Movie>>(emptyList()) }
    var topRatedMovies by remember { mutableStateOf<List<Movie>>(emptyList()) }


    LaunchedEffect(Unit) {
        scope.launch(Dispatchers.IO) {
            try {
                val popularResponse = RetrofitInstance.api.getPopularMovies(Constants.API_KEY)
                val topRatedResponse = RetrofitInstance.api.getTopRatedMovies(Constants.API_KEY)

                popularMovies = popularResponse.results.map { it.toMovie() }
                topRatedMovies = topRatedResponse.results.map { it.toMovie() }
            } catch (e: Exception) {
                Log.e(TAG, "Error fetching movies: ${e.message}")
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = MovieDBScreen.List.name,
        modifier = Modifier.fillMaxSize()
    ) {
        composable(MovieDBScreen.List.name) {
            MovieListScreen(
                popularMovies = popularMovies,
                topRatedMovies = topRatedMovies,
                onMovieClick = { movie ->
                    viewModel.setSelectedMovie(movie)  //  viewModel handles selection
                    navController.navigate(MovieDBScreen.Detail.name)
                }
            )
        }
        composable(MovieDBScreen.Detail.name) {
            val movie = viewModel.uiState.collectAsState().value.selectedMovie
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




@Preview(showBackground = true)
@Composable
fun Preview() {
    LabAssignmentTheme {
        TheMovieDBApp()
    }
}
