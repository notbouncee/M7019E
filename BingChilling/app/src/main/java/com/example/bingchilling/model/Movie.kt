package com.example.bingchilling.model


import com.example.bingchilling.database.CachedMovieEntity
import com.example.bingchilling.database.MovieEntity
import com.google.gson.annotations.SerializedName

fun NetworkMovie.toMovie(): Movie {
    return Movie(
        id = id,
        title = title,
        poster_path = posterPath,
        backdrop_path = backdropPath,
        overview = overview,
        release_date = releaseDate,
        vote_average = voteAverage,
        popularity = popularity,
        adult = adult,
        video = video,
        original_language = originalLanguage,
        original_title = originalTitle,
        vote_count = voteCount,
        genres = genreIds.map { genreId ->
            Genre(id = genreId, name = genreMap[genreId] ?: "Unknown")
        }
    )
}


fun Movie.toEntity() = MovieEntity(
    id = id,
    adult = adult,
    backdrop_path = backdrop_path,
    genres = genres,
    original_language = original_language,
    original_title = original_title,
    overview = overview,
    popularity = popularity,
    poster_path = poster_path,
    release_date = release_date,
    title = title,
    video = video,
    vote_average = vote_average,
    vote_count = vote_count
)

fun MovieEntity.toMovie() = Movie(
    adult = adult,
    backdrop_path = backdrop_path,
    genres = genres,
    id = id,
    original_language = original_language,
    original_title = original_title,
    overview = overview,
    popularity = popularity,
    poster_path = poster_path,
    release_date = release_date,
    title = title,
    video = video,
    vote_average = vote_average,
    vote_count = vote_count
)

data class Movie(
    val adult: Boolean,
    val backdrop_path: String?,
    val genres: List<Genre>,
    val id: Int,
    val original_language: String,
    val original_title: String,
    val overview: String?,
    val popularity: Double,
    val poster_path: String?,
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

data class MovieResponse(
    val results: List<NetworkMovie>
)

data class ReviewResponse(
    val results: List<Review>
)


data class VideoResponse(
    val results: List<Video>
)


data class NetworkMovie(
    val id: Int,
    val title: String,
    @SerializedName("poster_path")
    val posterPath: String?,
    @SerializedName("backdrop_path")
    val backdropPath: String?,
    @SerializedName("overview")
    val overview: String?,
    @SerializedName("release_date")
    val releaseDate: String,
    @SerializedName("vote_average")
    val voteAverage: Double,
    val popularity: Double,
    val adult: Boolean,
    val video: Boolean,
    @SerializedName("original_language")
    val originalLanguage: String,
    @SerializedName("original_title")
    val originalTitle: String,
    val voteCount: Int,
    @SerializedName("genre_ids")
    val genreIds: List<Int>   // Only IDs here
)

fun Movie.toCachedEntity(viewType: String): CachedMovieEntity = CachedMovieEntity(
    id,
    adult,
    backdrop_path,
    genres,
    original_language,
    original_title,
    overview,
    popularity,
    poster_path,
    release_date,
    title,
    video,
    vote_average,
    vote_count,
    viewType = viewType
)

fun CachedMovieEntity.toMovie(): Movie = Movie(
    adult,
    backdrop_path,
    genres,
    id,
    original_language,
    original_title,
    overview,
    popularity,
    poster_path,
    release_date,
    title,
    video,
    vote_average,
    vote_count
)


val genreMap = mapOf(
    28 to "Action",
    12 to "Adventure",
    16 to "Animation",
    35 to "Comedy",
    80 to "Crime",
    99 to "Documentary",
    18 to "Drama",
    10751 to "Family",
    14 to "Fantasy",
    36 to "History",
    27 to "Horror",
    10402 to "Music",
    9648 to "Mystery",
    10749 to "Romance",
    878 to "Science Fiction",
    10770 to "TV Movie",
    53 to "Thriller",
    10752 to "War",
    37 to "Western"
)