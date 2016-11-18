package com.eriktrinh.ikuzo.ui.browse

import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.*
import com.bluelinelabs.conductor.Controller
import com.eriktrinh.ikuzo.R
import com.eriktrinh.ikuzo.data.ani.Airing
import com.eriktrinh.ikuzo.data.ani.Anime
import com.eriktrinh.ikuzo.ui.SpacingItemDecoration
import com.eriktrinh.ikuzo.ui.page.SeriesPageActivity
import com.eriktrinh.ikuzo.utils.CalendarUtils
import com.eriktrinh.ikuzo.utils.ext.loadAndCropInto
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.controller_series_view.view.*
import kotlinx.android.synthetic.main.list_item_series_view.view.*

class SeriesController : Controller(), BrowseDialogFragment.Delegate {

    companion object {
        private val TAG = "SeriesController"
        private val BROWSER = "SeriesController.BrowseDialog"
    }

    private lateinit var adapter: SeriesAdapter
    private lateinit var presenter: SeriesPresenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        val view = inflater.inflate(R.layout.controller_series_view, container, false)

        setHasOptionsMenu(true)

        val recyclerView = view.controller_series_recycler_view
        val layoutManager = LinearLayoutManager(activity)
        recyclerView.layoutManager = layoutManager

        adapter = SeriesAdapter(emptyList())
        recyclerView.adapter = adapter
        recyclerView.addItemDecoration(SpacingItemDecoration(resources.getDimensionPixelSize(R.dimen.design_card_margin)))

        presenter = SeriesPresenter(activity)
                .takeController(this)
        return view
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDestroy()
    }

    fun onItemsNext(items: List<Anime>) {
        adapter.addItems(items)
    }

    inner private class SeriesHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        private val imageView = itemView.series_image
        private val englishTitleText = itemView.series_english_name
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
            englishTitleText.text = anime.titleEnglish
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
            for (i in 0..items.size - 1) {
                Picasso.with(activity)
                        .load(items[i].imageUrl)
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

    private fun Airing.formatCountdown(): String {
        val minutes = (countdown / 60) % 60
        val hours = (countdown / (60 * 60)) % 24
        val days = (countdown / (60 * 60)) / 24
        return "Ep $nextEpisode in" +
                (if (days != 0) " ${days}d" else "") +
                (if (hours != 0) " ${hours}h" else "") +
                " ${minutes}m"
    }

    private fun Anime.formatLeftText(): String {
        return "$type ($totalEpisodes eps)"
    }

    private fun Anime.formatCenterText(): String {
        return "$averageScore%"
    }

    private fun Anime.formatRightText(): String {
        return "$popularity"
    }

    private fun Anime.formatStatusText(): String {
        val dateString = if (startDate == null) "" else {
            val startYear = startDate / 10000
            val startMonth = CalendarUtils.monthToShortForm[(startDate / 100) % 100]
            " ($startMonth $startYear - ${
            if (endDate != null) "${CalendarUtils.monthToShortForm[(endDate / 100) % 100]} ${endDate / 10000}" else ""
            })"
        }
        return "$airingStatus$dateString"
    }
}
