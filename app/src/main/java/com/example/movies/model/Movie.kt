package com.example.movies.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Movie(
    @field:SerializedName("adult") val adult: Boolean,
    @field:SerializedName("backdrop_path") val backdropPath: String,
    @field:SerializedName("genre_ids") val genreIds: List<Int>,
    @field:SerializedName("id") val id: Int,
    @field:SerializedName("original_language") val originalLanguage: String,
    @field:SerializedName("original_title") val originalTitle: String,
    @field:SerializedName("overview") val overview: String,
    @field:SerializedName("popularity") val popularity: Double,
    @field:SerializedName("poster_path") val posterPath: String,
    @field:SerializedName("release_date") val releaseDate: String,
    @field:SerializedName("title") val title: String,
    @field:SerializedName("video") val video: Boolean,
    @field:SerializedName("vote_average") val voteAverage: Double,
    @field:SerializedName("vote_count") val voteCount: Int
) : Parcelable {

    val combineTitle
        get() = if (title == originalTitle) title else "$title($originalTitle)"

    val prefixOriginalTitle
        get() = "Original title: $originalTitle"

    val rDate
        get() = "Release date: $releaseDate"

    val info
        get() = "$releaseDate．$originalLanguage${if (adult) "．R" else ""}${if (video) "．VOD" else ""}"

    val ranking
        get() = "$voteAverage"

    val rankingSuffix
        get() = "/10．$voteCount"

    val popularIdx
        get() = "$popularity"
}