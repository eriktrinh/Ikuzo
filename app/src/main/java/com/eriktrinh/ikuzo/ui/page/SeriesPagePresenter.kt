package com.eriktrinh.ikuzo.ui.page

import android.app.Activity
import android.content.Context
import android.widget.Toast
import com.eriktrinh.ikuzo.data.ani.Anime
import com.eriktrinh.ikuzo.data.ani.Record
import com.eriktrinh.ikuzo.data.enums.ListStatus
import com.eriktrinh.ikuzo.data.payload.EditList
import com.eriktrinh.ikuzo.data.payload.Id
import com.eriktrinh.ikuzo.utils.shared_pref.MeUtils
import com.eriktrinh.ikuzo.web.ServiceGenerator
import com.eriktrinh.ikuzo.web.service.ListService
import com.eriktrinh.ikuzo.web.service.SeriesService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.combineLatest
import okhttp3.ResponseBody
import java.util.*

class SeriesPagePresenter(context: Context, id: Int) {
    companion object {
        val TAG = "SeriesPagePresenter"
    }

    private var controllers: MutableList<PagerChildController> = ArrayList()
    private val seriesService: SeriesService = ServiceGenerator.createService(SeriesService::class.java, context)
    private val listService: ListService = ServiceGenerator.createService(ListService::class.java, context)
    private var series: Anime? = null
    private var userStatus: Record? = null

    init {
        val pageCall = seriesService.getAnimePage(id)
        val statusCall = listService.getList(MeUtils.getMyId(context) ?: -1) // TODO make call and store response on app open/write on update
        pageCall.combineLatest(statusCall)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { (anime, seriesList) ->
                    series = anime
                    userStatus = seriesList.lists.getRecordById(id)
                    publish()
                }
    }

    fun takeController(controller: PagerChildController): SeriesPagePresenter {
        controllers.add(controller)
        controller.setPresenter(this)
        publish()
        return this
    }

    fun publish(controller: PagerChildController) {
        if (series != null) {
            controller.onNewItem(series!!, userStatus)
        }
    }

    fun publish() {
        if (series != null)
            controllers.forEach { it.onNewItem(series!!, userStatus) }
    }

    fun onDestroy() {
        controllers.clear()
    }

    fun isUpdateable(update: Record): Boolean {
        return userStatus != update
    }

    fun onUpdateButtonClicked(update: Record) {
        val successCallback: (ResponseBody) -> Unit = {
            userStatus = update
            controllers.forEach { controller -> controller.onItemUpdateableChanged(false) }
        }
        if (update.listStatus == ListStatus.NONE) {
            listService.delListEntry(series?.id ?: -1)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(successCallback)
        } else {
            val editList = EditList(series?.id ?: -1,
                    if (update.score == userStatus?.score) null else update.score,
                    if (update.episodesWatched == userStatus?.episodesWatched) null else update.episodesWatched,
                    if (update.listStatus == userStatus?.listStatus) null else update.listStatus
            )
            listService.putListEntry(editList).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(successCallback)
        }
    }

    fun onFavouriteButtonClicked(activity: Activity) {
        if (series != null) {
            val call = seriesService.favAnime(Id(series!!.id))
            call.observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        controllers.forEach { controller ->
                            controller.onFavouriteChanged(it.order == null)
                        }
                    }, {
                        Toast.makeText(activity, "Could not update favourite", Toast.LENGTH_SHORT)
                                .show()
                    })
        }
    }
}