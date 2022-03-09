package ru.androidschool.intensiv.utils

import android.widget.ImageView
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation

fun ImageView.loadImage(url: String?) {
    Picasso.get()
        .load(url)
        .fit()
        .into(this)
}

fun ImageView.loadImageForDetailsFragment(url: String?) {
    Picasso.get()
        .load(url)
        .transform(
            RoundedCornersTransformation(
                24,
                0,
                RoundedCornersTransformation.CornerType.BOTTOM
            )
        )
        .fit()
        .centerCrop()
        .into(this)
}