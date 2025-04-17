package com.example.labassignment.ui.files.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.labassignment.model.Movie
import com.example.labassignment.utils.Constants

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieDetailScreen(movie: Movie, modifier: Modifier) {
    val context = LocalContext.current

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
            Box {
                Spacer(modifier = Modifier.size(20.dp))
                AsyncImage(
                    model = Constants.BACKDROP_IMAGE_BASE_URL + Constants.BACKDROP_IMAGE_BASE_WIDTH + movie.backdrop_path,
                    contentDescription = movie.title,
                    modifier = modifier.width(400.dp).height(128.dp),
                    contentScale = ContentScale.Crop
                )
            }
            Text("Genres: ${movie.genres.joinToString { it.name }}")
            Spacer(modifier = Modifier.height(8.dp))
            Text("Overview: ${movie.overview ?: "N/A"}")
            Spacer(modifier = Modifier.height(8.dp))
            Text("Release Date: ${movie.release_date}")
            Spacer(modifier = Modifier.height(8.dp))
            Text("Rating: ${movie.vote_average}/10")
            Spacer(modifier = Modifier.height(8.dp))
            Text("Runtime: ${movie.runtime} mins")
            Spacer(modifier = Modifier.height(8.dp))
            Text("Tagline: ${movie.tagline ?: "N/A"}")
            Spacer(modifier = Modifier.height(8.dp))
            movie.homepage?.let {
                Text(
                    text = "Homepage",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.primary,
                        textDecoration = TextDecoration.Underline
                    ),
                    modifier = Modifier.clickable {
                        context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(it)))
                    }
                )
            }
            Text(
                text = "Open in IMDb",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.primary,
                    textDecoration = TextDecoration.Underline
                ),
                modifier = Modifier.clickable {
                    val imdbUrl = "https://www.imdb.com/title/${movie.imdb_id}/"
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(imdbUrl))
                    context.startActivity(intent)
                }
            )
        }
    }
}
