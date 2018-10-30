package cc.lmiot.uwallpaper.Fragment

import android.app.ProgressDialog
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cc.lmiot.uwallpaper.Activity.PhotoBrowseActivity
import cc.lmiot.uwallpaper.Adapter.MainPictureAdapter
import cc.lmiot.uwallpaper.Config
import cc.lmiot.uwallpaper.MyApplication
import cc.lmiot.uwallpaper.R
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.facebook.fresco.helper.photoview.PictureBrowse
import com.facebook.fresco.helper.photoview.PictureBrowseActivity
import com.facebook.fresco.helper.photoview.entity.PhotoInfo
import com.kc.unsplash.Unsplash
import com.kc.unsplash.api.Order
import com.kc.unsplash.models.Photo
import com.kc.unsplash.models.Urls
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener
import com.scwang.smartrefresh.layout.listener.OnRefreshListener
import kotlinx.android.synthetic.main.fragment_latestpic.*


open class LatestPicFragment : Fragment() {

    companion object {
        var latestData: MutableList<Photo>? = mutableListOf<Photo>()
    }


    var page = 1
    var perPage = 20
    var photolist: MutableList<PhotoInfo>? = mutableListOf<PhotoInfo>()
    var photoInfo: PhotoInfo? = null

    var loadDialog: ProgressDialog? = null
    var mAdapter: MainPictureAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view: View = inflater.inflate(R.layout.fragment_latestpic, container, false)
        return view

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadDialog = ProgressDialog(context)
        loadDialog?.setCancelable(false)
       loadDialog!!.show()

        mAdapter = MainPictureAdapter(activity, latestData)
        var mLayoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        recyclerView_latestpic.layoutManager = mLayoutManager
        recyclerView_latestpic.adapter = mAdapter
        refreshLayout_latestpic.setOnRefreshListener(object : OnRefreshListener {
            override fun onRefresh(refreshLayout: RefreshLayout?) {
                refreshLayout!!.layout.postDelayed(object : Runnable {
                    override fun run() {
                        latestData = ArrayList()
                        page = 1
                        getLatestData(page)
                        refreshLayout.finishRefresh()
                    }

                }, 2000)
            }
        })
        refreshLayout_latestpic.setOnLoadMoreListener(object : OnLoadMoreListener {
            override fun onLoadMore(refreshLayout: RefreshLayout?) {
                if (page < 5) {
                    getLatestData(page)
                    refreshLayout!!.finishLoadMore()
                } else {
                    ToastUtils.showShort(getString(R.string.no_more1) + page * perPage + getString(R.string.no_more2))
                    refreshLayout!!.finishLoadMoreWithNoMoreData()
                }
            }
        })

        getLatestData(1)

        mAdapter!!.setOnItemClickListener(object : BaseQuickAdapter.OnItemClickListener {
            override fun onItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
               // ToastUtils.showShort("第" + position + "张")
                PictureBrowse.newBuilder(context, PhotoBrowseActivity::class.java)
                        .setPhotoList(photolist as java.util.ArrayList<PhotoInfo>?)
                        .setThumbnailView(view)
                        .setCurrentPosition(position)
                        .start()
            }
        })

    }

    fun setData(latestData: List<Photo>) {
        mAdapter!!.setNewData(latestData)
        page++
        if (loadDialog!!.isShowing()) {
            loadDialog!!.dismiss()
        loadDialog!!.cancel()
        }
    }

    fun getLatestData(page: Int) {
        MyApplication.unsplash!!.getPhotos(page, perPage, Order.LATEST, object : Unsplash.OnPhotosLoadedListener {
            override fun onComplete(photos: MutableList<Photo>?) {
                latestData!!.addAll(photos!!)
                setData(latestData!!)
                photolist = ArrayList();
                for (latestDatum in latestData!!) {
                    photoInfo = PhotoInfo()
                    var urls: Urls = latestDatum.urls
                    photoInfo!!.originalUrl = urls.regular
                    photoInfo!!.thumbnailUrl = urls.thumb
                    photoInfo!!.height = latestDatum.height
                    photoInfo!!.width = latestDatum.width
                    photolist!!.add(photoInfo!!)
                }
            }
            override fun onError(error: String?) {
                LogUtils.e("Error", error)
            }

        })
    }
}

