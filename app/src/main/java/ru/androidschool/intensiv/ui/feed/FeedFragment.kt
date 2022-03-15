package ru.androidschool.intensiv.ui.feed

import android.os.Bundle
import android.view.*
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import ru.androidschool.intensiv.R
import ru.androidschool.intensiv.data.Movie
import ru.androidschool.intensiv.data.MoviesResponse
import ru.androidschool.intensiv.databinding.FeedFragmentBinding
import ru.androidschool.intensiv.databinding.FeedHeaderBinding
import ru.androidschool.intensiv.network.MovieApiClient
import ru.androidschool.intensiv.utils.setSchedulersForShowcaseRequest
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

        if (adapter.itemCount == 0) {
            showNowPlayingMovies()
            showUpcomingMovies()
            showPopularMovies()
        }
    }

    private fun showNowPlayingMovies() {
        disposables += MovieApiClient.apiClient.getNowPlayingMovies()
            .setSchedulersForShowcaseRequest()
            .subscribe({ moviesResponse ->
                addMoviesResponseToAdapter(moviesResponse, R.string.recommended)
            }, {
                Timber.tag(TAG).e(it)
            })
    }

    private fun showUpcomingMovies() {
        disposables += MovieApiClient.apiClient.getUpcomingMovies()
            .setSchedulersForShowcaseRequest()
            .subscribe({ moviesResponse ->
                addMoviesResponseToAdapter(moviesResponse, R.string.upcoming)
            }, {
                Timber.tag(TAG).e(it)
            })
    }

    private fun showPopularMovies() {
        disposables += MovieApiClient.apiClient.getPopularMovies()
            .setSchedulersForShowcaseRequest()
            .subscribe({ moviesResponse ->
                addMoviesResponseToAdapter(moviesResponse, R.string.popular)
            }, {
                Timber.tag(TAG).e(it)
            })
    }

    private fun observeMovieSearching() {
        disposables += searchBinding.searchToolbar.doSearch()
            .subscribe({
                openSearch(it)
            }, {
                Timber.tag(TAG).e(it)
            })
    }

    private fun addMoviesResponseToAdapter(moviesResponse: MoviesResponse, @StringRes title: Int) {
        val movies = moviesResponse.results
        val popularMovies = listOf(
            MainCardContainer(
                title,
                movies.map { movie ->
                    MovieItem(movie) {
                        openMovieDetails(movie)
                    }
                }
            )
        )
        binding.moviesRecyclerView.adapter = adapter.apply { addAll(popularMovies) }
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

    companion object {
        const val TAG = "FeedFragment"
        const val KEY_SEARCH = "search"
        const val KEY_MOVIE = "movie"
    }
}
