package ru.androidschool.intensiv.ui.movie_details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.fragment.app.Fragment
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import ru.androidschool.intensiv.R
import ru.androidschool.intensiv.data.Movie
import ru.androidschool.intensiv.data.TvShow
import ru.androidschool.intensiv.database.MovieDao
import ru.androidschool.intensiv.database.MovieDatabase
import ru.androidschool.intensiv.database.MovieEntity
import ru.androidschool.intensiv.databinding.MovieDetailsFragmentBinding
import ru.androidschool.intensiv.utils.loadImageForDetailsFragment
import ru.androidschool.intensiv.utils.setCompletableToDbCall
import ru.androidschool.intensiv.utils.setSchedulersFromIoToMainThread
import timber.log.Timber

class MovieDetailsFragment : Fragment(R.layout.movie_details_fragment) {

    private var _binding: MovieDetailsFragmentBinding? = null
    private val binding get() = requireNotNull(_binding)

    private val disposables = CompositeDisposable()

    private lateinit var movieDao: MovieDao

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MovieDetailsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val movie = arguments?.getParcelable<Movie>("movie")
        val tvShow = arguments?.getParcelable<TvShow>("tv_show")
        movieDao = MovieDatabase.get(requireContext()).movieDao()

        movie?.let { setDetailsForMovie(it) }
        tvShow?.let { setDetailsForTvShow(it) }

        binding.favorite.setOnClickListener { view ->
            val movieEntity = convertMovieToDbEntity(requireNotNull(movie))
            if (view is CheckBox) {
                if (view.isChecked) {
                    movieDao.save(movieEntity).setCompletableToDbCall(TAG)
                } else {
                    movieDao.delete(movieEntity).setCompletableToDbCall(TAG)
                }
            }
        }
        binding.backButton.setOnClickListener { parentFragmentManager.popBackStack() }
    }

    private fun setDetailsForMovie(movie: Movie) {
        with(movie) {
            binding.movieRating.rating = voteAverage ?: 0F
            binding.detailsTitle.text = title
            binding.overview.text = overview
            binding.detailsImage.loadImageForDetailsFragment(posterPath)
        }
    }

    private fun setDetailsForTvShow(tvShow: TvShow) {
        with(tvShow) {
            binding.movieRating.rating = voteAverage ?: 0F
            binding.detailsTitle.text = name
            binding.overview.text = overview
            binding.detailsImage.loadImageForDetailsFragment(posterPath)
        }
    }

    private fun convertMovieToDbEntity(movieDto: Movie): MovieEntity =
        MovieEntity(
            movieId = movieDto.id,
            title = movieDto.title,
            posterPath = movieDto.posterPath
        )

    private fun checkMovieInFavorite() {
        disposables += movieDao.getMovie(requireNotNull(arguments?.getParcelable<Movie>("movie")?.id))
            .setSchedulersFromIoToMainThread()
            .subscribe({
                binding.favorite.isChecked = true
            }, {
                Timber.tag(TAG).e(it)
            })
    }

    override fun onResume() {
        super.onResume()
        checkMovieInFavorite()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    companion object {
        const val TAG = "MovieDetailsFragment"
    }
}
