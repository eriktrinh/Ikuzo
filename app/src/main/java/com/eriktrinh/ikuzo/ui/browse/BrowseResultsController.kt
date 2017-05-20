package com.eriktrinh.ikuzo.ui.browse

import android.app.SearchManager
import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.SearchView
import android.view.*
import com.eriktrinh.ikuzo.R
import com.eriktrinh.ikuzo.ui.lists.SeriesController
import com.eriktrinh.ikuzo.ui.lists.SeriesPresenter


class BrowseResultsController : SeriesController(), BrowseDialogFragment.Delegate {
    override fun onOKPressed(request: QueryRequest) {
        presenter.newQuery(request)
    }

    companion object {
        private val TAG = "BrowseResultsController"
        private val BROWSER = "BrowseResultsController.BrowseDialog"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        val view = super.onCreateView(inflater, container)

        setHasOptionsMenu(true)

        presenter = SeriesPresenter(activity)
                .takeController(this)
        presenter.newQuery(QueryRequest.DEFAULT)
        return view
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDestroy()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.controller_series_view, menu)
        // Associate searchable configuration with the SearchView
        val searchManager = activity.getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.menu_item_search_series).actionView as SearchView
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(activity.componentName))
        // TODO: collapse searchView on query submit
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_item_query_series -> {
                val dialog = BrowseDialogFragment.newInstance()
                dialog.setDelegate(this)
                dialog.show((activity as AppCompatActivity).supportFragmentManager, BROWSER)
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
}
