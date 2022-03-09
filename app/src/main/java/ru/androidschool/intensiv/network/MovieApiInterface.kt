package ru.androidschool.intensiv.network

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query
import ru.androidschool.intensiv.MainActivity
import ru.androidschool.intensiv.data.MoviesResponse
import ru.androidschool.intensiv.data.TvShowsResponse

interface MovieApiInterface {
    @GET("movie/now_playing")
    fun getNowPlayingMovies(
        @Query("api_key") apiKey: String = MainActivity.API_KEY,
        @Query("language") language: String = "ru",
        @Query("page") page: Int = 1
    ): Single<MoviesResponse>

    @GET("movie/upcoming")
    fun getUpcomingMovies(
        @Query("api_key") apiKey: String = MainActivity.API_KEY,
        @Query("language") language: String = "ru",
        @Query("page") page: Int = 1
    ): Single<MoviesResponse>

    @GET("movie/popular")
    fun getPopularMovies(
        @Query("api_key") apiKey: String = MainActivity.API_KEY,
        @Query("language") language: String = "ru",
        @Query("page") page: Int = 1
    ): Single<MoviesResponse>

    @GET("tv/popular")
    fun getPopularTvShow(
        @Query("api_key") apiKey: String = MainActivity.API_KEY,
        @Query("language") language: String = "ru",
        @Query("page") page: Int = 1
    ): Single<TvShowsResponse>

    @GET("search/movie")
    fun getSearchResult(
        @Query("api_key") apiKey: String = MainActivity.API_KEY,
        @Query("query") query: String,
        @Query("language") language: String = "ru"
    ): Single<MoviesResponse>
}
