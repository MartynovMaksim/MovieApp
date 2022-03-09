package ru.androidschool.intensiv.data

object MockRepository {

    fun getMovies(): List<MovieMock> {

        val moviesList = mutableListOf<MovieMock>()
        for (x in 0..10) {
            val movie = MovieMock(
                title = "Spider-Man $x",
                voteAverage = 10.0 - x
            )
            moviesList.add(movie)
        }

        return moviesList
    }
}
