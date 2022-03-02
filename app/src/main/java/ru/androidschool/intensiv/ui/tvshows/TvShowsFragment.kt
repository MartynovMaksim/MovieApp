package ru.androidschool.intensiv.ui.tvshows

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import ru.androidschool.intensiv.R
import ru.androidschool.intensiv.data.MovieMock
import ru.androidschool.intensiv.databinding.TvShowsFragmentBinding
import ru.androidschool.intensiv.ui.feed.FeedFragment

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

//        val tvShowsList = MockRepository.getMovies().map {
//            TvShowItem(it) { tvShow ->
//                openTvShowDetails(tvShow)
//            }
//        }
//        binding.tvShowRecyclerView.adapter =
//            GroupAdapter<GroupieViewHolder>().apply {
//                addAll(tvShowsList)
//            }
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