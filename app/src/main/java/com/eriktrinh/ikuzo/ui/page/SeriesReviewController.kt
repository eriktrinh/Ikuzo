package com.eriktrinh.ikuzo.ui.page

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.eriktrinh.ikuzo.R
import com.eriktrinh.ikuzo.data.ani.Anime
import com.eriktrinh.ikuzo.data.ani.Favourite
import com.eriktrinh.ikuzo.data.ani.Record
import com.eriktrinh.ikuzo.data.payload.Id
import com.eriktrinh.ikuzo.ui.SpacingItemDecoration
import com.eriktrinh.ikuzo.utils.ext.loadAndCropInto
import com.eriktrinh.ikuzo.web.ServiceGenerator
import com.eriktrinh.ikuzo.web.service.SeriesService
import com.jaredrummler.materialspinner.MaterialSpinner
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.controller_series_page.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 *  TODO:
 *  - Display the initial reviews (in a list)
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
        favFab.setOnClickListener { view ->
            val call = seriesService.favAnime(Id(id))
            call.enqueue(object : Callback<Favourite> {
                override fun onResponse(call: Call<Favourite>, response: Response<Favourite>?) {
                    if (response != null && response.code() == 200) {
                        favFab.setImageResource(if (response.body().order == null) R.drawable.ic_menu_favourited else R.drawable.ic_menu_unfavourited)
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

    override fun onItemsNext(series: Anime, status: Record?) {
        Picasso.with(activity)
                .loadAndCropInto(series.imageUrl, view.series_detail_image)
        characterAdapter.addItems(series.characters ?: emptyList())
        descriptionTextView.text = series.description?.replace("<br>", "")
        titleTextView.text = series.titleEnglish
        favFab.setImageResource(if (series.favourite ?: false) R.drawable.ic_menu_favourited else R.drawable.ic_menu_unfavourited)
    }

    override fun onItemUpdateableChanged(canUpdate: Boolean) {
    }

    override fun onFavouriteChanged(favourite: Boolean) {
    }
}