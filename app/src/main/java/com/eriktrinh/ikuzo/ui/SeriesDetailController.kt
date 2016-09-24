package com.eriktrinh.ikuzo.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bluelinelabs.conductor.Controller
import com.eriktrinh.ikuzo.R
import com.eriktrinh.ikuzo.SeriesLab
import com.eriktrinh.ikuzo.domain.Anime
import com.eriktrinh.ikuzo.ext.loadInto
import com.eriktrinh.ikuzo.web.SeriesService
import com.eriktrinh.ikuzo.web.ServiceGenerator
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.controller_series_detail.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SeriesDetailController(args: Bundle?) : Controller(args) {
    companion object {
        private val TAG = "SeriesDetailController"
        private val KEY_ID = "ARGS_ID"
        private fun Bundle.putId(id: Int): Bundle {
            this.putInt(KEY_ID, id)
            return this
        }
    }

    constructor(id: Int) : this(Bundle().putId(id))

    private lateinit var series: Anime // TODO Figure out how to handle both anime and manga w/o too much code duplication
    private lateinit var seriesService: SeriesService
    private lateinit var seriesImageView: ImageView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        val view = inflater.inflate(R.layout.controller_series_detail, container, false)
        seriesImageView = view.series_detail_image

        seriesService = ServiceGenerator.createService(SeriesService::class.java, activity)

        val id = args.getInt(KEY_ID, -1)
        series = SeriesLab.get(activity).getOrElse(id) { throw RuntimeException("Series not found") }
        (activity as AppCompatActivity).supportActionBar?.title = series.titleEnglish

        val call = seriesService.getAnimePage(id)
        call.enqueue(object : Callback<Anime> {
            override fun onResponse(call: Call<Anime>, response: Response<Anime>?) {
                when (response?.raw()?.code() ?: 400) {
                    200 -> {
                        if (response != null) {
                            series = response.body()
                            Log.i(TAG, "Got: ${series}")
                            updateUI()
                        }
                    }
                }
            }

            override fun onFailure(call: Call<Anime>?, t: Throwable?) {
                throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })

        return view
    }

    private fun updateUI() {
        Picasso.with(activity)
                .loadInto(series.imageUrl, seriesImageView)
    }
}
