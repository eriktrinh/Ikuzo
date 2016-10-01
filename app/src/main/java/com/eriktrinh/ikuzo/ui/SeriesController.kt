package com.eriktrinh.ikuzo.ui

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bluelinelabs.conductor.Controller
import com.eriktrinh.ikuzo.R
import com.eriktrinh.ikuzo.SeriesLab
import com.eriktrinh.ikuzo.domain.Anime
import com.eriktrinh.ikuzo.ext.loadInto
import com.eriktrinh.ikuzo.web.SeriesService
import com.eriktrinh.ikuzo.web.ServiceGenerator
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.controller_series_view.view.*
import kotlinx.android.synthetic.main.list_item_series_view.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SeriesController : Controller() {
    companion object {
        private val TAG = "SeriesController"
    }

    private lateinit var adapter: SeriesAdapter
    private lateinit var seriesService: SeriesService

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        val view = inflater.inflate(R.layout.controller_series_view, container, false)

        val recyclerView = view.controller_series_recycler_view
        val layoutManager = LinearLayoutManager(activity)
        recyclerView.layoutManager = layoutManager

        adapter = SeriesAdapter(emptyList())
        recyclerView.adapter = adapter
        recyclerView.addItemDecoration(SpacingItemDecoration(resources.getDimensionPixelSize(R.dimen.design_card_margin)))

        seriesService = ServiceGenerator.createService(SeriesService::class.java, activity)
        val call = seriesService.browseAnime()
        call.enqueue(object : Callback<List<Anime>> {
            override fun onFailure(call: Call<List<Anime>>, t: Throwable?) {
                throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onResponse(call: Call<List<Anime>>, response: Response<List<Anime>>?) {
                when (response?.raw()?.code() ?: 400) {
                    200 -> {
                        val series: List<Anime> = response?.body() ?: emptyList()
                        series.forEach { SeriesLab.put(it) }
                        adapter.addItems(series)
                    }
                }
            }
        })
        return view
    }

    inner private class SeriesHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        private val imageView = itemView.series_image
        private val englishTitleText = itemView.series_english_name
        private val statusText = itemView.series_status
        private lateinit var anime: Anime

        init {
            itemView.setOnClickListener(this)
        }

        fun bindItem(anime: Anime) {
            this.anime = anime
            Picasso.with(activity)
                    .loadInto(anime.imageUrl, imageView)
            englishTitleText.text = anime.titleEnglish
            statusText.text = anime.airingStatus
        }

        override fun onClick(view: View) {
            startActivity(SeriesDetailActivity.newIntent(activity, anime.id))
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
                notifyItemInserted(oldSize + i)
                Picasso.with(activity)
                        .load(items[i].imageUrl)
                        .fetch()
            }
        }

        fun clearItems() {
            val oldSize = series.size
            notifyItemRangeRemoved(0, oldSize)
            series = emptyList()
        }
    }
}
