package com.eriktrinh.ikuzo.ui.browse

import com.eriktrinh.ikuzo.data.enums.AiringStatus
import com.eriktrinh.ikuzo.data.enums.Season
import com.eriktrinh.ikuzo.data.enums.Sort
import com.eriktrinh.ikuzo.data.enums.Type
import com.eriktrinh.ikuzo.utils.ext.getSeason
import com.eriktrinh.ikuzo.utils.ext.getYear
import java.util.*

data class QueryRequest(val year: Int?,
                        val season: Season,
                        val status: AiringStatus,
                        val type: Type,
                        val sort: Sort,
                        val descending: Boolean) {
    companion object {
        val DEFAULT = QueryRequest(Calendar.getInstance().getYear(),
                Calendar.getInstance().getSeason(),
                AiringStatus.NONE,
                Type.NONE,
                Sort.POPULARITY,
                true)
    }
}
