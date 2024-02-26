package com.finance.movieslisttmdb.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Poster(
    @SerializedName("aspect_ratio") val aspectRatio: Double,
    @SerializedName("file_path") val filePath: String,
    val height: Int,
    @SerializedName("iso_639_1") val isoLanguageCode: String,
    @SerializedName("vote_average") val voteAverage: Double,
    @SerializedName("vote_count") val voteCount: Int,
    val width: Int
) : Serializable