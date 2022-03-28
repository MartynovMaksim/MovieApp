package ru.androidschool.intensiv.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

@Dao
interface MovieDao {

    @Insert
    fun save(movieEntity: MovieEntity): Completable

    @Delete
    fun delete(movieEntity: MovieEntity): Completable

    @Query("SELECT * FROM Movies")
    fun getMovies(): Observable<List<MovieEntity>>

    @Query("SELECT * FROM Movies WHERE movieId = :id")
    fun getMovie(id: Int): Single<MovieEntity>
}
