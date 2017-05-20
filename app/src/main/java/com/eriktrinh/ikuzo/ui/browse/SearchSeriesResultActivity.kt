package com.eriktrinh.ikuzo.ui.browse

import android.app.SearchManager
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.bluelinelabs.conductor.Conductor
import com.bluelinelabs.conductor.Router
import com.bluelinelabs.conductor.RouterTransaction
import com.eriktrinh.ikuzo.R
import kotlinx.android.synthetic.main.activity_search_series_result.*

class SearchSeriesResultActivity : AppCompatActivity() {

    companion object {
        private val TAG = "SearchSeriesResultActiv"
    }

    lateinit var router: Router

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_series_result)
        router = Conductor.attachRouter(this, controller_container, savedInstanceState)
    }

    override fun onNewIntent(intent: Intent) {
        handleIntent(intent)
    }

    fun handleIntent(intent: Intent) {
        if (Intent.ACTION_SEARCH == intent.action) {
            val query = intent.getStringExtra(SearchManager.QUERY)
            Log.d(TAG, "Got query: $query")
            //use the query to search your data somehow
            if (!router.hasRootController()) {
                router.setRoot(RouterTransaction.with(SearchSeriesResultController(query)))
            }
        }
    }

    override fun onBackPressed() {
        if (!router.handleBack()) {
            super.onBackPressed()
        }
    }
}
