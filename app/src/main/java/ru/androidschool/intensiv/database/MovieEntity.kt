package ru.androidschool.intensiv.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Movies")
data class MovieEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "movieId")
    val id: Long? = null,
    @ColumnInfo(name = "title")
    val title: String?
)
