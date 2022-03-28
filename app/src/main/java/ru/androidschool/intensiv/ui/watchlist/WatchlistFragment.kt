package ru.androidschool.intensiv.ui.watchlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import ru.androidschool.intensiv.R
import ru.androidschool.intensiv.data.Movie
import ru.androidschool.intensiv.database.MovieDatabase
import ru.androidschool.intensiv.database.MovieEntity
import ru.androidschool.intensiv.databinding.FragmentWatchlistBinding
import ru.androidschool.intensiv.utils.setSchedulersFromIoToMainThread
import timber.log.Timber

class WatchlistFragment : Fragment() {

    private var _binding: FragmentWatchlistBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val adapter by lazy {
        GroupAdapter<GroupieViewHolder>()
    }

    private val movieDao by lazy {
        MovieDatabase.get(requireContext()).movieDao()
    }

    private val disposables = CompositeDisposable()

    private val options = navOptions {
        anim {
            enter = R.anim.slide_in_right
            exit = R.anim.slide_out_left
            popEnter = R.anim.slide_in_left
            popExit = R.anim.slide_out_right
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWatchlistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setFavoriteMoviesToWatchList()
    }

    private fun setFavoriteMoviesToWatchList() {
        disposables += movieDao.getMovies()
            .setSchedulersFromIoToMainThread()
            .doOnSubscribe {
                binding.progressBar.visibility = View.VISIBLE
            }
            .subscribe({
                val favoriteMovies = it.map { movieEntity ->
                    val movie = convertDbEntityToDto(movieEntity)
                    MoviePreviewItem(movie) {
                        openMovieDetails(movie)
                    }
                }
                binding.moviesRecyclerView.adapter = adapter.apply {
                    binding.progressBar.visibility = View.GONE
                    addAll(favoriteMovies)
                }
            }, {
                Timber.tag(TAG).e(it)
            })
    }

    private fun convertDbEntityToDto(entity: MovieEntity) = Movie(
        entity.isAdult,
        entity.backdropPath,
        entity.genreIds,
        entity.movieId,
        entity.originalLanguage,
        entity.originalTitle,
        entity.overview,
        entity.popularity,
        entity.releaseDate,
        entity.title,
        entity.video,
        entity.voteCount
    ).apply {
        posterPath = entity.posterPath
        voteAverage = entity.voteAverage?.times(2F)
    }

    private fun openMovieDetails(movie: Movie) {
        val bundle = Bundle()
        bundle.putParcelable(KEY_MOVIE, movie)
        findNavController().navigate(R.id.movie_details_fragment, bundle, options)
    }

    override fun onDestroyView() {
        _binding = null
        disposables.clear()
        super.onDestroyView()
    }

    companion object {
        @JvmStatic
        fun newInstance() = WatchlistFragment()
        const val TAG = "WatchlistFragment"
        private const val KEY_MOVIE = "movie"
    }
}
