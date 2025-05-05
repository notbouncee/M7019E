package com.example.bingchilling.workmanager

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.bingchilling.database.AppDatabase
import com.example.bingchilling.model.toCachedEntity
import com.example.bingchilling.model.toMovie
import com.example.bingchilling.network.RetrofitInstance
import com.example.bingchilling.utils.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception


class MovieSyncWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    companion object {
        const val KEY_VIEW_STATE  = "view_state"
        const val VIEW_POPULAR    = "Popular Movies"
        const val VIEW_TOP_RATED  = "Top Rated Movies"
    }

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        val viewState = inputData.getString(KEY_VIEW_STATE) ?: VIEW_POPULAR

        return@withContext try {
            // 1) Pick endpoint
            val resp = when(viewState) {
                VIEW_TOP_RATED -> RetrofitInstance.api.getTopRatedMovies(Constants.API_KEY)
                else           -> RetrofitInstance.api.getPopularMovies(Constants.API_KEY)
            }

            // 2) Map & tag
            val list = resp.results
                .map { it.toMovie() }
                .map { it.toCachedEntity(viewState) }

            // 3) Clear old + insert new
            val db = AppDatabase.getDatabase(applicationContext)
            db.cachedMovieDAO().clearAll()
            db.cachedMovieDAO().insertAll(list)

            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }
}
