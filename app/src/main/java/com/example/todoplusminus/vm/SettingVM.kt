package com.example.todoplusminus.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.todoplusminus.R
import com.example.todoplusminus.repository.SettingRepository

class SettingVM(repository : SettingRepository) {

    val settingDataList : MutableLiveData<List<Pair<Int, SettingData>>> = MutableLiveData(
        listOf(
            Pair(R.string.year, ValueEmpty()),
            Pair(R.string.year, ValueBoolean(true)),
            Pair(R.string.year, ValueBoolean(true)),
            Pair(R.string.year, ValueEmpty()),
            Pair(R.string.year, ValueBoolean(true)),
            Pair(R.string.year, ValueString("갓갓갓")),
            Pair(R.string.year, ValueBoolean(true))
        )
    )


}
sealed class SettingData()

data class ValueString(val value: String) : SettingData()
data class ValueBoolean(val value : Boolean) : SettingData()
data class ValueEmpty(val value: Nothing? = null) : SettingData()
