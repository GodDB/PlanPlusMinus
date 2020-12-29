package com.example.todoplusminus.ui.setting.fontSetting

import androidx.lifecycle.MutableLiveData
import com.example.todoplusminus.AppConfig
import com.example.todoplusminus.R
import com.example.todoplusminus.util.livedata.Event
import com.example.todoplusminus.data.source.remote.FontDownloadManager
import com.example.todoplusminus.data.repository.SettingRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FontSettingVM (private val repository: SettingRepository) {

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
            R.drawable.cute_font,
            FontDownloadManager.FONT_CUTE_FONT,
            listOf(R.string.hangul, R.string.english)
        ),
        FontItemData(
            R.drawable.do_hyeon,
            FontDownloadManager.FONT_DO_HYEON,
            listOf(R.string.hangul, R.string.english)
        ),
        FontItemData(
            R.drawable.gaegu,
            FontDownloadManager.FONT_GAEGU,
            listOf(R.string.hangul, R.string.english)
        ),
        FontItemData(
            R.drawable.gamja_flower,
            FontDownloadManager.FONT_GAMJA_FLOWER,
            listOf(R.string.hangul, R.string.english)
        ),
        FontItemData(
            R.drawable.hi_melody,
            FontDownloadManager.FONT_HI_MELODY,
            listOf(R.string.hangul, R.string.english)
        ),
        FontItemData(
            R.drawable.jua,
            FontDownloadManager.FONT_JUA,
            listOf(R.string.hangul, R.string.english)
        ),
        FontItemData(
            R.drawable.nanum_gothic,
            FontDownloadManager.FONT_NANUM_GOTHIC,
            listOf(R.string.hangul, R.string.english)
        ),
        FontItemData(
            R.drawable.nanum_pen_script,
            FontDownloadManager.FONT_NANUM_PEN_SCRIPT,
            listOf(R.string.hangul, R.string.english)
        ),
        FontItemData(
            R.drawable.poor_story,
            FontDownloadManager.FONT_POOR_STORY,
            listOf(R.string.hangul, R.string.english)
        ),
        FontItemData(
            R.drawable.single_day,
            FontDownloadManager.FONT_SINGLE_DAY,
            listOf(R.string.hangul, R.string.english)
        ),
        FontItemData(
            R.drawable.sunflower,
            FontDownloadManager.FONT_SUNFLOWER,
            listOf(R.string.hangul, R.string.english)
        ),
        FontItemData(
            R.drawable.yeon_sung,
            FontDownloadManager.FONT_YEON_SUNG,
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