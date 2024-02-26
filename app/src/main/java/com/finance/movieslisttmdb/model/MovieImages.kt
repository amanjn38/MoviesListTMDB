package com.finance.movieslisttmdb.model

import java.io.Serializable

data class MovieImages(
    val backdrops: List<Backdrop>,
    val id: Int,
    val logos: List<Logo>,
    val posters: List<Poster>
) : Serializable