package com.example.todoplusminus.vm

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.example.todoplusminus.AppConfig
import com.example.todoplusminus.R
import com.example.todoplusminus.base.Event
import com.example.todoplusminus.repository.SettingRepository


//todo replace dagger -> repository 접근지정자 변경 public -> private
class SettingVM(val repository: SettingRepository) {

    companion object {
        private const val TAG_PLAN_RECOMMEND_KEYWORD: Int = 1
        private const val TAG_PUSH_TO_THE_RIGHT: Int = 2
        private const val TAG_CALEDNAR_BASIC_MODE: Int = 3
        private const val TAG_FONT_STYLE: Int = 4
        private const val TAG_ALARM_AT_10: Int = 5
        private const val TAG_APP_VERSION: Int = 6
        private const val TAG_PLAN_SIZE: Int = 7
    }

    val showFontSettingEditor: MutableLiveData<Event<Boolean>> = MutableLiveData(Event(false))
    val showPlanSizeEditor : MutableLiveData<Event<Boolean>> = MutableLiveData(Event(false))

    val settingDataList: List<Pair<Int, SettingData>>
        get() {
            return listOf(
                Pair(R.string.plan, ValueEmpty()),
                Pair(R.string.plan_size, ValueString(AppConfig.planSize.toString(), TAG_PLAN_SIZE)),
                Pair(
                    R.string.plan_recommendation_keywords,
                    ValueBoolean(AppConfig.showSuggestedKeyword, TAG_PLAN_RECOMMEND_KEYWORD)
                ),
                Pair(
                    R.string.push_to_the_right,
                    ValueBoolean(AppConfig.swipeDirectionToRight, TAG_PUSH_TO_THE_RIGHT)
                ),
                Pair(R.string.plus_minus, ValueEmpty()),
                Pair(R.string.font_style, ValueString(AppConfig.fontName, TAG_FONT_STYLE)),
                Pair(R.string.alarm_at_10pm, ValueBoolean(AppConfig.enableAlarm, TAG_ALARM_AT_10)),
                Pair(R.string.support, ValueEmpty()),
                Pair(R.string.official_website, ValueString(tag = 0)),
                Pair(R.string.faq, ValueString(tag = 0)),
                Pair(R.string.share_app_with_friends, ValueString(tag = 0)),
                Pair(R.string.app_info, ValueEmpty()),
                Pair(R.string.app_version, ValueString(AppConfig.version, TAG_APP_VERSION)),
                Pair(R.string.empty, ValueEmpty()),
                Pair(R.string.to_delete_all_data, ValueString(tag = 0)),
                Pair(R.string.empty, ValueEmpty())
            )
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
        }
    }

    private fun showFontSettingEditor() {
        this.showFontSettingEditor.value = Event(true)
    }

    private fun showPlanSizeEditor(){
        this.showPlanSizeEditor.value = Event(true)
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
        repository.setEnableAlarm(value)
    }


}

sealed class SettingData()

data class ValueString(val value: String = "", val tag: Int) : SettingData()
data class ValueBoolean(val value: Boolean, val tag: Int) : SettingData()
data class ValueEmpty(val value: Nothing? = null) : SettingData()
