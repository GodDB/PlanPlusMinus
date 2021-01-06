package com.example.todoplusminus.ui.setting.fontSetting

import androidx.lifecycle.MutableLiveData
import com.example.todoplusminus.AppConfig
import com.example.todoplusminus.R
import com.example.todoplusminus.data.repository.ISettingRepository
import com.example.todoplusminus.util.livedata.Event
import com.example.todoplusminus.data.source.remote.FontDownloadManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class FontSettingVM @Inject constructor(private val repository: ISettingRepository) {

    val downloadingFontName : MutableLiveData<Event<String>> = MutableLiveData()
    val allowDownload : MutableLiveData<Event<Boolean>> = MutableLiveData(
        Event(true)
    )
    val completeDownloadFontName : MutableLiveData<Event<String>> = MutableLiveData(
        Event(AppConfig.fontName)
    )
    val closeFontSettingEditor : MutableLiveData<Event<Boolean>> = MutableLiveData()

    val fontListData : List<FontItemData> = listOf(
        FontItemData(
            R.drawable.font_image_east_sea_dokdo,
            FontDownloadManager.FONT_EAST_SEA_DOKDO,
            listOf(R.string.hangul, R.string.english)
        ),
        FontItemData(
            R.drawable.font_image_nanum_gothic,
            FontDownloadManager.FONT_NANUM_GOTHIC,
            listOf(R.string.hangul, R.string.english)
        ),
        FontItemData(
            R.drawable.font_image_yeon_sung,
            FontDownloadManager.FONT_YEON_SUNG,
            listOf(R.string.hangul, R.string.english)
        ),
        FontItemData(
            R.drawable.font_image_nanum_myeongjo,
            FontDownloadManager.FONT_NANUM_MYEONGJO,
            listOf(R.string.hangul, R.string.english)
        ),
        FontItemData(
            R.drawable.font_image_gothic_a1,
            FontDownloadManager.FONT_GOTHIC_A1,
            listOf(R.string.hangul, R.string.english)
        ),
        FontItemData(
            R.drawable.font_image_nanum_pen_script,
            FontDownloadManager.FONT_NANUM_PEN_SCRIPT,
            listOf(R.string.hangul, R.string.english)
        ),
        FontItemData(
            R.drawable.font_image_do_hyeon,
            FontDownloadManager.FONT_DO_HYEON,
            listOf(R.string.hangul, R.string.english)
        ),
        FontItemData(
            R.drawable.font_image_black_han_sans,
            FontDownloadManager.FONT_BLACK_HAN_SANS,
            listOf(R.string.hangul, R.string.english)
        ),
        FontItemData(
            R.drawable.font_image_nanum_brush_script,
            FontDownloadManager.FONT_NANUM_BRUSH_SCRIPT,
            listOf(R.string.hangul, R.string.english)
        ),
        FontItemData(
            R.drawable.font_image_gaegu,
            FontDownloadManager.FONT_GAEGU,
            listOf(R.string.hangul, R.string.english)
        ),
        FontItemData(
            R.drawable.font_image_gugi,
            FontDownloadManager.FONT_GUGI,
            listOf(R.string.hangul, R.string.english)
        ),
        FontItemData(
            R.drawable.font_image_jua,
            FontDownloadManager.FONT_JUA,
            listOf(R.string.hangul, R.string.english)
        ),
        FontItemData(
            R.drawable.font_image_hi_melody,
            FontDownloadManager.FONT_HI_MELODY,
            listOf(R.string.hangul, R.string.english)
        ),
        FontItemData(
            R.drawable.font_image_dokdo,
            FontDownloadManager.FONT_DOKDO,
            listOf(R.string.hangul, R.string.english)
        ),
        FontItemData(
            R.drawable.font_image_gamja_flower,
            FontDownloadManager.FONT_GAMJA_FLOWER,
            listOf(R.string.hangul, R.string.english)
        ),
        FontItemData(
            R.drawable.font_image_song_myung,
            FontDownloadManager.FONT_SONG_MYUNG,
            listOf(R.string.hangul, R.string.english)
        ),
        FontItemData(
            R.drawable.font_image_stylish,
            FontDownloadManager.FONT_STYLISH,
            listOf(R.string.hangul, R.string.english)
        ),
        FontItemData(
            R.drawable.font_image_cute_font,
            FontDownloadManager.FONT_CUTE_FONT,
            listOf(R.string.hangul, R.string.english)
        ),
        FontItemData(
            R.drawable.font_image_poor_story,
            FontDownloadManager.FONT_POOR_STORY,
            listOf(R.string.hangul, R.string.english)
        ),
        FontItemData(
            R.drawable.font_image_black_and_white_picture,
            FontDownloadManager.FONT_BLACK_AND_WHITE_PICTURE,
            listOf(R.string.hangul, R.string.english)
        )
        ,
        FontItemData(
            R.drawable.font_image_kirang_haerang,
            FontDownloadManager.FONT_KIRANG_HAERANG,
            listOf(R.string.hangul, R.string.english)
        ),
        FontItemData(
            R.drawable.font_image_single_day,
            FontDownloadManager.FONT_SINGLE_DAY,
            listOf(R.string.hangul, R.string.english)
        )
    )

    fun onFontSelect(fontName : String){
        banOtherFontDownload()
        startLoading(fontName)
        getFont(fontName)
    }

    fun onClose(){
        this.closeFontSettingEditor.value =
            Event(true)
    }

    private fun getFont(fontName: String){
        CoroutineScope(Dispatchers.Main).launch {
            val font = repository.getFont(fontName) ?: return@launch onError()
            onComplete(fontName)
        }
    }

    private fun onComplete(fontName: String){
        setDownloadCompleteFontName(fontName)
        allowOtherFontDownload()
        stopLoading()
    }

    private fun onError(){
        allowOtherFontDownload()
        stopLoading()
    }

    private fun startLoading(fontName : String){
        downloadingFontName.value =
            Event(fontName)
    }

    private fun stopLoading() {
        downloadingFontName.value =
            Event("")
    }

    private fun banOtherFontDownload(){
        allowDownload.value =
            Event(false)
    }

    private fun allowOtherFontDownload(){
        allowDownload.value = Event(true)
    }

    private fun setDownloadCompleteFontName(fontName : String){
        this.completeDownloadFontName.value =
            Event(fontName)
    }
}

data class FontItemData(val fontImageId : Int, val fontName : String, val fontType : List<Int>)