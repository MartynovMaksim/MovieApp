package ru.androidschool.intensiv.database

import androidx.room.TypeConverter

class GenreIdsConverter {
    companion object {
        @TypeConverter
        @JvmStatic
        fun genreIdsToString(genreIds: MutableList<Int>?): String? =
            genreIds?.map { it }?.joinToString(separator = ",")

        @TypeConverter
        @JvmStatic
        fun stringToGenreIds(genreIds: String?): MutableList<Int>? =
            genreIds?.split(",")?.map { it.toInt() }?.toMutableList()
    }
}