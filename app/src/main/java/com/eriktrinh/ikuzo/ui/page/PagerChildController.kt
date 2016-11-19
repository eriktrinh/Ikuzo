package com.eriktrinh.ikuzo.ui.page

import android.os.Bundle
import com.bluelinelabs.conductor.Controller
import com.eriktrinh.ikuzo.data.ani.Anime
import com.eriktrinh.ikuzo.data.ani.Record

abstract class PagerChildController(args: Bundle?) : Controller(args) {
    private lateinit var presenter: SeriesPagePresenter

    fun setPresenter(presenter: SeriesPagePresenter) {
        this.presenter = presenter
    }

    fun getPresenter(): SeriesPagePresenter {
        return presenter
    }

    abstract fun onNewItem(series: Anime, status: Record?)
    abstract fun onItemUpdateableChanged(canUpdate: Boolean)
    abstract fun onFavouriteChanged(favourite: Boolean)
}