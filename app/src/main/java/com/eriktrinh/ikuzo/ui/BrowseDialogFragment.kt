package com.eriktrinh.ikuzo.ui

import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import com.eriktrinh.ikuzo.R
import com.eriktrinh.ikuzo.data.enums.Season
import com.eriktrinh.ikuzo.data.enums.Sort
import com.eriktrinh.ikuzo.utils.ext.getYear
import kotlinx.android.synthetic.main.dialog_browse.view.*
import java.util.*

class BrowseDialogFragment : DialogFragment() {
    interface Delegate {
        fun onOKPressed()
    }

    companion object {
        fun newInstance(): BrowseDialogFragment {
            return BrowseDialogFragment()
        }
    }

    private var delegate: Delegate? = null

    fun setDelegate(delegate: Delegate?) {
        this.delegate = delegate
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val inflater = LayoutInflater.from(activity)
        val view = inflater.inflate(R.layout.dialog_browse, null)

        setSpinners(view)

        return AlertDialog.Builder(activity)
                .setView(view)
                .setTitle(R.string.browse_title)
                .setPositiveButton(android.R.string.ok) { dialogInterface, i ->
                    delegate?.onOKPressed()
                }
                .create()
    }

    private fun setSpinners(view: View) {
        val currYear = Calendar.getInstance().getYear()
        val firstYear = 1951

        val years = listOf("").plus(Array(currYear - firstYear) { (currYear - it).toString() })
        view.browse_year_spinner.setItems(years)

        val seasons = listOf("").plus(Season.values().map(Season::string))
        val seasonSpinner = view.browse_season_spinner
        seasonSpinner.setItems(seasons)

        val sortItems = Sort.values().map(Sort::string)
        val sortSpinner = view.browse_sort_spinner
        sortSpinner.setItems(sortItems)
    }
}