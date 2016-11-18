package com.eriktrinh.ikuzo.ui.page

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bluelinelabs.conductor.Controller
import com.bluelinelabs.conductor.support.ControllerPagerAdapter
import com.eriktrinh.ikuzo.R
import kotlinx.android.synthetic.main.controller_series_pager.view.*

class SeriesPagePagerController(args: Bundle?) : Controller(args) {
    companion object {
        private val TAG = "SeriesPagePagerController"
        private val KEY_ID = "ARGS_ID"
        private fun Bundle.putId(id: Int): Bundle {
            this.putInt(KEY_ID, id)
            return this
        }
    }

    private val id: Int
    private val pagerAdapter: ControllerPagerAdapter
    private var presenter: SeriesPagePresenter? = null
    private lateinit var viewPager: ViewPager
    private lateinit var tabLayout: TabLayout

    init {
        id = args?.getInt(KEY_ID, -1) ?: -1
        pagerAdapter = object : ControllerPagerAdapter(this, false) {
            override fun getItem(position: Int): Controller {
                val child = if (position == 0) SeriesPageController(id) else SeriesReviewController(id)
                if (presenter == null) {
                    presenter = SeriesPagePresenter(activity, id)
                }
                presenter?.takeController(child)
                return child
            }

            override fun getCount(): Int {
                return 2
            }

            override fun getPageTitle(position: Int): CharSequence {
                when (position) {
                    0 -> return "Details"
                    else -> return "Reviews"
                }
            }
        }
    }

    constructor(id: Int) : this(Bundle().putId(id))

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        val view = inflater.inflate(R.layout.controller_series_pager, container)
        viewPager = view.view_pager
        tabLayout = view.tab_layout
        viewPager.adapter = pagerAdapter
        tabLayout.setupWithViewPager(viewPager)
        return view
    }

    override fun onDestroy() {
        super.onDestroy()
        viewPager.adapter = null
        presenter?.onDestroy()
    }
}