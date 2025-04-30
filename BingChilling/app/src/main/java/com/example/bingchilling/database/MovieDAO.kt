package com.example.bingchilling.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovie(movie: MovieEntity)

    @Query("DELETE FROM Favourite WHERE id = :movieId")
    suspend fun deleteMovie(movieId: Int)

    @Query("SELECT * FROM Favourite")
    fun getAllFavoriteMovies(): Flow<List<MovieEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM Favourite WHERE id = :movieId)")
    suspend fun isFavorite(movieId: Int): Boolean
}