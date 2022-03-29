package ru.androidschool.intensiv.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

@Dao
interface TvShowDao {

    @Insert
    fun save(tvShowEntity: TvShowEntity): Completable

    @Delete
    fun delete(tvShowEntity: TvShowEntity): Completable

    @Query("SELECT * FROM TvShows")
    fun getTvShows(): Observable<List<TvShowEntity>>

    @Query("SELECT * FROM TvShows WHERE tvShowId = :id")
    fun getTvShow(id: Int): Single<TvShowEntity>
}