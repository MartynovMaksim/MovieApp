package ru.androidschool.intensiv.data.mapper

import ru.androidschool.intensiv.data.Movie
import ru.androidschool.intensiv.database.MovieEntity

object MovieMapper {

    fun toEntity(movie: Movie): MovieEntity = movie.run {
        MovieEntity(
            movieId = id,
            title = title,
            posterPath = posterPath,
            isAdult = isAdult,
            backdropPath = backdropPath,
            genreIds = genreIds,
            originalLanguage = originalLanguage,
            originalTitle = originalTitle,
            overview = overview,
            popularity = popularity,
            releaseDate = releaseDate,
            video = video,
            voteCount = voteCount,
            voteAverage = voteAverage
        )
    }

    fun toData(entity: MovieEntity): Movie = entity.run {
        Movie(
            entity.isAdult,
            entity.backdropPath,
            entity.genreIds,
            entity.movieId,
            entity.originalLanguage,
            entity.originalTitle,
            entity.overview,
            entity.popularity,
            entity.releaseDate,
            entity.title,
            entity.video,
            entity.voteCount
        ).apply {
            posterPath = entity.posterPath
            voteAverage = entity.voteAverage?.times(2F)
        }
    }
}