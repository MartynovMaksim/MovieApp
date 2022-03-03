package ru.androidschool.intensiv.data

import java.io.Serializable

class Movie(
    var title: String? = "",
    var voteAverage: Double = 0.0
) : Serializable {
    val rating: Float
        get() = voteAverage.div(2).toFloat()
}
