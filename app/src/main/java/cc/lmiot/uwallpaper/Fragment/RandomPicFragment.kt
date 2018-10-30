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
import com.facebook.fresco.helper.photoview.entity.PhotoInfo
import com.kc.unsplash.Unsplash
import com.kc.unsplash.models.Photo
import com.kc.unsplash.models.Urls
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener
import com.scwang.smartrefresh.layout.listener.OnRefreshListener
import kotlinx.android.synthetic.main.fragment_randompic.*

class RandomPicFragment : Fragment() {

    companion object {
        var randomData: MutableList<Photo>? = mutableListOf<Photo>()
    }
    var page = 1
    var perPage = 20
    var photolist: MutableList<PhotoInfo>? = mutableListOf<PhotoInfo>()
    var photoInfo: PhotoInfo? = null

    var mAdapter: MainPictureAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view: View = inflater.inflate(R.layout.fragment_randompic, container, false)
        return view

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mAdapter = MainPictureAdapter(activity, randomData)
        var mLayoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        recyclerView_randompic.layoutManager = mLayoutManager
        recyclerView_randompic.adapter = mAdapter
        refreshLayout_randompic.setOnRefreshListener(object : OnRefreshListener {
            override fun onRefresh(refreshLayout: RefreshLayout?) {
                refreshLayout!!.layout.postDelayed(object : Runnable {
                    override fun run() {
                        randomData = ArrayList()
                        perPage = 20
                        getRandomPicData(perPage)
                        refreshLayout.finishRefresh()
                    }

                }, 2000)
            }
        })
        refreshLayout_randompic.setOnLoadMoreListener(object : OnLoadMoreListener {
            override fun onLoadMore(refreshLayout: RefreshLayout?) {
                if (page < 5) {
                    getRandomPicData(perPage)
                    refreshLayout!!.finishLoadMore()
                } else {
                    ToastUtils.showShort(getString(R.string.no_more1) + randomData!!.size + getString(R.string.no_more2))
                    refreshLayout!!.finishLoadMoreWithNoMoreData()
                }
            }
        })

        //首次获取数据
        getRandomPicData(perPage)

        mAdapter!!.setOnItemClickListener(object : BaseQuickAdapter.OnItemClickListener {
            override fun onItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
                //ToastUtils.showShort("第" + position + "张")
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
    }


    private fun getRandomPicData(perPage: Int) {
        MyApplication.unsplash!!.getRandomPhotos(null, false, null, null, null, null, null, perPage, object : Unsplash.OnPhotosLoadedListener {
            override fun onComplete(photos: MutableList<Photo>?) {
                if (photos != null) {
                    randomData!!.addAll(photos)
                    setData(randomData!!)
                    for (randomDatum in randomData!!) {
                        photoInfo = PhotoInfo()
                        var urls: Urls = randomDatum.urls
                        photoInfo!!.originalUrl = urls.regular
                        photoInfo!!.thumbnailUrl = urls.thumb
                        photoInfo!!.height = randomDatum.height
                        photoInfo!!.width = randomDatum.width
                        photolist!!.add(photoInfo!!)
                    }
                }
            }

            override fun onError(error: String?) {
                ToastUtils.showShort(error)
            }
        })
    }
}