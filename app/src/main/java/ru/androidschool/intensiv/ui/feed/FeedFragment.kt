package ru.androidschool.intensiv.ui.feed

import android.os.Bundle
import android.view.*
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import ru.androidschool.intensiv.R
import ru.androidschool.intensiv.data.Movie
import ru.androidschool.intensiv.data.MoviesResponse
import ru.androidschool.intensiv.database.MovieDao
import ru.androidschool.intensiv.database.MovieDatabase
import ru.androidschool.intensiv.databinding.FeedFragmentBinding
import ru.androidschool.intensiv.databinding.FeedHeaderBinding
import ru.androidschool.intensiv.network.MovieApiClient
import ru.androidschool.intensiv.utils.setSchedulersFromIoToMainThread
import ru.androidschool.intensiv.utils.showAndHideView
import timber.log.Timber

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

    private lateinit var movieDao: MovieDao

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

        movieDao = MovieDatabase.get(requireContext()).movieDao()

        observeMovieSearching()

        if (adapter.itemCount == 0) {
            observeShowcases()
        }
    }

    private fun observeMovieSearching() {
        disposables += searchBinding.searchToolbar.doSearch()
            .subscribe({
                openSearch(it)
            }, {
                Timber.tag(TAG).e(it)
            })
    }

    private fun observeShowcases() {

        disposables += Single.zip(
            MovieApiClient.apiClient.getNowPlayingMovies(),
            MovieApiClient.apiClient.getUpcomingMovies(),
            MovieApiClient.apiClient.getPopularMovies()
        ) { nowPlaying, upcoming, popular ->
            hashMapOf(
                (MovieCategories.RECOMMENDED.category to (createListOfMainCardContainer(
                    nowPlaying,
                    R.string.recommended
                ))),
                (MovieCategories.POPULAR.category to (createListOfMainCardContainer(
                    popular, R.string.popular
                ))),
                (MovieCategories.UPCOMING.category to (createListOfMainCardContainer(
                    upcoming, R.string.upcoming
                )))
            )
        }
            .setSchedulersFromIoToMainThread()
            .showAndHideView(binding.progressBar)
            .subscribe({ list ->
                list.forEach {
                    binding.moviesRecyclerView.adapter = adapter.apply { addAll(it.value) }
                }
            }, {
                Timber.tag(TAG).e(it)
            })
    }

    private fun createListOfMainCardContainer(
        moviesResponse: MoviesResponse,
        @StringRes title: Int
    ): List<MainCardContainer> {
        val movies = moviesResponse.results
        return listOf(
            MainCardContainer(
                title,
                movies.map { movie ->
                    MovieItem(movie) {
                        openMovieDetails(movie)
                    }
                }
            )
        )
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
        disposables.clear()
        super.onDestroyView()
    }

    private enum class MovieCategories(val category: String) {
        RECOMMENDED("recommended"),
        POPULAR("popular"),
        UPCOMING("upcoming")
    }


    companion object {
        const val TAG = "FeedFragment"
        const val KEY_SEARCH = "search"
        const val KEY_MOVIE = "movie"
    }
}
