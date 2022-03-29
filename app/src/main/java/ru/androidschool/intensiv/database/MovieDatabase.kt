package ru.androidschool.intensiv.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.androidschool.intensiv.database.converters.GenreIdsConverter
import ru.androidschool.intensiv.database.converters.OriginCountriesConverter

@Database(entities = [MovieEntity::class, TvShowEntity::class], version = 1)
@TypeConverters(GenreIdsConverter::class, OriginCountriesConverter::class)
abstract class MovieDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao
    abstract fun tvShowDao(): TvShowDao

    companion object {
        private var instance: MovieDatabase? = null

        @Synchronized
        fun get(context: Context): MovieDatabase {
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    MovieDatabase::class.java,
                    "MovieDatabase"
                )
                    .build()
            }
            return requireNotNull(instance)
        }
    }
}
