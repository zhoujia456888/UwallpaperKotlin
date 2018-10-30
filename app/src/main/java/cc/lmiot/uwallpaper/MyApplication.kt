package cc.lmiot.uwallpaper

import android.app.Application
import android.os.Environment
import com.blankj.utilcode.util.Utils
import com.facebook.fresco.helper.Phoenix
import com.kc.unsplash.Unsplash

class MyApplication : Application() {

    var application: Application = this

    override fun onCreate() {
        super.onCreate()

        application = this

        Phoenix.init(this)//Phoenix

        Utils.init(application)//AndroidUtils

        unsplash=Unsplash(Config.applicationID)//Unsplash


    }

    companion object {
        var unsplash:Unsplash?=null
    }



}