package com.eriktrinh.ikuzo.ui

import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.widget.ArrayAdapter
import com.eriktrinh.ikuzo.R
import com.eriktrinh.ikuzo.enums.Season
import com.eriktrinh.ikuzo.enums.Sort
import com.eriktrinh.ikuzo.ext.getYear
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
        val spinnerItem = android.R.layout.simple_spinner_item
        val spinnerDropdownItem = android.R.layout.simple_spinner_dropdown_item

        val currYear = Calendar.getInstance().getYear()
        val years = Array(currYear - 1951) { (currYear - it).toString() }
        val yearAdapter = ArrayAdapter<String>(activity, spinnerItem, listOf("").plus(years))
        yearAdapter.setDropDownViewResource(spinnerDropdownItem)
        view.browse_year_spinner.adapter = yearAdapter
//        view.browse_year_spinner.setSelection()

        val seasonAdapter = ArrayAdapter<String>(activity, spinnerItem, listOf("").plus(Season.values().map(Season::string)))
        seasonAdapter.setDropDownViewResource(spinnerDropdownItem)
        val seasonSpinner = view.browse_season_spinner
        seasonSpinner.adapter = seasonAdapter
//        seasonSpinner.setSelection()

        val sortAdapter = ArrayAdapter<String>(activity, spinnerItem, Sort.values().map(Sort::string))
        sortAdapter.setDropDownViewResource(spinnerDropdownItem)
        val sortSpinner = view.browse_sort_spinner
        sortSpinner.adapter = sortAdapter
    }
}