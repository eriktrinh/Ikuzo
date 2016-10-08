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
                            statusCall.enqueue(object : Callback<SeriesList> {
                                override fun onResponse(call: Call<SeriesList>, response: Response<SeriesList>?) {
                                    if (response != null && response.code() == 200) {
                                        val body = response.body()
                                        initSpinners(series, body.lists.getRecordById(series.id))
                                    }
                                }

                                override fun onFailure(call: Call<SeriesList>, t: Throwable?) {
                                    throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
                                }
                            })
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

    private fun initSpinners(series: Anime, status: Record?) {
        val updateButton = view.series_detail_update_button

        var preUpdate = status?.copy() ?: Record(ListStatus.NONE, 0, 0, Id(series.id))
        val posUpdate = preUpdate.copy()

        val episodes = emptyList<Int>().plus(Array(series.totalEpisodes + 1, Int::toInt))
        val progSpinner = view.series_detail_progress_spinner
        progSpinner.setItems(episodes)
        progSpinner.isEnabled = false

        val scores = emptyList<Int>().plus(Array(11, Int::toInt))
        val scoreSpinner = view.series_detail_score_spinner
        scoreSpinner.setItems(scores)
        scoreSpinner.isEnabled = false

        val statuses = ListStatus.values().map { it.string }
        val statusSpinner = view.series_detail_status_spinner
        statusSpinner.setItems(statuses)

        if (status != null) {
            progSpinner.selectedIndex = preUpdate.episodesWatched
            scoreSpinner.selectedIndex = preUpdate.score
            statusSpinner.selectedIndex = statuses.indexOf(preUpdate.listStatus.string)
            scoreSpinner.isEnabled = status.listStatus != ListStatus.PLAN_TO_WATCH &&
                    status.listStatus != ListStatus.NONE
            progSpinner.isEnabled = scoreSpinner.isEnabled &&
                    status.listStatus != ListStatus.COMPLETED
        }

        progSpinner.setOnItemSelectedListener { view, position, id, item ->
            posUpdate.episodesWatched = position
            updateButton.isEnabled = posUpdate != preUpdate
        }
        scoreSpinner.setOnItemSelectedListener { view, position, id, item ->
            posUpdate.score = position
            updateButton.isEnabled = posUpdate != preUpdate
        }
        statusSpinner.setOnItemSelectedListener { view, position, id, item ->
            val listStatus = ListStatus.values()[position]
            posUpdate.listStatus = listStatus
            if (listStatus == ListStatus.COMPLETED) {
                posUpdate.episodesWatched = series.totalEpisodes
                progSpinner.selectedIndex = series.totalEpisodes
                progSpinner.isEnabled = false
            } else if (listStatus == ListStatus.NONE || listStatus == ListStatus.PLAN_TO_WATCH) {
                posUpdate.episodesWatched = 0
                progSpinner.selectedIndex = 0
                posUpdate.score = 0
                scoreSpinner.selectedIndex = 0
                progSpinner.isEnabled = false
                scoreSpinner.isEnabled = false
            } else {
                progSpinner.isEnabled = true
                scoreSpinner.isEnabled = true
            }
            updateButton.isEnabled = posUpdate != preUpdate
        }

        val updateCallback = object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>?) {
                if (response != null && response.code() == 200) {
                    preUpdate = posUpdate
                    updateButton.isEnabled = false
                    return
                }
                throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable?) {
                throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        }
        updateButton.setOnClickListener {
            if (posUpdate.listStatus == ListStatus.NONE) {
                listService.delListEntry(series.id).enqueue(updateCallback)
            } else {
                val editList = EditList(series.id,
                        if (preUpdate.score == posUpdate.score) null else posUpdate.score,
                        if (preUpdate.episodesWatched == posUpdate.episodesWatched) null else posUpdate.episodesWatched,
                        if (preUpdate.listStatus == posUpdate.listStatus) null else posUpdate.listStatus
                )
                listService.putListEntry(editList).enqueue(updateCallback)
            }
        }
    }
}
