package com.eriktrinh.ikuzo.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import com.bluelinelabs.conductor.Conductor
import com.bluelinelabs.conductor.Router
import com.bluelinelabs.conductor.RouterTransaction
import com.eriktrinh.ikuzo.R
import kotlinx.android.synthetic.main.activity_series_detail.*
import kotlinx.android.synthetic.main.controller_series_detail.*

class SeriesDetailActivity : AppCompatActivity() {

    companion object {
        private val EXTRA_SERIES_ID = "ikuzo.SERIES_ID"
        fun newIntent(context: Context, id: Int): Intent {
            return Intent(context, SeriesDetailActivity::class.java)
                    .putExtra(EXTRA_SERIES_ID, id)
        }
    }

    lateinit var router: Router
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_series_detail)
        setSupportActionBar(toolbar)

        val fab = findViewById(R.id.fab) as FloatingActionButton
        fab.setOnClickListener { view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show() }
        router = Conductor.attachRouter(this, content_series_detail, savedInstanceState)

        val id = intent.getIntExtra(EXTRA_SERIES_ID, -1)

        if (!router.hasRootController()) {
            router.setRoot(RouterTransaction.with(SeriesDetailController(id)))
        }
    }

    override fun onBackPressed() {
        if (!router.handleBack()) {
            super.onBackPressed()
        }
    }
}
