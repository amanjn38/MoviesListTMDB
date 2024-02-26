package com.finance.movieslisttmdb.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Crew(
    val adult: Boolean,
    @SerializedName("credit_id") val creditId: String,
    val department: String,
    val gender: Int,
    val id: Int,
    val job: String,
    @SerializedName("known_for_department") val knownForDepartment: String,
    val name: String,
    @SerializedName("original_name") val originalName: String,
    val popularity: Double,
    @SerializedName("profile_path") val profilePath: String
) : Serializable
