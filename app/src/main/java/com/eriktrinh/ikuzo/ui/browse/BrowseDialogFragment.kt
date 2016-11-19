package com.eriktrinh.ikuzo.ui.browse

import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.widget.CheckBox
import com.eriktrinh.ikuzo.R
import com.eriktrinh.ikuzo.data.enums.AiringStatus
import com.eriktrinh.ikuzo.data.enums.Season
import com.eriktrinh.ikuzo.data.enums.Sort
import com.eriktrinh.ikuzo.data.enums.Type
import com.eriktrinh.ikuzo.utils.ext.getYear
import com.jaredrummler.materialspinner.MaterialSpinner
import kotlinx.android.synthetic.main.dialog_browse.view.*
import java.util.*

class BrowseDialogFragment : DialogFragment() {
    interface Delegate {
        fun onOKPressed(request: QueryRequest)
    }

    companion object {
        fun newInstance(): BrowseDialogFragment {
            return BrowseDialogFragment()
        }
    }

    private var delegate: Delegate? = null
    private val currYear = Calendar.getInstance().getYear()
    private lateinit var yearSpinner: MaterialSpinner
    private lateinit var seasonSpinner: MaterialSpinner
    private lateinit var statusSpinner: MaterialSpinner
    private lateinit var typeSpinner: MaterialSpinner
    private lateinit var sortSpinner: MaterialSpinner
    private lateinit var descCheckbox: CheckBox

    fun setDelegate(delegate: Delegate?) {
        this.delegate = delegate
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val inflater = LayoutInflater.from(activity)
        val view = inflater.inflate(R.layout.dialog_browse, null)

        yearSpinner = view.browse_year_spinner
        seasonSpinner = view.browse_season_spinner
        statusSpinner = view.browse_status_spinner
        typeSpinner = view.browse_type_spinner
        sortSpinner = view.browse_sort_spinner
        descCheckbox = view.browse_desc_checkbox

        setSpinners()

        return AlertDialog.Builder(activity)
                .setView(view)
                .setTitle(R.string.browse_title)
                .setPositiveButton(android.R.string.ok) { dialogInterface, i ->
                    delegate?.onOKPressed(getRequest())
                }
                .create()
    }

    private fun getRequest(): QueryRequest {
        val year = if (yearSpinner.selectedIndex == 0) null else currYear - yearSpinner.selectedIndex + 1
        val season = Season.values()[seasonSpinner.selectedIndex]
        val status = AiringStatus.values()[statusSpinner.selectedIndex]
        val type = Type.values()[typeSpinner.selectedIndex]
        val sort = Sort.values()[sortSpinner.selectedIndex]
        val descending = descCheckbox.isChecked
        return QueryRequest(year, season, status, type, sort, descending)
    }

    private fun setSpinners() {
        val firstYear = 1951

        val years = listOf("").plus(Array(currYear - firstYear) { (currYear - it).toString() })
        yearSpinner.setItems(years)

        val seasons = Season.values().map(Season::string)
        seasonSpinner.setItems(seasons)

        val statuses = AiringStatus.values().map(AiringStatus::string)
        statusSpinner.setItems(statuses)

        val types = Type.values().map(Type::string)
        typeSpinner.setItems(types)

        val sortItems = Sort.values().map(Sort::display)
        sortSpinner.setItems(sortItems)
    }
}