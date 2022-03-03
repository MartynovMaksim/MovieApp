package ru.androidschool.intensiv.ui.tvshows

import android.view.View
import com.squareup.picasso.Picasso
import com.xwray.groupie.viewbinding.BindableItem
import ru.androidschool.intensiv.R
import ru.androidschool.intensiv.data.TvShow
import ru.androidschool.intensiv.databinding.ItemWithTextBinding

class TvShowItem(
    private val content: TvShow,
    private val onClick: (tvShow: TvShow) -> Unit
) : BindableItem<ItemWithTextBinding>() {

    override fun getLayout(): Int = R.layout.item_with_text_large

    override fun bind(view: ItemWithTextBinding, position: Int) {
        view.description.text = content.name
        view.movieRating.rating = content.voteAverage?.div(2) ?: 0F
        view.content.setOnClickListener {
            onClick.invoke(content)
        }

        Picasso.get()
            .load(content.posterPath)
            .fit()
            .into(view.imagePreview)
    }

    override fun initializeViewBinding(v: View) = ItemWithTextBinding.bind(v)
}