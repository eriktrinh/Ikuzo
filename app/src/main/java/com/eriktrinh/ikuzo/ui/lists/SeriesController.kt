package com.eriktrinh.ikuzo.ui.lists

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bluelinelabs.conductor.Controller
import com.eriktrinh.ikuzo.R
import com.eriktrinh.ikuzo.data.ani.Anime
import com.eriktrinh.ikuzo.ui.SpacingItemDecoration
import com.eriktrinh.ikuzo.ui.page.SeriesPageActivity
import com.eriktrinh.ikuzo.utils.ext.loadAndCropInto
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.controller_series_view.view.*
import kotlinx.android.synthetic.main.list_item_series_view.view.*


abstract class SeriesController : Controller() {

    companion object {
        private val TAG = "SeriesController"
    }

    private lateinit var adapter: SeriesAdapter
    protected lateinit var presenter: SeriesPresenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        val view = inflater.inflate(R.layout.controller_series_view, container, false)

        val recyclerView = view.controller_series_recycler_view
        val layoutManager = LinearLayoutManager(activity)
        recyclerView.layoutManager = layoutManager

        adapter = SeriesAdapter(emptyList())
        recyclerView.adapter = adapter
        recyclerView.addItemDecoration(SpacingItemDecoration(resources.getDimensionPixelSize(R.dimen.design_card_margin)))

        return view
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDestroy()
    }

    fun onNewItems(items: List<Anime>) {
        adapter.clearItems()
        adapter.addItems(items)
    }

    fun onNextItems(items: List<Anime>) {
        adapter.addItems(items)
    }

    inner private class SeriesHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        private val imageView = itemView.series_image
        private val titleText = itemView.series_name
        private val statusText = itemView.series_status
        private val topLeftText = itemView.series_left_text
        private val topCenterText = itemView.series_center_text
        private val topRightText = itemView.series_right_text
        private val countdownText = itemView.series_countdown
        private lateinit var anime: Anime

        init {
            itemView.setOnClickListener(this)
        }

        fun bindItem(anime: Anime) {
            this.anime = anime
            Picasso.with(activity)
                    .loadAndCropInto(anime.imageUrl, imageView)
            titleText.text = anime.titleRomaji
            statusText.text = anime.formatStatusText()
            topLeftText.text = anime.formatLeftText()
            topCenterText.text = anime.formatCenterText()
            topRightText.text = anime.formatRightText()
            countdownText.text = anime.airing?.formatCountdown() ?: ""
        }

        override fun onClick(view: View) {
            startActivity(SeriesPageActivity.newIntent(activity, anime.id))
            Log.i(TAG, "$anime.titleEnglish} clicked")
        }
    }

    inner private class SeriesAdapter(var series: List<Anime>) : RecyclerView.Adapter<SeriesHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SeriesHolder {
            val inflater = LayoutInflater.from(activity)
            val view = inflater.inflate(R.layout.list_item_series_view, parent, false)

            return SeriesHolder(view)
        }

        override fun onBindViewHolder(holder: SeriesHolder, position: Int) {
            holder.bindItem(series[position])
        }

        override fun getItemCount(): Int {
            return series.size
        }

        fun addItems(items: List<Anime>) {
            val oldSize = series.size
            series = series.plus(items)
            items.forEach {
                Picasso.with(activity)
                        .load(it.imageUrl)
                        .fetch()
            }
            notifyItemRangeInserted(oldSize, items.size)
        }

        fun clearItems() {
            val oldSize = series.size
            notifyItemRangeRemoved(0, oldSize)
            series = emptyList()
        }
    }
}
