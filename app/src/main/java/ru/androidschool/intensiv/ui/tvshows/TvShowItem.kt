package ru.androidschool.intensiv.ui.tvshows

import ru.androidschool.intensiv.R
import ru.androidschool.intensiv.data.Movie
import ru.androidschool.intensiv.ui.feed.MovieItem

class TvShowItem(content: Movie, onClick: (movie: Movie) -> Unit) : MovieItem(content, onClick) {

    override fun getLayout(): Int = R.layout.item_with_text_large
}