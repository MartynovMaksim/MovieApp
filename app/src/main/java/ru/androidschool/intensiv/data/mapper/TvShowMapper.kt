package ru.androidschool.intensiv.data.mapper

import ru.androidschool.intensiv.data.TvShow
import ru.androidschool.intensiv.database.TvShowEntity

object TvShowMapper {

    fun toEntity(tvShow: TvShow): TvShowEntity = tvShow.run {
        TvShowEntity(
            tvShowId = id,
            name = name,
            posterPath = posterPath,
            backdropPath = backdropPath,
            genreIds = genreIds,
            originalLanguage = originalLanguage,
            originalName = originalName,
            overview = overview,
            popularity = popularity,
            firstAirDate = firstAirDate,
            originCountries = originCountries,
            voteCount = voteCount,
            voteAverage = voteAverage
        )
    }

    fun toData(entity: TvShowEntity): TvShow = entity.run {
        TvShow(
            entity.popularity,
            entity.tvShowId,
            entity.backdropPath,
            entity.overview,
            entity.firstAirDate,
            entity.originCountries,
            entity.genreIds,
            entity.originalLanguage,
            entity.voteCount,
            entity.name,
            entity.originalName
        ).apply {
            posterPath = entity.posterPath
            voteAverage = entity.voteAverage?.times(2F)
        }
    }
}