@file:kotlin.OptIn(ExperimentalMaterial3Api::class)
@file:Suppress("INFERRED_TYPE_VARIABLE_INTO_EMPTY_INTERSECTION_WARNING")

package com.example.bingchilling.screens

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.bingchilling.R
import com.example.bingchilling.ui.theme.BingChillingTheme
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bingchilling.database.AppDatabase
import com.example.bingchilling.model.toMovie
import com.example.bingchilling.viewmodel.MovieDBViewModel
import com.example.bingchilling.utils.isNetworkConnected
import androidx.compose.runtime.DisposableEffect
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.example.bingchilling.workmanager.MovieSyncWorker


enum class MovieDBScreen(@StringRes val title: Int){
    List(title = R.string.app_name),
    Detail(title = R.string.movie_detail)
}

@Composable
fun TheMovieDBApp(viewModel: MovieDBViewModel = viewModel()) {
    val navController = rememberNavController()
    // Context + WorkManager + Room
    val context       = LocalContext.current
    val workManager   = WorkManager.getInstance(context)
    val db            = remember { AppDatabase.getDatabase(context) }

    // Network connectivity
    var isConnected by remember { mutableStateOf(isNetworkConnected(context)) }

    // Selected view-state: use the Worker constants directly
    var viewType by remember { mutableStateOf(MovieSyncWorker.VIEW_POPULAR) }

//    var cachedViewType by remember { mutableStateOf("Popular Movies") } // default
//    var cachedMovies by remember { mutableStateOf<List<Movie>>(emptyList()) }



    // Connectivity monitoring
    DisposableEffect(Unit) {
        val manager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val callback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) { isConnected = true }
            override fun onLost(network: Network) { isConnected = false }
        }
        val request = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()
        manager.registerNetworkCallback(request, callback)
        onDispose { manager.unregisterNetworkCallback(callback) }
    }

    // Fetch Movies
    LaunchedEffect(viewType, isConnected) {
        if (isConnected) {
            val inputData = workDataOf(
                MovieSyncWorker.KEY_VIEW_STATE to viewType
            )
            val workReq = OneTimeWorkRequestBuilder<MovieSyncWorker>()
                .setInputData(inputData)
                .build()

            workManager.enqueueUniqueWork(
                /* uniqueName = */ "sync_movies",
                /* policy     = */ ExistingWorkPolicy.REPLACE,
                /* work       = */ workReq
            )
        }
    }

    // Collect the cached movies for the current viewType from Room
    val cachedEntities by db.cachedMovieDAO()
        .getByViewType(viewType)
        .collectAsState(initial = emptyList())

    // Map entities back to your domain model
    val movies = cachedEntities.map { it.toMovie() }

    NavHost(
        navController = navController,
        startDestination = MovieDBScreen.List.name,
        modifier = Modifier.fillMaxSize()
    ) {
        composable(MovieDBScreen.List.name) {
            MovieListScreen(
                popularMovies   = if (viewType == MovieSyncWorker.VIEW_POPULAR) movies else emptyList(),
                topRatedMovies  = if (viewType == MovieSyncWorker.VIEW_TOP_RATED) movies else emptyList(),
                selectedView    = if (viewType == MovieSyncWorker.VIEW_POPULAR)
                    "Popular Movies"
                else
                    "Top Rated Movies",
                onViewTypeChange = { newLabel ->
                    viewType = if (newLabel == "Top Rated Movies")
                        MovieSyncWorker.VIEW_TOP_RATED
                    else
                        MovieSyncWorker.VIEW_POPULAR
                },
                isConnected = isConnected,
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
                    onBackClick = { navController.popBackStack() },
                    db = db,
                    isConnected = isConnected
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
    BingChillingTheme {
        TheMovieDBApp()
    }
}
