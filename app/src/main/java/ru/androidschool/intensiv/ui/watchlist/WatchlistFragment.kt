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
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import ru.androidschool.intensiv.MainActivity.Companion.KEY_MOVIE
import ru.androidschool.intensiv.MainActivity.Companion.KEY_TV
import ru.androidschool.intensiv.R
import ru.androidschool.intensiv.data.Movie
import ru.androidschool.intensiv.data.TvShow
import ru.androidschool.intensiv.data.mapper.MovieMapper
import ru.androidschool.intensiv.data.mapper.TvShowMapper
import ru.androidschool.intensiv.database.MovieDatabase
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
    private val tvShowDao by lazy {
        MovieDatabase.get(requireContext()).tvShowDao()
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
        disposables += Single.zip(
            movieDao.getMovies(),
            tvShowDao.getTvShows()
        ) { movies, tvShows ->
            val favoriteMovies = movies.map { movieEntity ->
                val movie = MovieMapper.toData(movieEntity)
                MoviePreviewItem(movie) {
                    openMovieDetails(movie)
                }
            }
            val favoriteTvShows = tvShows.map { tvShowEntity ->
                val tvShow = TvShowMapper.toData(tvShowEntity)
                TvShowPreviewItem(tvShow) {
                    openTvShowDetails(tvShow)
                }
            }
            listOf(favoriteMovies, favoriteTvShows)
        }.setSchedulersFromIoToMainThread()
            .doOnSubscribe {
                binding.progressBar.visibility = View.VISIBLE
            }
            .subscribe({ list ->
                adapter.clear()

                binding.progressBar.visibility = View.GONE

                list.forEach {
                    binding.moviesRecyclerView.adapter = adapter.apply {
                        addAll(it)
                    }
                }
            }, {
                Timber.tag(TAG).e(it)
            })
    }

    private fun openMovieDetails(movie: Movie) {
        val bundle = Bundle()
        bundle.putParcelable(KEY_MOVIE, movie)
        findNavController().navigate(R.id.movie_details_fragment, bundle, options)
    }

    private fun openTvShowDetails(tvShow: TvShow) {
        val bundle = Bundle()
        bundle.putParcelable(KEY_TV, tvShow)
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
    }
}
