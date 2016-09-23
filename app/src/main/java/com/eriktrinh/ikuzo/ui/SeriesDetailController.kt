package com.eriktrinh.ikuzo.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bluelinelabs.conductor.Controller
import com.eriktrinh.ikuzo.R
import com.eriktrinh.ikuzo.SeriesLab
import com.eriktrinh.ikuzo.domain.Anime

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

    lateinit var series: Anime // TODO Figure out how to handle both anime and manga w/o too much code duplication

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        val id = args.getInt(KEY_ID, -1)
        series = SeriesLab.get(activity).getOrElse(id) { throw RuntimeException("Series not found") } as Anime
        Log.i(TAG, "Got: $series")
        (activity as AppCompatActivity).supportActionBar?.title = series.titleEnglish
        return inflater.inflate(R.layout.controller_series_detail, container, false)
    }
}
