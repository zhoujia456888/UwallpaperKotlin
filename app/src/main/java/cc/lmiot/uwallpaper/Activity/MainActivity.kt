package cc.lmiot.uwallpaper.Activity

import android.os.Bundle
import android.support.v4.view.ViewPager
import android.view.KeyEvent
import cc.lmiot.uwallpaper.Fragment.LatestPicFragment
import cc.lmiot.uwallpaper.Fragment.RandomPicFragment
import cc.lmiot.uwallpaper.R
import com.blankj.utilcode.util.ToastUtils
import com.kc.unsplash.Unsplash
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {
    companion object {
        var currentItem: Int = 0
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initSmartTab()
    }

    fun initSmartTab() {
        var adapter: FragmentPagerItemAdapter = FragmentPagerItemAdapter(supportFragmentManager, FragmentPagerItems.with(this)
                .add(R.string.title_latest, LatestPicFragment().javaClass)
                .add(R.string.title_random, RandomPicFragment().javaClass)
                .create())


        viewpager.adapter = adapter
        viewpagertab.setViewPager(viewpager)
        viewpager.setOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                currentItem = position
            }
        })
    }


    //按两下退出程序
    val exitTime: Long = 0

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (System.currentTimeMillis() - exitTime > 2000) {
                ToastUtils.showShort("再按一次退出程序")
            } else {
                finish()
                System.exit(0)
            }
        }

        return super.onKeyDown(keyCode, event)
    }

}


