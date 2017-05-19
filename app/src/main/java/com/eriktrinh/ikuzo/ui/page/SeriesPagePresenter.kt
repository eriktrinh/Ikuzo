package com.eriktrinh.ikuzo.ui.page

import android.app.Activity
import android.content.Context
import android.util.Log
import android.widget.Toast
import com.eriktrinh.ikuzo.data.ani.Anime
import com.eriktrinh.ikuzo.data.ani.Favourite
import com.eriktrinh.ikuzo.data.ani.Record
import com.eriktrinh.ikuzo.data.ani.SeriesList
import com.eriktrinh.ikuzo.data.enums.ListStatus
import com.eriktrinh.ikuzo.data.payload.EditList
import com.eriktrinh.ikuzo.data.payload.Id
import com.eriktrinh.ikuzo.utils.shared_pref.MeUtils
import com.eriktrinh.ikuzo.web.ServiceGenerator
import com.eriktrinh.ikuzo.web.service.ListService
import com.eriktrinh.ikuzo.web.service.SeriesService
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class SeriesPagePresenter(context: Context, id: Int) {
    companion object {
        val TAG = "SeriesPagePresenter"
    }

    private var controllers: MutableList<PagerChildController> = ArrayList()
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
                    userStatus = body!!.lists.getRecordById(id)
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
        val updateCallback = object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>?) {
                if (response != null && response.code() == 200) {
                    userStatus = update
                    controllers.forEach { it.onItemUpdateableChanged(false) }
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

    fun onFavouriteButtonClicked(activity: Activity) {
        if (series != null) {
            val call = seriesService.favAnime(Id(series!!.id))
            call.enqueue(object : Callback<Favourite> {
                override fun onResponse(call: Call<Favourite>, response: Response<Favourite>?) {
                    if (response != null && response.code() == 200) {
                        controllers.forEach { it.onFavouriteChanged(response.body()!!.order == null) }
                    } else {
                        Toast.makeText(activity, "Could not update favourite", Toast.LENGTH_SHORT)
                                .show()
                    }
                }

                override fun onFailure(call: Call<Favourite>, t: Throwable?) {
                    throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
                }
            })
        }
    }
}