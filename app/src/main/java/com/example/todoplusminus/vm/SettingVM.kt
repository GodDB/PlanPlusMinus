package com.example.todoplusminus.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData

class SettingVM {

    val settingTextIdList : LiveData<List<Int>> = MutableLiveData(
        listOf(1,2,34,5,6,7)
    )


}