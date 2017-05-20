package com.eriktrinh.ikuzo.ui.browse

import android.content.Context
import com.eriktrinh.ikuzo.data.ani.Anime
import com.eriktrinh.ikuzo.utils.ext.browseAnime
import com.eriktrinh.ikuzo.web.ServiceGenerator
import com.eriktrinh.ikuzo.web.service.SeriesService
import io.reactivex.android.schedulers.AndroidSchedulers

class SeriesPresenter(context: Context) {
    private var controller: SeriesController? = null
    private val seriesService: SeriesService = ServiceGenerator.createService(SeriesService::class.java, context)
    private var series: List<Anime> = emptyList()

    init {
        newRequest(QueryRequest.DEFAULT)
    }

    fun takeController(controller: SeriesController): SeriesPresenter {
        this.controller = controller
        publish()
        return this
    }

    fun publish() {
        if (controller != null) {
            controller?.onNewItems(series)
        }
    }

    fun onDestroy() {
        controller = null
    }

    fun newRequest(request: QueryRequest) {
        val call = seriesService.browseAnime(request)
        call.observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    series = it
                    publish()
                }
    }
}
