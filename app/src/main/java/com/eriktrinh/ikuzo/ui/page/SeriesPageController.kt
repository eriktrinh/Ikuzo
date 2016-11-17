package com.eriktrinh.ikuzo.ui.page

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.eriktrinh.ikuzo.R
import com.eriktrinh.ikuzo.data.ani.Anime
import com.eriktrinh.ikuzo.data.ani.Record
import com.eriktrinh.ikuzo.data.enums.ListStatus
import com.eriktrinh.ikuzo.data.payload.Id
import com.eriktrinh.ikuzo.ui.SpacingItemDecoration
import com.eriktrinh.ikuzo.utils.ext.loadAndCropInto
import com.eriktrinh.ikuzo.web.ServiceGenerator
import com.eriktrinh.ikuzo.web.service.SeriesService
import com.jaredrummler.materialspinner.MaterialSpinner
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.controller_series_page.view.*

class SeriesPageController(args: Bundle?) : PagerChildController(args) {

    companion object {
        private val TAG = "SeriesPageController"
        private val KEY_ID = "ARGS_ID"
        private fun Bundle.putId(id: Int): Bundle {
            this.putInt(KEY_ID, id)
            return this
        }
    }

    constructor(id: Int) : this(Bundle().putId(id))

    private lateinit var seriesService: SeriesService
    private val id: Int
    private lateinit var characterAdapter: CharacterAdapter
    private lateinit var descriptionTextView: TextView
    private lateinit var titleTextView: TextView
    private lateinit var favFab: FloatingActionButton
    private lateinit var progSpinner: MaterialSpinner
    private lateinit var scoreSpinner: MaterialSpinner
    private lateinit var statusSpinner: MaterialSpinner

    init {
        id = args?.getInt(KEY_ID, -1) ?: -1
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        val view = inflater.inflate(R.layout.controller_series_page, container, false)
        descriptionTextView = view.series_detail_description
        titleTextView = view.series_detail_title
        favFab = view.series_detail_fav_fab
        progSpinner = view.series_detail_progress_spinner
        scoreSpinner = view.series_detail_score_spinner
        statusSpinner = view.series_detail_status_spinner

        seriesService = ServiceGenerator.createService(SeriesService::class.java, activity)

        val recyclerView = view.series_detail_character_recycler
        val layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        characterAdapter = CharacterAdapter(activity, emptyList())
        recyclerView.adapter = characterAdapter
        recyclerView.layoutManager = layoutManager
        recyclerView.addItemDecoration(SpacingItemDecoration(
                resources.getDimensionPixelSize(R.dimen.design_card_margin),
                resources.getDimensionPixelSize(R.dimen.activity_horizontal_margin),
                true))

        initButtons()

        return view
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    private fun initButtons() {
        favFab.setImageResource(R.drawable.ic_menu_unfavourited)
        favFab.setOnClickListener { getPresenter().onFavouriteButtonClicked(activity) }
    }

    private fun initSpinners(series: Anime, status: Record?) {
        val updateButton = view.series_detail_update_button
        val posUpdate = status?.copy() ?: Record(ListStatus.NONE, 0, 0, Id(series.id))
        val presenter = getPresenter()

        val episodes = emptyList<Int>().plus(Array(series.totalEpisodes + 1, Int::toInt))
        progSpinner.setItems(episodes)
        progSpinner.isEnabled = false

        val scores = emptyList<Int>().plus(Array(11, Int::toInt))
        scoreSpinner.setItems(scores)
        scoreSpinner.isEnabled = false

        val statuses = ListStatus.values().map { it.string }
        statusSpinner.setItems(statuses)

        progSpinner.setOnItemSelectedListener { view, position, id, item ->
            posUpdate.episodesWatched = position
            updateButton.isEnabled = presenter.isUpdateable(posUpdate)
        }
        scoreSpinner.setOnItemSelectedListener { view, position, id, item ->
            posUpdate.score = position
            updateButton.isEnabled = presenter.isUpdateable(posUpdate)
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
            updateButton.isEnabled = presenter.isUpdateable(posUpdate)
        }

        updateButton.setOnClickListener {
            presenter.onUpdateButtonClicked(posUpdate)
        }
    }

    private fun setSpinnersSelected(status: Record?) {
        if (status != null) {
            val statuses = ListStatus.values().map { it.string }
            progSpinner.selectedIndex = status.episodesWatched
            scoreSpinner.selectedIndex = status.score
            statusSpinner.selectedIndex = statuses.indexOf(status.listStatus.string)
            scoreSpinner.isEnabled = status.listStatus != ListStatus.PLAN_TO_WATCH &&
                    status.listStatus != ListStatus.NONE
            progSpinner.isEnabled = scoreSpinner.isEnabled &&
                    status.listStatus != ListStatus.COMPLETED
        }
    }

    override fun onItemsNext(series: Anime, status: Record?) {
        Picasso.with(activity)
                .loadAndCropInto(series.imageUrl, view.series_detail_image)
        characterAdapter.addItems(series.characters ?: emptyList())
        descriptionTextView.text = series.description?.replace("<br>", "")
        titleTextView.text = series.titleEnglish
        onFavouriteChanged(series.favourite ?: false)
        initSpinners(series, status)
        setSpinnersSelected(status)
    }

    override fun onItemUpdateableChanged(canUpdate: Boolean) {
        view.series_detail_update_button.isEnabled = false
    }

    override fun onFavouriteChanged(favourite: Boolean) {
        favFab.setImageResource(if (favourite) R.drawable.ic_menu_favourited else R.drawable.ic_menu_unfavourited)
    }
}
