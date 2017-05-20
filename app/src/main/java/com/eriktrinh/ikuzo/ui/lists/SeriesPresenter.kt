package com.eriktrinh.ikuzo.ui.lists

import android.content.Context
import android.widget.Toast
import com.eriktrinh.ikuzo.data.ani.Anime
import com.eriktrinh.ikuzo.ui.browse.QueryRequest
import com.eriktrinh.ikuzo.utils.ext.browseAnime
import com.eriktrinh.ikuzo.web.ServiceGenerator
import com.eriktrinh.ikuzo.web.service.SeriesService
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import retrofit2.HttpException

class SeriesPresenter(val context: Context) {
    private var controller: SeriesController? = null
    private val seriesService: SeriesService = ServiceGenerator.createService(SeriesService::class.java, context)
    private var series: List<Anime> = emptyList()

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

    private fun newCall(request: Observable<List<Anime>>) {
        request.observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    series = it
                    publish()
                }, { e: Throwable ->
                    if (e is HttpException) {
                        Toast.makeText(context, e.response().code(), Toast.LENGTH_LONG).show()
                    }
                })
    }

    fun newQuery(query: QueryRequest) {
        newCall(seriesService.browseAnime(query))
    }

    fun newSearch(query: String) {
        newCall(seriesService.search(query))
    }
}
