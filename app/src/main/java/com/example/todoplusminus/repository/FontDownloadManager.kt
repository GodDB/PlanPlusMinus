package com.example.todoplusminus.repository

import android.content.Context
import android.graphics.Typeface
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import android.widget.Toast
import androidx.core.provider.FontRequest
import androidx.core.provider.FontsContractCompat
import com.example.todoplusminus.AppConfig
import com.example.todoplusminus.R

class FontDownloadManager(private val mContext : Context) {

    private var mHandler: Handler? = null

    companion object {
        private const val FONT_AUTHORITY = "com.google.android.gms.fonts"
        private const val FONT_PACKAGE = "com.google.android.gms"
        private const val FONT_THREAD_NAME = "fonts"


        const val FONT_BASIC = "Roboto"
        const val FONT_SINGLE_DAY = "Single Day"
        const val FONT_POOR_STORY = "Poor Stroy"
        const val FONT_YEON_SUNG = "Yeon Sung"
        const val FONT_CUTE_FONT = "Cute Font"
        const val FONT_JUA = "Jua"
        const val FONT_GUGI = "Gugi"
        const val FONT_DO_HYEON = "Do Hyeon"
        const val FONT_NANUM_PEN_SCRIPT = "Nanum Pen Script"
        const val FONT_NANUM_GOTHIC = "Nanum Gothic"
        const val FONT_HI_MELODY = "Hi Melody"
        const val FONT_GAMJA_FLOWER = "Gamja Flower"
        const val FONT_GAEGU = "Gaegu"
        const val FONT_SUNFLOWER = "Sunflower"
    }

    fun downloadFont(fontName: String, onSuccess: (Typeface) -> Unit, onFail: () -> Unit) {
        val request = FontRequest(
            FONT_AUTHORITY,
            FONT_PACKAGE,
            fontName,
            R.array.com_google_android_gms_fonts_certs
        )

        val callback = object : FontsContractCompat.FontRequestCallback() {
            override fun onTypefaceRetrieved(typeface: Typeface) {
                AppConfig.font = typeface
                onSuccess(typeface)
            }

            override fun onTypefaceRequestFailed(reason: Int) {
                Toast.makeText(
                    mContext,
                    mContext.getString(R.string.failed_to_download_font),
                    Toast.LENGTH_LONG
                ).show()
                onFail()
            }
        }

        FontsContractCompat.requestFont(
            mContext,
            request,
            callback,
            getHandlerFontThread()
        )
    }



    private fun getHandlerFontThread(): Handler {
        if (mHandler == null) {
            val handlerThread = HandlerThread(FONT_THREAD_NAME)
            handlerThread.start()
            mHandler = Handler(handlerThread.looper)
        }
        return mHandler!!
    }

}