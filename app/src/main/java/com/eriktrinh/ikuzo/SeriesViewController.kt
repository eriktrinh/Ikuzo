package com.eriktrinh.ikuzo

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bluelinelabs.conductor.Controller
import com.eriktrinh.ikuzo.domain.Anime
import com.eriktrinh.ikuzo.domain.Series
import com.eriktrinh.ikuzo.web.BrowseService
import com.eriktrinh.ikuzo.web.ServiceGenerator
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.controller_series_view.view.*
import kotlinx.android.synthetic.main.list_item_series_view.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SeriesViewController : Controller() {
    private lateinit var adapter: SeriesAdapter
    private lateinit var browseService: BrowseService

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        val view = inflater.inflate(R.layout.controller_series_view, container, false)

        val recyclerView = view.controller_series_recycler_view
        val layoutManager = LinearLayoutManager(activity)
        recyclerView.layoutManager = layoutManager

        adapter = SeriesAdapter(emptyList())
        recyclerView.adapter = adapter

        browseService = ServiceGenerator.createService(BrowseService::class.java, activity)
        val call = browseService.browseAnime()
        call.enqueue(object : Callback<List<Anime>> {
            override fun onFailure(call: Call<List<Anime>>?, t: Throwable?) {
                throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onResponse(call: Call<List<Anime>>, response: Response<List<Anime>>?) {
                when (response?.raw()?.code() ?: 400) { // TODO Handle case where user has not auth'd yet
                    200 -> adapter.addItems(response?.body() ?: emptyList())
                }
            }
        })
        return view
    }

    private fun Picasso.loadInto(url: String?, target: ImageView) {
        this.load(url)
                .noFade()
                .into(target)
    }

    inner private class SeriesHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView = itemView.series_image
        private val englishTitleText = itemView.series_english_name
        private val statusText = itemView.series_status

        fun bindItem(series: Series) {
            if (series is Anime) {
                Picasso.with(activity)
                        .loadInto(series.imageUrl, imageView)
                englishTitleText.text = series.titleEnglish
                statusText.text = series.airingStatus
            }
        }
    }

    inner private class SeriesAdapter(series: List<Series>) : RecyclerView.Adapter<SeriesHolder>() {
        private lateinit var series: List<Series>

        init {
            this.series = series
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SeriesHolder {
            val inflater = LayoutInflater.from(activity)
            val view = inflater.inflate(R.layout.list_item_series_view, parent, false)

            return SeriesHolder(view)
        }

        override fun onBindViewHolder(holder: SeriesHolder?, position: Int) {
            holder?.bindItem(series[position])
        }

        override fun getItemCount(): Int {
            return series.size
        }

        fun addItems(items: List<Series>) {
            val oldSize = series.size
            series = series.plus(items)
            for (i in 0..items.size - 1) {
                notifyItemInserted(oldSize + i)
                Picasso.with(activity)
                        .load((items[i] as Anime).imageUrl)
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
