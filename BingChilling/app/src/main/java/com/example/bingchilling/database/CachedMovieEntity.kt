package com.example.bingchilling.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.bingchilling.model.Genre

@Entity(tableName = "Cached")
data class CachedMovieEntity(
    @PrimaryKey val id: Int,
    val adult: Boolean,
    val backdrop_path: String?,
    val genres: List<Genre>,
    val original_language: String,
    val original_title: String,
    val overview: String?,
    val popularity: Double,
    val poster_path: String?,
    val release_date: String,
    val title: String,
    val video: Boolean,
    val vote_average: Double,
    val vote_count: Int,
    val viewType: String      // either "popular" or "top_rated"
)