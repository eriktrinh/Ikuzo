package com.eriktrinh.ikuzo.ext

import android.widget.ImageView
import com.squareup.picasso.Picasso

fun Picasso.loadInto(url: String?, target: ImageView) {
    this.load(url)
            .into(target)
}

fun Picasso.loadAndCropInto(url: String?, target: ImageView) {
    this.load(url)
            .fit()
            .centerCrop()
            .into(target)
}

fun Picasso.loadAndCenterInto(url: String?, target: ImageView) {
    this.load(url)
            .fit()
            .centerInside()
            .into(target)
}