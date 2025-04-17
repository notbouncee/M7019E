package com.example.labassignment.model

data class Movie(
    val adult: Boolean,
    val backdrop_path: String,
    val genres: List<Genre>,
    val id: Int,
    val original_language: String,
    val original_title: String,
    val overview: String,
    val popularity: Double,
    val poster_path: String,
    val release_date: String,
    val title: String,
    val video: Boolean,
    val vote_average: Double,
    val vote_count: Int
)

data class Review(
    val author: String,
    val content: String
)

data class Genre(val id: Int, val name: String)

data class Video(
    val id: String,
    val name: String,
    val site: String,
    val key: String,
    val url: String
)