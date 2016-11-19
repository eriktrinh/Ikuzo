package com.eriktrinh.ikuzo.ui.page

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.eriktrinh.ikuzo.R
import com.eriktrinh.ikuzo.data.ani.Anime
import com.eriktrinh.ikuzo.data.ani.Record
import com.eriktrinh.ikuzo.ui.SpacingItemDecoration
import kotlinx.android.synthetic.main.controller_series_reviews.view.*

/**
 *  TODO:
 *  - 'Load More' button at the bottom of the list
 *  - FAB to write a review (implemented later)
 *  - click on reviewers profile, vote on review (later)
 */
class SeriesReviewController(args: Bundle?) : PagerChildController(args) {

    companion object {
        private val TAG = "SeriesReviewController"
        private val KEY_ID = "ARGS_ID"
        private fun Bundle.putId(id: Int): Bundle {
            this.putInt(KEY_ID, id)
            return this
        }
    }

    constructor(id: Int) : this(Bundle().putId(id))

    private val id: Int
    private lateinit var reviewAdapter: ReviewAdapter

    init {
        id = args?.getInt(KEY_ID, -1) ?: -1
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        val view = inflater.inflate(R.layout.controller_series_reviews, container, false)

        val recyclerView = view.series_reviews_recycler
        val layoutManager = LinearLayoutManager(activity)
        reviewAdapter = ReviewAdapter(activity, emptyList())
        recyclerView.adapter = reviewAdapter
        recyclerView.layoutManager = layoutManager
        recyclerView.addItemDecoration(SpacingItemDecoration(
                resources.getDimensionPixelSize(R.dimen.design_card_margin)
        ))

        getPresenter().publish(this)

        return view
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onNewItem(series: Anime, status: Record?) {
        if (isAttached) {
            reviewAdapter.clearItems()
            reviewAdapter.addItems(series.reviews ?: emptyList())
        }
    }

    override fun onItemUpdateableChanged(canUpdate: Boolean) {
    }

    override fun onFavouriteChanged(favourite: Boolean) {
    }
}