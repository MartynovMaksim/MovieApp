package ru.androidschool.intensiv.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import ru.androidschool.intensiv.BuildConfig

@Parcelize
data class TvShow(
    val popularity: String?,
    val id: Int?,
    @SerializedName("backdrop_path")
    val backdropPath: String?,
    val overview: String?,
    @SerializedName("first_air_date")
    val firstAirDate: String?,
    @SerializedName("origin_country")
    val originCountries: List<String>?,
    @SerializedName("genre_ids")
    val genreIds: List<Int>?,
    @SerializedName("original_language")
    val originalLanguage: String?,
    @SerializedName("vote_count")
    val voteCount: Int?,
    val name: String?,
    @SerializedName("original_name")
    val originalName: String?
) : Parcelable {
    @SerializedName("poster_path")
    var posterPath: String? = null
        get() = """${BuildConfig.IMAGE_BASE_URL}/w500$field"""

    @SerializedName("vote_average")
    var voteAverage: Float? = null
        // Value's range is 0..10. We divide this value by 2 because for AppCompatRatingBar set 5 stars
        get() = field?.div(2) ?: 0F
}
