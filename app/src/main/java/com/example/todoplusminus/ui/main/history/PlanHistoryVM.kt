package com.example.todoplusminus.ui.main.history

import android.graphics.Typeface
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.switchMap
import com.example.todoplusminus.AppConfig
import com.example.todoplusminus.util.livedata.Event
import com.example.todoplusminus.data.entities.PlanProject
import com.example.todoplusminus.data.repository.IPlannerRepository
import javax.inject.Inject

class PlanHistoryVM @Inject constructor(val targetId: String, private val repository: IPlannerRepository) {

    val font: Typeface?
        get() = AppConfig.font

    private val _mPlanProject: LiveData<PlanProject> =
        repository.getPlanProjectById(targetId)
            .asLiveData()

    val mHistoryTitle: LiveData<String> = _mPlanProject.switchMap {
        MutableLiveData(it.getPlanDataById(targetId).title)
    }

    val mTabColor: LiveData<Int> = _mPlanProject.switchMap {
        MutableLiveData(it.getPlanDataBgColorByIndex(0))
    }

    var wantEditorClose: MutableLiveData<Event<Boolean>> =
        MutableLiveData(Event(false))


    fun onCancel() {
        wantEditorClose.value =
            Event(true)
    }
}