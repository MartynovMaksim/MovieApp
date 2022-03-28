package ru.androidschool.intensiv.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Movies")
data class MovieEntity(
    @PrimaryKey
    @ColumnInfo(name = "movieId")
    val movieId: Int?,
    @ColumnInfo(name = "title")
    val title: String?,
    @ColumnInfo(name = "posterPath")
    val posterPath: String?,
    @ColumnInfo(name = "adult")
    val isAdult: Boolean?,
    @ColumnInfo(name = "backdropPath")
    val backdropPath: String?,
    @ColumnInfo(name = "genreIds")
    val genreIds: List<Int>?,
    @ColumnInfo(name = "originalLanguage")
    val originalLanguage: String?,
    @ColumnInfo(name = "originalTitle")
    val originalTitle: String?,
    @ColumnInfo(name = "overview")
    val overview: String?,
    @ColumnInfo(name = "popularity")
    val popularity: Double?,
    @ColumnInfo(name = "releaseDate")
    val releaseDate: String?,
    @ColumnInfo(name = "video")
    val video: Boolean?,
    @ColumnInfo(name = "voteCount")
    val voteCount: Int?,
    @ColumnInfo(name = "voteAverage")
    val voteAverage: Float?
)
