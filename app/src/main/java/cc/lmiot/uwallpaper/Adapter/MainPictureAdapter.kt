package cc.lmiot.uwallpaper.Adapter

import android.support.v4.app.FragmentActivity
import cc.lmiot.uwallpaper.R
import com.blankj.utilcode.util.ScreenUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.facebook.drawee.view.SimpleDraweeView
import com.facebook.fresco.helper.Phoenix
import com.kc.unsplash.models.Photo

class MainPictureAdapter(val contexr: FragmentActivity?, datas: MutableList<Photo>?) : BaseQuickAdapter<Photo, BaseViewHolder>(R.layout.rv_item_main_picture,datas) {
    override fun convert(helper: BaseViewHolder?, item: Photo?) {
        item?:return
        helper?:return
        val url= item.urls.regular

        val img = helper.getView<SimpleDraweeView>(R.id.img_maindata)
        Phoenix.with(img)
                .setWidth(ScreenUtils.getScreenWidth() / 2)
                .setHeight(ScreenUtils.getScreenWidth() / 2 * item.height / item.width)
                .load(url)

    }

}