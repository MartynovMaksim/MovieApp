package ru.androidschool.intensiv.ui.movie_details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.fragment.app.Fragment
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import ru.androidschool.intensiv.MainActivity.Companion.KEY_MOVIE
import ru.androidschool.intensiv.MainActivity.Companion.KEY_TV
import ru.androidschool.intensiv.R
import ru.androidschool.intensiv.data.Movie
import ru.androidschool.intensiv.data.TvShow
import ru.androidschool.intensiv.data.mapper.MovieMapper
import ru.androidschool.intensiv.data.mapper.TvShowMapper
import ru.androidschool.intensiv.database.*
import ru.androidschool.intensiv.databinding.MovieDetailsFragmentBinding
import ru.androidschool.intensiv.utils.loadImageForDetailsFragment
import ru.androidschool.intensiv.utils.setCompletableToDbCall
import ru.androidschool.intensiv.utils.setSchedulersFromIoToMainThread
import timber.log.Timber

class MovieDetailsFragment : Fragment(R.layout.movie_details_fragment) {

    private var _binding: MovieDetailsFragmentBinding? = null
    private val binding get() = requireNotNull(_binding)

    private val disposables = CompositeDisposable()

    private val movieDao: MovieDao by lazy {
        MovieDatabase.get(requireContext()).movieDao()
    }
    private val tvShowDao: TvShowDao by lazy {
        MovieDatabase.get(requireContext()).tvShowDao()
    }

    private val movie: Movie? by lazy {
        arguments?.getParcelable(KEY_MOVIE)
    }
    private val tvShow: TvShow? by lazy {
        arguments?.getParcelable(KEY_TV)
    }

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

        movie?.let { setDetailsForMovie(it) }
        tvShow?.let { setDetailsForTvShow(it) }

        binding.favorite.setOnClickListener {
            if (movie != null) {
                val movieEntity = MovieMapper.toEntity(requireNotNull(movie))
                if (it is CheckBox) {
                    if (it.isChecked) {
                        movieDao.save(movieEntity).setCompletableToDbCall(TAG)
                    } else {
                        movieDao.delete(movieEntity).setCompletableToDbCall(TAG)
                    }
                }
            } else if (tvShow != null) {
                val tvShowEntity = TvShowMapper.toEntity(requireNotNull(tvShow))
                if (it is CheckBox) {
                    if (it.isChecked) {
                        tvShowDao.save(tvShowEntity).setCompletableToDbCall(TAG)
                    } else {
                        tvShowDao.delete(tvShowEntity).setCompletableToDbCall(TAG)
                    }
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

    private fun checkMovieInFavorite(movie: Movie) {
        disposables += movieDao.getMovie(requireNotNull(movie.id))
            .setSchedulersFromIoToMainThread()
            .subscribe({
                binding.favorite.isChecked = true
            }, {
                Timber.tag(TAG).e(it)
            })
    }

    private fun checkTvShowInFavorite(tvShow: TvShow) {
        disposables += tvShowDao.getTvShow(requireNotNull(tvShow.id))
            .setSchedulersFromIoToMainThread()
            .subscribe({
                binding.favorite.isChecked = true
            }, {
                Timber.tag(TAG).e(it)
            })
    }

    override fun onResume() {
        super.onResume()
        movie?.let { checkMovieInFavorite(it) }
        tvShow?.let { checkTvShowInFavorite(it) }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    companion object {
        const val TAG = "MovieDetailsFragment"
    }
}
