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
    private val context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        return@withContext try {
            val response = RetrofitInstance.api.getPopularMovies(Constants.API_KEY)
            val movieList = response.results
                .map { it.toMovie() }
                .map { it.toCachedEntity() }

            val db = AppDatabase.getDatabase(context)
            db.cachedMovieDAO().clearAll()
            db.cachedMovieDAO().insertAll(movieList)

            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }
}