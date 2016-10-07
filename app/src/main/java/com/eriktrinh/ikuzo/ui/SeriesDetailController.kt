package com.eriktrinh.ikuzo.ui

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bluelinelabs.conductor.Controller
import com.eriktrinh.ikuzo.R
import com.eriktrinh.ikuzo.SeriesLab
import com.eriktrinh.ikuzo.ani.domain.Anime
import com.eriktrinh.ikuzo.ani.domain.Favourite
import com.eriktrinh.ikuzo.ani.domain.Record
import com.eriktrinh.ikuzo.ani.domain.SeriesList
import com.eriktrinh.ikuzo.enums.ListStatus
import com.eriktrinh.ikuzo.ext.loadAndCropInto
import com.eriktrinh.ikuzo.payload.EditList
import com.eriktrinh.ikuzo.payload.Id
import com.eriktrinh.ikuzo.utils.MeUtils
import com.eriktrinh.ikuzo.web.ListService
import com.eriktrinh.ikuzo.web.SeriesService
import com.eriktrinh.ikuzo.web.ServiceGenerator
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.controller_series_detail.view.*
import okhttp3.ResponseBody
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

    private lateinit var seriesService: SeriesService
    private lateinit var listService: ListService
    private var id: Int = -1
    private lateinit var series: Anime // TODO Figure out how to handle both anime and manga w/o too much code duplication
    private lateinit var seriesImageView: ImageView
    private lateinit var characterAdapter: CharacterAdapter
    private lateinit var descriptionTextView: TextView
    private lateinit var titleTextView: TextView
    private lateinit var favouriteButton: Button

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        val view = inflater.inflate(R.layout.controller_series_detail, container, false)
        seriesImageView = view.series_detail_image
        descriptionTextView = view.series_detail_description
        titleTextView = view.series_detail_title
        favouriteButton = view.series_detail_fav_button

        view.fab.setOnClickListener { view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show() }

        seriesService = ServiceGenerator.createService(SeriesService::class.java, activity)
        listService = ServiceGenerator.createService(ListService::class.java, activity)

        id = args.getInt(KEY_ID, id)
        series = SeriesLab.getOrElse(id) { throw RuntimeException("Series not found") }

        val recyclerView = view.series_detail_character_recycler
        val layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        characterAdapter = CharacterAdapter(activity, emptyList())
        recyclerView.adapter = characterAdapter
        recyclerView.layoutManager = layoutManager
        recyclerView.addItemDecoration(SpacingItemDecoration(
                resources.getDimensionPixelSize(R.dimen.design_card_margin),
                resources.getDimensionPixelSize(R.dimen.activity_horizontal_margin),
                true))

        val pageCall = seriesService.getAnimePage(id)
        val statusCall = listService.getList(MeUtils.getMyId(activity) ?: -1) // TODO make call and store response on app open/write on update
        pageCall.enqueue(object : Callback<Anime> {
            override fun onResponse(call: Call<Anime>, response: Response<Anime>?) {
                when (response?.raw()?.code() ?: 400) {
                    200 -> {
                        if (response != null) {
                            series = response.body()
                            Log.i(TAG, "Got: $series")
                            updateUI()
                        }
                    }
                }
            }

            override fun onFailure(call: Call<Anime>?, t: Throwable?) {
                throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })

        initButtons()
        return view
    }

    private fun updateUI() {
        Picasso.with(activity)
                .loadAndCropInto(series.imageUrl, seriesImageView)
        characterAdapter.addItems(series.characters ?: emptyList())
        descriptionTextView.text = series.description?.replace("<br>", "")
        titleTextView.text = series.titleEnglish
        favouriteButton.text = if (series.favourite ?: false) "Favourited" else "Unfavourited"
    }

    private fun initButtons() {
        favouriteButton.setOnClickListener {
            val call = seriesService.favAnime(Id(id))
            call.enqueue(object : Callback<Favourite> {
                override fun onResponse(call: Call<Favourite>, response: Response<Favourite>?) {
                    if (response != null && response.code() == 200) {
                        favouriteButton.text = if (response.body().order == null) "Favourited" else "Unfavourited"
                    } else {
                        Toast.makeText(activity, "Could not update favourite", Toast.LENGTH_SHORT)
                                .show()
                    }
                }

                override fun onFailure(call: Call<Favourite>, t: Throwable?) {
                    throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
                }
            })
        }
    }
}
