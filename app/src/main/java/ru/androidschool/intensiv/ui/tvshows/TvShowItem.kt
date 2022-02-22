package ru.androidschool.intensiv.ui.tvshows

import android.view.View
import com.squareup.picasso.Picasso
import com.xwray.groupie.viewbinding.BindableItem
import ru.androidschool.intensiv.R
import ru.androidschool.intensiv.data.Movie
import ru.androidschool.intensiv.databinding.ItemWithTextLargeBinding

class TvShowItem(
    private val content: Movie,
    private val onClick: (movie: Movie) -> Unit
) : BindableItem<ItemWithTextLargeBinding>() {

    override fun bind(view: ItemWithTextLargeBinding, position: Int) {
        view.description.text = content.title
        view.movieRating.rating = content.rating
        view.content.setOnClickListener {
            onClick.invoke(content)
        }
        Picasso.get()
            .load("https://m.media-amazon.com/images/M/MV5BYTk3MDljOWQtNGI2My00OTEzLTlhYjQtOTQ4ODM2MzUwY2IwXkEyXkFqcGdeQXVyNTIzOTk5ODM@._V1_.jpg")
            .into(view.imagePreview)
    }

    override fun getLayout(): Int = R.layout.item_with_text_large

    override fun initializeViewBinding(v: View): ItemWithTextLargeBinding =
        ItemWithTextLargeBinding.bind(v)
}