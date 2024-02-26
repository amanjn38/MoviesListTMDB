package com.finance.movieslisttmdb.model

import java.io.Serializable

data class MovieCredits(
    val cast: List<Cast>,
    val crew: List<Crew>,
    val id: Int
) : Serializable