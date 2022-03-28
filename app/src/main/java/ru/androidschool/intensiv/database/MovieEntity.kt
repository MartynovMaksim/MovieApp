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
    val posterPath: String?
)
