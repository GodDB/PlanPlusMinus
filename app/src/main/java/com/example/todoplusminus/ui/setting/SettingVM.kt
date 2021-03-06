package com.example.todoplusminus.ui.setting

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.todoplusminus.AppConfig
import com.example.todoplusminus.R
import com.example.todoplusminus.data.repository.ISettingRepository
import com.example.todoplusminus.util.livedata.Event
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


class SettingVM @Inject constructor(private val repository: ISettingRepository) : ViewModel() {

    companion object {
        private const val TAG_PLAN_RECOMMEND_KEYWORD: Int = 1
        private const val TAG_PUSH_TO_THE_RIGHT: Int = 2
        private const val TAG_CALEDNAR_BASIC_MODE: Int = 3
        private const val TAG_FONT_STYLE: Int = 4
        private const val TAG_ALARM_AT_10: Int = 5
        private const val TAG_APP_VERSION: Int = 6
        private const val TAG_PLAN_SIZE: Int = 7
        private const val TAG_DELETE_ALL_DATA: Int = 8
        private const val TAG_SEND_SHARE_MESSAGE : Int = 9
    }

    val showFontSettingEditor: MutableLiveData<Event<Boolean>> = MutableLiveData(
        Event(false)
    )
    val showPlanSizeEditor: MutableLiveData<Event<Boolean>> = MutableLiveData(
        Event(false)
    )
    val showWarningDialog: MutableLiveData<Event<Boolean>> = MutableLiveData(
        Event(false)
    )

    val startSendShareMessage : MutableLiveData<Event<Boolean>> = MutableLiveData()

    val valueDataList: MutableLiveData<MutableList<Triple<Int, Int, ValueData>>> = MutableLiveData()

    private val _settingDataList
        get() = mutableListOf(
            Triple(
                R.string.plan,
                R.color.black,
                ValueEmpty()
            ),
            Triple(
                R.string.plan_size,
                R.color.black,
                ValueString(
                    AppConfig.planSize.toString(),
                    TAG_PLAN_SIZE
                )
            ),
            Triple(
                R.string.plan_recommendation_keywords,
                R.color.black,
                ValueBoolean(
                    AppConfig.showSuggestedKeyword,
                    TAG_PLAN_RECOMMEND_KEYWORD
                )
            ),
            Triple(
                R.string.push_to_the_right,
                R.color.black,
                ValueBoolean(
                    AppConfig.swipeDirectionToRight,
                    TAG_PUSH_TO_THE_RIGHT
                )
            ),
            Triple(
                R.string.plus_minus,
                R.color.black,
                ValueEmpty()
            ),
            Triple(
                R.string.font_style,
                R.color.black,
                ValueString(
                    AppConfig.fontName,
                    TAG_FONT_STYLE
                )
            ),
            Triple(
                R.string.alarm_at_10pm,
                R.color.black,
                ValueBoolean(
                    AppConfig.enableAlarm,
                    TAG_ALARM_AT_10
                )
            ),
          /*  //todo 추후에 지원할 기능
          Triple(
                R.string.support,
                R.color.black,
                ValueEmpty()
            ),
            Triple(
                R.string.official_website,
                R.color.black,
                ValueString(tag = 0)
            ),
            Triple(
                R.string.faq,
                R.color.black,
                ValueString(tag = 0)
            ),
            Triple(
                R.string.share_app_with_friends,
                R.color.black,
                ValueString(tag = TAG_SEND_SHARE_MESSAGE)
            ),*/
            Triple(
                R.string.app_info,
                R.color.black,
                ValueEmpty()
            ),
            Triple(
                R.string.app_version,
                R.color.black,
                ValueString(
                    AppConfig.version,
                    TAG_APP_VERSION
                )
            ),
            Triple(
                R.string.empty,
                R.color.black,
                ValueEmpty()
            ),
            Triple(
                R.string.to_delete_all_data,
                R.color.red,
                ValueString(tag = TAG_DELETE_ALL_DATA)
            ),
            Triple(
                R.string.empty,
                R.color.black,
                ValueEmpty()
            )
        )


    fun reload() {
        valueDataList.value = _settingDataList
    }

    fun onSwitchEvent(tag: Int, value: Boolean) {
        when (tag) {
            TAG_PLAN_RECOMMEND_KEYWORD -> onShowSuggestedKeyword(value)
            TAG_PUSH_TO_THE_RIGHT -> onSwipeDirectionToRight(value)
            TAG_CALEDNAR_BASIC_MODE -> onModeShowCalendar(value)
            TAG_ALARM_AT_10 -> onAlarm(value)
        }
    }

    fun onClickEvent(tag: Int) {
        when (tag) {
            TAG_FONT_STYLE -> showFontSettingEditor()
            TAG_PLAN_SIZE -> showPlanSizeEditor()
            TAG_DELETE_ALL_DATA -> showWarningDialog()
            TAG_SEND_SHARE_MESSAGE -> sendShareMessage()
        }
    }

    fun setPlanSize(value: Int) {
        repository.setPlanSize(value)

        reload()
    }

    fun onDeleteAllData() {
        CoroutineScope(Dispatchers.Main).launch {
            repository.onDeleteAllData()
        }
    }

    private fun showWarningDialog() {
        this.showWarningDialog.value =
            Event(true)
    }

    private fun showFontSettingEditor() {
        this.showFontSettingEditor.value =
            Event(true)
    }

    private fun showPlanSizeEditor() {
        this.showPlanSizeEditor.value =
            Event(true)
    }

    private fun onShowSuggestedKeyword(value: Boolean) {
        repository.setSuggestedKeyword(value)
    }

    private fun onSwipeDirectionToRight(value: Boolean) {
        repository.setSwipeToRight(value)
    }

    private fun onModeShowCalendar(value: Boolean) {
        repository.setShowCalendar(value)
    }

    private fun onAlarm(value: Boolean) {
        repository.setEnableDefaultAlarm(value)
    }

    private fun sendShareMessage(){
        this.startSendShareMessage.value = Event(true)
    }

}

sealed class ValueData()

data class ValueString(val value: String = "", val tag: Int) : ValueData()
data class ValueBoolean(val value: Boolean, val tag: Int) : ValueData()
data class ValueEmpty(val value: Nothing? = null) : ValueData()
