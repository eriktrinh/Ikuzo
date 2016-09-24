package com.eriktrinh.ikuzo.ext

import android.widget.ImageView
import com.squareup.picasso.Picasso

fun Picasso.loadInto(url: String?, target: ImageView) {
    this.load(url)
            .noFade()
            .into(target)
}
