package com.eriktrinh.ikuzo.ui.browse

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.eriktrinh.ikuzo.ui.lists.SeriesController
import com.eriktrinh.ikuzo.ui.lists.SeriesPresenter

// TODO: fix this shit (requires default ctor or bundle arg)
class SearchSeriesResultController(val query: String = "") : SeriesController() {

    companion object {
        private val TAG = "SearchSeriesResultCtr"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        val view = super.onCreateView(inflater, container)

        presenter = SeriesPresenter(activity)
                .takeController(this)
        presenter.newSearch(query)
        return view
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDestroy()
    }

    override fun handleBack(): Boolean {
        return super.handleBack()
    }
}
