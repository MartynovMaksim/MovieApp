package ru.androidschool.intensiv.ui.movie_details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.androidschool.intensiv.R
import ru.androidschool.intensiv.data.Movie
import ru.androidschool.intensiv.data.TvShow
import ru.androidschool.intensiv.databinding.MovieDetailsFragmentBinding
import ru.androidschool.intensiv.utils.loadImageForDetailsFragment

class MovieDetailsFragment : Fragment(R.layout.movie_details_fragment) {

    private var _binding: MovieDetailsFragmentBinding? = null
    private val binding get() = requireNotNull(_binding)

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

        movie?.let { setDetailsForMovie(it) }
        tvShow?.let { setDetailsForTvShow(it) }
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
