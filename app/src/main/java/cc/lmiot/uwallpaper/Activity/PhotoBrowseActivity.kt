package cc.lmiot.uwallpaper.Activity

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.view.View
import cc.lmiot.uwallpaper.Config.Companion.FilePath
import cc.lmiot.uwallpaper.Fragment.LatestPicFragment
import cc.lmiot.uwallpaper.Fragment.RandomPicFragment
import cc.lmiot.uwallpaper.R
import com.afollestad.materialdialogs.MaterialDialog
import com.anbetter.log.MLog
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.facebook.fresco.helper.Phoenix
import com.facebook.fresco.helper.listener.IDownloadResult
import com.facebook.fresco.helper.listener.IResult
import com.facebook.fresco.helper.photoview.PictureBrowseActivity
import com.kc.unsplash.models.Photo
import kotlinx.android.synthetic.main.activity_photo_browse.*
import java.io.File

class PhotoBrowseActivity : PictureBrowseActivity() {

    lateinit var loadDialog: MaterialDialog.Builder
    var photoData: MutableList<Photo>? = mutableListOf<Photo>()


    override fun getLayoutResId(): Int {
        return R.layout.activity_photo_browse
    }

    override fun setupViews() {
        super.setupViews()
        loadDialog = MaterialDialog.Builder(this)
                .content(R.string.txt_download_wait)
                .widgetColor(resources.getColor(R.color.colorAccent))
                .progress(false, 100, true)
                .autoDismiss(false)
                .canceledOnTouchOutside(false)
                .cancelable(false)

        img_download.setOnClickListener {
            var dialog: MaterialDialog = loadDialog.show()
            var filePath: String = FilePath + "Download/"
            var path1: File = File(filePath)
            if (!path1.exists()) {
                path1.mkdir()
            }


            when (MainActivity.currentItem) {
                0 -> photoData = LatestPicFragment.latestData
                1 -> photoData = RandomPicFragment.randomData
            }

            var photo: Photo = photoData!!.get(mPhotoIndex)
            var id: String = photo.id
            var url: String = photo.urls.full

            LogUtils.e(filePath + id + ".jpg")
            Phoenix.with(this)
                    .setUrl(url)
                    .setResult(object : IDownloadResult(filePath + id + ".jpg") {
                        override fun onResult(filePath: String?) {
                            dialog.dismiss()
                            LogUtils.e("filePath = $filePath")
                            ToastUtils.showShort("${getString(R.string.txt_download_succ)} $filePath")
                        }

                        override fun onProgress(progress: Int) {
                            super.onProgress(progress)
                            LogUtils.e(progress)
                            dialog.setProgress(progress - 1)
                        }

                    }).download()
        }

        img_setwallpaper.setOnClickListener {
            when (MainActivity.currentItem) {
                0 -> photoData = LatestPicFragment.latestData
                1 -> photoData = RandomPicFragment.randomData
            }

            loadDialog.progress(true, 0).progressIndeterminateStyle(true)
            var dialog: MaterialDialog = loadDialog.show()
            dialog.setContent(R.string.txt_getfullpic_wait)

            var url = photoData!!.get(mPhotoIndex).urls.full
            Phoenix.with(this)
                    .setUrl(url)
                    .setResult(object : IResult<Bitmap> {
                        override fun onResult(result: Bitmap?) {
                            dialog.dismiss()

                            var intent = Intent(Intent.ACTION_ATTACH_DATA)
                            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            intent.putExtra("mimeType", "image/*")
                            var uri = Uri.parse(MediaStore.Images.Media
                                    .insertImage(contentResolver, result, null, null))
                            intent.setData(uri)
                            startActivityForResult(intent, 1);
                        }

                    }).load()


        }


    }

    override fun onLongClick(view: View?): Boolean {
        return super.onLongClick(view)
        MLog.i("currentPosition = $currentPosition")

        val photoInfo = currentPhotoInfo
        MLog.i("current originalUrl = " + photoInfo.originalUrl)

        return super.onLongClick(view)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        LogUtils.e("requestCode:$requestCode")
        LogUtils.e("resultCode:$resultCode")
        if (resultCode == -1) {
            ToastUtils.showShort(R.string.txt_setwallpaper_succ)
        } else {
            ToastUtils.showShort(R.string.txt_cancel)
        }
    }

}


