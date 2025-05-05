package com.example.bingchilling.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CachedMovieDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(movies: List<CachedMovieEntity>)

    @Query("DELETE FROM Cached")
    suspend fun clearAll()

    @Query("SELECT * FROM Cached WHERE viewType = :viewType")
    fun getByViewType(viewType: String): Flow<List<CachedMovieEntity>>
}