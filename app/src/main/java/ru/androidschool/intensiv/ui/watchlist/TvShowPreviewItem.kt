package ru.androidschool.intensiv.ui.watchlist

import android.view.View
import com.xwray.groupie.viewbinding.BindableItem
import ru.androidschool.intensiv.R
import ru.androidschool.intensiv.data.TvShow
import ru.androidschool.intensiv.databinding.ItemSmallBinding
import ru.androidschool.intensiv.utils.loadImage

class TvShowPreviewItem(
    private val content: TvShow,
    private val onClick: (tvShow: TvShow) -> Unit
) : BindableItem<ItemSmallBinding>() {

    override fun getLayout() = R.layout.item_small

    override fun bind(view: ItemSmallBinding, position: Int) {
        view.imagePreview.apply {
            loadImage(content.posterPath)
            setOnClickListener {
                onClick.invoke(content)
            }
        }
    }

    override fun initializeViewBinding(v: View): ItemSmallBinding = ItemSmallBinding.bind(v)
}
