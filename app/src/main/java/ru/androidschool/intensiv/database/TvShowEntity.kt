package ru.androidschool.intensiv.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "TvShows")
data class TvShowEntity(
    @PrimaryKey
    @ColumnInfo(name = "TvShowId")
    val tvShowId: Int?,
    @ColumnInfo(name = "name")
    val name: String?,
    @ColumnInfo(name = "posterPath")
    val posterPath: String?,
    @ColumnInfo(name = "backdropPath")
    val backdropPath: String?,
    @ColumnInfo(name = "genreIds")
    val genreIds: List<Int>?,
    @ColumnInfo(name = "originalLanguage")
    val originalLanguage: String?,
    @ColumnInfo(name = "originalName")
    val originalName: String?,
    @ColumnInfo(name = "overview")
    val overview: String?,
    @ColumnInfo(name = "popularity")
    val popularity: String?,
    @ColumnInfo(name = "firstAirDate")
    val firstAirDate: String?,
    @ColumnInfo(name = "originCountry")
    val originCountries: List<String>?,
    @ColumnInfo(name = "voteCount")
    val voteCount: Int?,
    @ColumnInfo(name = "voteAverage")
    val voteAverage: Float?
)
