package com.eriktrinh.ikuzo.utils.ext

import com.eriktrinh.ikuzo.data.ani.Anime
import com.eriktrinh.ikuzo.data.enums.AiringStatus
import com.eriktrinh.ikuzo.data.enums.Season
import com.eriktrinh.ikuzo.data.enums.Type
import com.eriktrinh.ikuzo.ui.browse.QueryRequest
import com.eriktrinh.ikuzo.web.service.SeriesService
import io.reactivex.Observable

fun SeriesService.browseAnime(request: QueryRequest, page: Int = 1): Observable<List<Anime>> {
    return this.browseAnime(
            request.year,
            if (request.season == Season.NONE) null else request.season.string,
            if (request.status == AiringStatus.NONE) null else request.status.string,
            if (request.type == Type.NONE) null else request.type.string,
            request.sort.string + if (request.descending) "-desc" else "",
            page
    )
}