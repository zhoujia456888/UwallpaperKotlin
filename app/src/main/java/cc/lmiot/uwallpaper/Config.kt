package cc.lmiot.uwallpaper

import android.os.Environment

class Config {

    companion object {
        val applicationID="24d27e40710fcc0e45a3922a7917c70f282ceccad10986193d1b2288f6ae8032"
        val secret="2195a2c63b57eeb866c4c5d9fe80c777cfef2b857fdf014f940f66cba0450d98"

        val FilePath= Environment.getExternalStorageDirectory().getAbsolutePath() + "/Uwallpaper/"
    }



}