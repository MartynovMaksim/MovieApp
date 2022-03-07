package ru.androidschool.intensiv.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import ru.androidschool.intensiv.databinding.FeedHeaderBinding
import ru.androidschool.intensiv.databinding.FragmentSearchBinding
import ru.androidschool.intensiv.network.MovieApiClient
import ru.androidschool.intensiv.ui.afterTextChanged
import ru.androidschool.intensiv.ui.feed.FeedFragment.Companion.KEY_SEARCH
import ru.androidschool.intensiv.ui.feed.MovieItem
import timber.log.Timber
import java.util.concurrent.TimeUnit

class SearchFragment : Fragment(R.layout.fragment_search) {

    private var _binding: FragmentSearchBinding? = null
    private var _searchBinding: FeedHeaderBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val searchBinding get() = _searchBinding!!

    private val options = navOptions {
        anim {
            enter = R.anim.slide_in_right
            exit = R.anim.slide_out_left
            popEnter = R.anim.slide_in_left
            popExit = R.anim.slide_out_right
        }
    }

    private val disposables: CompositeDisposable = CompositeDisposable()

    private val adapter by lazy {
        GroupAdapter<GroupieViewHolder>()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        _searchBinding = FeedHeaderBinding.bind(binding.root)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeMovieSearching()

        val searchTerm = requireArguments().getString(KEY_SEARCH)
        searchTerm?.let { searchMovie(it) }
        searchBinding.searchToolbar.setText(searchTerm)
    }

    private fun observeMovieSearching() {
        val observableForSearch = Observable.create<String> { emitter ->
            searchBinding.searchToolbar.binding.searchEditText.afterTextChanged {
                Timber.tag(TAG).d(it.toString())
                if (it?.trim().toString().length > MIN_LENGTH) {
                    emitter.onNext(it.toString())
                }
                if (it.isNullOrBlank()) {
                    adapter.clear()
                }
            }
        }
        disposables += observableForSearch
            .subscribeOn(AndroidSchedulers.mainThread())
            .debounce(500, TimeUnit.MILLISECONDS)
            .subscribe({
                searchMovie(it)
            }, {
                Timber.tag(TAG).e(it)
            })
    }

    private fun searchMovie(query: String) {
        disposables += MovieApiClient.apiClient.getSearchResult(MainActivity.API_KEY, query, "ru")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { movieResponse ->
                val movies = movieResponse.results
                val searchedMovies = movies.map { movie ->
                    MovieItem(movie) {
                        openMovieDetails(movie)
                    }
                }
                binding.moviesRecyclerView.adapter = adapter.apply {
                    addAll(searchedMovies)
                }
            }
    }

    private fun openMovieDetails(movie: Movie) {
        val bundle = Bundle()
        bundle.putParcelable(KEY_MOVIE, movie)
        findNavController().navigate(R.id.movie_details_fragment, bundle, options)
    }

    override fun onResume() {
        super.onResume()
        binding.moviesRecyclerView.adapter = adapter
    }

    override fun onDestroyView() {
        disposables.dispose()
        _binding = null
        _searchBinding = null
        super.onDestroyView()
    }

    companion object {
        const val TAG = "SearchFragment"
        const val MIN_LENGTH = 3
        const val KEY_MOVIE = "movie"
    }
}
