package com.eriktrinh.ikuzo.ui.browse

import android.content.Context
import com.eriktrinh.ikuzo.data.ani.Anime
import com.eriktrinh.ikuzo.utils.ext.browseAnime
import com.eriktrinh.ikuzo.web.ServiceGenerator
import com.eriktrinh.ikuzo.web.service.SeriesService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SeriesPresenter(context: Context) {
    private var controller: SeriesController? = null
    private val seriesService: SeriesService
    private var series: List<Anime> = emptyList()

    init {
        seriesService = ServiceGenerator.createService(SeriesService::class.java, context)
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
        call.enqueue(object : Callback<List<Anime>> {
            override fun onFailure(call: Call<List<Anime>>, t: Throwable?) {
                throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onResponse(call: Call<List<Anime>>, response: Response<List<Anime>>?) {
                when (response?.raw()?.code() ?: 400) {
                    200 -> {
                        series = response?.body() ?: emptyList()
                        publish()
                    }
                }
            }
        })
    }
}
