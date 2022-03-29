package ru.androidschool.intensiv.database.converters

import androidx.room.TypeConverter

class OriginCountriesConverter {
    companion object {
        @TypeConverter
        @JvmStatic
        fun originCountriesToString(originCountries: MutableList<String>?): String? =
            originCountries?.joinToString(",") { it }

        @TypeConverter
        @JvmStatic
        fun stringToOriginCountries(originCountries: String?): MutableList<String>? =
            originCountries?.split(",")?.toMutableList()
    }
}