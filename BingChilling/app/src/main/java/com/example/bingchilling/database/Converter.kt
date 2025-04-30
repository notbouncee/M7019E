package com.example.bingchilling.database


import androidx.room.TypeConverter
import com.example.bingchilling.model.Genre
import org.json.JSONArray
import org.json.JSONObject

class Converters {
    @TypeConverter
    fun fromGenreList(genres: List<Genre>): String {
        val jsonArray = JSONArray()
        genres.forEach { genre ->
            val json = JSONObject().apply {
                put("id", genre.id)
                put("name", genre.name)
            }
            jsonArray.put(json)
        }
        return jsonArray.toString()
    }

    @TypeConverter
    fun toGenreList(genresString: String): List<Genre> {
        val jsonArray = JSONArray(genresString)
        val genres = mutableListOf<Genre>()
        for (i in 0 until jsonArray.length()) {
            val json = jsonArray.getJSONObject(i)
            genres.add(
                Genre(
                    id = json.getInt("id"),
                    name = json.getString("name")
                )
            )
        }
        return genres
    }
}