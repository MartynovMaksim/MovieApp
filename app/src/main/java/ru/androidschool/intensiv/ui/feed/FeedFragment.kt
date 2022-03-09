package ru.androidschool.intensiv.ui.feed

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.schedulers.Schedulers
import ru.androidschool.intensiv.MainActivity
import ru.androidschool.intensiv.R
import ru.androidschool.intensiv.data.Movie
import ru.androidschool.intensiv.databinding.FeedFragmentBinding
import ru.androidschool.intensiv.databinding.FeedHeaderBinding
import ru.androidschool.intensiv.network.MovieApiClient
import ru.androidschool.intensiv.ui.afterTextChanged
import timber.log.Timber
import java.util.concurrent.TimeUnit

class FeedFragment : Fragment(R.layout.feed_fragment) {

    private var _binding: FeedFragmentBinding? = null
    private var _searchBinding: FeedHeaderBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val searchBinding get() = _searchBinding!!

    private val adapter by lazy {
        GroupAdapter<GroupieViewHolder>()
    }

    private val options = navOptions {
        anim {
            enter = R.anim.slide_in_right
            exit = R.anim.slide_out_left
            popEnter = R.anim.slide_in_left
            popExit = R.anim.slide_out_right
        }
    }

    private val disposables: CompositeDisposable = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FeedFragmentBinding.inflate(inflater, container, false)
        _searchBinding = FeedHeaderBinding.bind(binding.root)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeMovieSearching()

        showNowPlayingMovies()
        showUpcomingMovies()
        showPopularMovies()
    }

    private fun showNowPlayingMovies() {
        disposables += MovieApiClient.apiClient.getNowPlayingMovies()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ movieResponse ->
                val movies = movieResponse.results
                val nowPlayingMovie = listOf(
                    MainCardContainer(
                        R.string.recommended,
                        movies.map { movie ->
                            MovieItem(movie) {
                                openMovieDetails(movie)
                            }
                        }
                    )
                )
                binding.moviesRecyclerView.adapter = adapter.apply { addAll(nowPlayingMovie) }
            }, {
                Timber.tag(TAG).e(it)
            })
    }

    private fun showUpcomingMovies() {
        disposables += MovieApiClient.apiClient.getUpcomingMovies()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ movieResponse ->
                val movies = movieResponse.results
                val upcomingMovies = listOf(
                    MainCardContainer(
                        R.string.upcoming,
                        movies.map { movie ->
                            MovieItem(movie) {
                                openMovieDetails(movie)
                            }
                        }
                    )
                )
                binding.moviesRecyclerView.adapter = adapter.apply { addAll(upcomingMovies) }
            }, {
                Timber.tag(TAG).e(it)
            })
    }

    private fun showPopularMovies() {
        disposables += MovieApiClient.apiClient.getPopularMovies()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ movieResponse ->
                val movies = movieResponse.results
                val popularMovies = listOf(
                    MainCardContainer(
                        R.string.popular,
                        movies.map { movie ->
                            MovieItem(movie) {
                                openMovieDetails(movie)
                            }
                        }
                    )
                )
                binding.moviesRecyclerView.adapter = adapter.apply { addAll(popularMovies) }
            }, {
                Timber.tag(TAG).e(it)
            })
    }

    private fun observeMovieSearching() {
        val observableForSearch = Observable.create<String> { emitter ->
            searchBinding.searchToolbar.binding.searchEditText.afterTextChanged {
                Timber.tag(TAG).d(it.toString())
                if (it?.trim().toString().length > MIN_LENGTH) {
                    emitter.onNext(it.toString())
                }
            }
        }
        disposables += observableForSearch
            .subscribeOn(AndroidSchedulers.mainThread())
            .debounce(500, TimeUnit.MILLISECONDS)
            .subscribe({
                openSearch(it)
            }, {
                Timber.tag(TAG).e(it)
            })
    }

    private fun openMovieDetails(movie: Movie) {
        val bundle = Bundle()
        bundle.putParcelable(KEY_MOVIE, movie)
        findNavController().navigate(R.id.movie_details_fragment, bundle, options)
    }

    private fun openSearch(searchText: String) {
        val bundle = Bundle()
        bundle.putString(KEY_SEARCH, searchText)
        findNavController().navigate(R.id.search_dest, bundle, options)
    }

    override fun onStop() {
        super.onStop()
        searchBinding.searchToolbar.clear()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
    }

    override fun onResume() {
        super.onResume()
        binding.moviesRecyclerView.adapter = adapter
    }

    override fun onDestroyView() {
        _binding = null
        _searchBinding = null
        disposables.dispose()
        super.onDestroyView()
    }

    companion object {
        const val TAG = "FeedFragment"
        const val MIN_LENGTH = 3
        const val KEY_SEARCH = "search"
        const val KEY_MOVIE = "movie"
    }
}
