package com.example.todoplusminus.data.source.remote

import android.content.Context
import android.graphics.Typeface
import android.os.Handler
import android.os.HandlerThread
import android.widget.Toast
import androidx.core.provider.FontRequest
import androidx.core.provider.FontsContractCompat
import com.example.todoplusminus.AppConfig
import com.example.todoplusminus.R
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class FontDownloadManager(private val mContext : Context) {

    private var mHandler: Handler? = null

    companion object {
        private const val FONT_AUTHORITY = "com.google.android.gms.fonts"
        private const val FONT_PACKAGE = "com.google.android.gms"
        private const val FONT_THREAD_NAME = "fonts"

        const val FONT_EAST_SEA_DOKDO = "East Sea Dokdo"
        const val FONT_NANUM_GOTHIC = "Nanum Gothic"
        const val FONT_YEON_SUNG = "Yeon Sung"
        const val FONT_NANUM_MYEONGJO = "Nanum Myeongjo"
        const val FONT_GOTHIC_A1 = "Gothic A1"
        const val FONT_NANUM_PEN_SCRIPT = "Nanum Pen Script"
        const val FONT_DO_HYEON = "Do Hyeon"
        const val FONT_BLACK_HAN_SANS = "Black Han Sans"
        const val FONT_NANUM_BRUSH_SCRIPT = "Nanum Brush Script"
        const val FONT_GAEGU = "Gaegu"
        const val FONT_GUGI = "Gugi"
        const val FONT_JUA = "Jua"
        const val FONT_HI_MELODY = "Hi Melody"
        const val FONT_DOKDO = "Dokdo"
        const val FONT_GAMJA_FLOWER = "Gamja Flower"
        const val FONT_SONG_MYUNG = "Song Myung"
        const val FONT_STYLISH = "Stylish"
        const val FONT_CUTE_FONT = "Cute Font"
        const val FONT_POOR_STORY = "Poor Story"
        const val FONT_BLACK_AND_WHITE_PICTURE = "Black And White Picture"
        const val FONT_KIRANG_HAERANG = "Kirang Haerang"
        const val FONT_SINGLE_DAY = "Single Day"
    }

    suspend fun downloadFont(fontName: String) : Typeface? {
        val request = FontRequest(
            FONT_AUTHORITY,
            FONT_PACKAGE,
            fontName,
            R.array.com_google_android_gms_fonts_certs
        )

        return suspendCoroutine<Typeface?> { contination ->
            val callback = object : FontsContractCompat.FontRequestCallback() {
                override fun onTypefaceRetrieved(typeface: Typeface) {
                    AppConfig.font = typeface
                    contination.resume(typeface)
                }

                override fun onTypefaceRequestFailed(reason: Int) {
                    Toast.makeText(
                        mContext,
                        mContext.getString(R.string.failed_to_download_font),
                        Toast.LENGTH_LONG
                    ).show()
                    contination.resume(null)
                }
            }

            FontsContractCompat.requestFont(
                mContext,
                request,
                callback,
                getHandlerFontThread()
            )
        }
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