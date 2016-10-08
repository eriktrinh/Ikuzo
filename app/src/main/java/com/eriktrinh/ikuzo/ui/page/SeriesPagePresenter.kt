package com.eriktrinh.ikuzo.ui.page

import android.content.Context
import android.util.Log
import com.eriktrinh.ikuzo.data.ani.Anime
import com.eriktrinh.ikuzo.data.ani.Record
import com.eriktrinh.ikuzo.data.ani.SeriesList
import com.eriktrinh.ikuzo.data.enums.ListStatus
import com.eriktrinh.ikuzo.data.payload.EditList
import com.eriktrinh.ikuzo.utils.shared_pref.MeUtils
import com.eriktrinh.ikuzo.web.ServiceGenerator
import com.eriktrinh.ikuzo.web.service.ListService
import com.eriktrinh.ikuzo.web.service.SeriesService
import kotlinx.android.synthetic.main.controller_series_page.view.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SeriesPagePresenter(context: Context, id: Int) {
    companion object {
        val TAG = "SeriesPagePresenter"
    }

    private var controller: SeriesPageController? = null
    private val seriesService: SeriesService
    private val listService: ListService
    private var series: Anime? = null
    private var userStatus: Record? = null
    private var oneDone = false

    init {
        seriesService = ServiceGenerator.createService(SeriesService::class.java, context)
        listService = ServiceGenerator.createService(ListService::class.java, context)
        val pageCall = seriesService.getAnimePage(id)
        val statusCall = listService.getList(MeUtils.getMyId(context) ?: -1) // TODO make call and store response on app open/write on update
        pageCall.enqueue(object : Callback<Anime> {
            override fun onResponse(call: Call<Anime>, response: Response<Anime>?) {
                if (response != null && response.code() == 200) {
                    series = response.body()
                    Log.i(TAG, "Got: $series")
                    if (oneDone) publish() else {
                        oneDone = true
                    }
                }
            }

            override fun onFailure(call: Call<Anime>?, t: Throwable?) {
                throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })
        statusCall.enqueue(object : Callback<SeriesList> {
            override fun onResponse(call: Call<SeriesList>, response: Response<SeriesList>?) {
                if (response != null && response.code() == 200) {
                    val body = response.body()
                    userStatus = body.lists.getRecordById(id)
                    if (oneDone) publish() else {
                        oneDone = true
                    }
                }
            }

            override fun onFailure(call: Call<SeriesList>, t: Throwable?) {
                throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })
    }

    fun takeController(controller: SeriesPageController): SeriesPagePresenter {
        this.controller = controller
        publish()
        return this
    }

    fun publish() {
        if (controller != null) {
            if (series != null)
                controller?.onItemsNext(series!!, userStatus)
        }
    }

    fun onDestroy() {
        controller = null
    }

    fun onSpinnersUpdated(update: Record): Boolean {
        return userStatus != update
    }

    fun onUpdateButtonClicked(update: Record) {
        val updateCallback = object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>?) {
                if (response != null && response.code() == 200 && controller != null) {
                    userStatus = update
                    controller!!.view.series_detail_update_button.isEnabled = false
                    return
                }
                throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable?) {
                throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        }
        if (update.listStatus == ListStatus.NONE) {
            listService.delListEntry(series?.id ?: -1).enqueue(updateCallback)
        } else {
            val editList = EditList(series?.id ?: -1,
                    if (update.score == userStatus?.score) null else update.score,
                    if (update.episodesWatched == userStatus?.episodesWatched) null else update.episodesWatched,
                    if (update.listStatus == userStatus?.listStatus) null else update.listStatus
            )
            listService.putListEntry(editList).enqueue(updateCallback)
        }
    }
}