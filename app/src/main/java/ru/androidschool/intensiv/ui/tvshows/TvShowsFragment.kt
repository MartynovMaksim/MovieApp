package ru.androidschool.intensiv.ui.tvshows

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.androidschool.intensiv.MainActivity
import ru.androidschool.intensiv.R
import ru.androidschool.intensiv.data.MovieMock
import ru.androidschool.intensiv.data.TvShowsResponse
import ru.androidschool.intensiv.databinding.TvShowsFragmentBinding
import ru.androidschool.intensiv.network.MovieApiClient
import ru.androidschool.intensiv.ui.feed.FeedFragment
import timber.log.Timber

class TvShowsFragment : Fragment(R.layout.tv_shows_fragment) {

    private var _binding: TvShowsFragmentBinding? = null
    private val binding get() = requireNotNull(_binding)

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
        _binding = TvShowsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showPopularTvShows()
    }

    private fun showPopularTvShows() {
        val popularTvShowsCall =
            MovieApiClient.apiClient.getPopularTvShow(MainActivity.API_KEY, "ru", 1)
        popularTvShowsCall.enqueue(object : Callback<TvShowsResponse> {
            override fun onResponse(
                call: Call<TvShowsResponse>,
                response: Response<TvShowsResponse>
            ) {
                val tvShows = response.body()?.results
                val popularTvShows = tvShows?.let {
                    it.map { tvShow ->
                        TvShowItem(tvShow) {}
                    }
                }
                binding.tvShowRecyclerView.adapter = GroupAdapter<GroupieViewHolder>().apply {
                    if (popularTvShows != null) {
                        addAll(popularTvShows)
                    }
                }
            }

            override fun onFailure(call: Call<TvShowsResponse>, error: Throwable) {
                Timber.e(error)
            }

        })
    }

    private fun openTvShowDetails(movieMock: MovieMock) {
        val bundle = Bundle()
        bundle.putSerializable(FeedFragment.KEY_MOVIE, movieMock)
        findNavController().navigate(R.id.movie_details_fragment, bundle, options)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}