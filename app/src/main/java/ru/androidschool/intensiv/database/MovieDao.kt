package ru.androidschool.intensiv.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import io.reactivex.Completable
import io.reactivex.Observable

@Dao
interface MovieDao {

    @Insert
    fun save(movieEntity: List<MovieEntity>): Completable

    @Delete
    fun delete(movieEntity: MovieEntity): Completable

    @Query("SELECT * FROM Movies")
    fun getMovies(): Observable<List<MovieEntity>>
}
