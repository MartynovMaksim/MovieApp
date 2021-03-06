package ru.androidschool.intensiv.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import ru.androidschool.intensiv.BuildConfig

@Parcelize
data class Movie(
    @SerializedName("adult")
    val isAdult: Boolean?,
    @SerializedName("backdrop_path")
    val backdropPath: String?,
    @SerializedName("genre_ids")
    val genreIds: List<Int>?,
    val id: Int?,
    @SerializedName("original_language")
    val originalLanguage: String?,
    @SerializedName("original_title")
    val originalTitle: String?,
    val overview: String?,
    val popularity: Double?,
    @SerializedName("release_date")
    val releaseDate: String?,
    val title: String?,
    val video: Boolean?,
    @SerializedName("vote_count")
    val voteCount: Int?
) : Parcelable {
    @SerializedName("poster_path")
    var posterPath: String? = null
        get() = """${BuildConfig.IMAGE_BASE_URL}/w500$field"""

    @SerializedName("vote_average")
    var voteAverage: Float? = null
        // Value's range is 0..10. We divide this value by 2 because for AppCompatRatingBar set 5 stars
        get() = field?.div(2) ?: 0F
}
