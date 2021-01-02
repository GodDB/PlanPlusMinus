package com.example.todoplusminus.ui.main.history

import android.graphics.Typeface
import androidx.lifecycle.*
import com.example.todoplusminus.AppConfig
import com.example.todoplusminus.R
import com.example.todoplusminus.data.entities.BaseID
import com.example.todoplusminus.util.livedata.Event
import com.example.todoplusminus.data.entities.PlanProject
import com.example.todoplusminus.data.repository.IPlannerRepository
import com.example.todoplusminus.ui.setting.ValueData
import com.example.todoplusminus.ui.setting.ValueEmpty
import com.example.todoplusminus.ui.setting.ValueString
import com.example.todoplusminus.util.ColorID
import com.example.todoplusminus.util.StringID
import javax.inject.Inject

class PlanHistoryVM @Inject constructor(val targetId: BaseID, private val repository: IPlannerRepository) {

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

    val wantEditorClose: MutableLiveData<Event<Boolean>> =
        MutableLiveData(Event(false))


    //test alarm 화면 다 완성되면 시간, repeat day값을 담아서 호출해야함
    val showAlarmSelector : MutableLiveData<Event<Boolean>> = MutableLiveData()

    fun showAlarmSelector(){
        this.showAlarmSelector.value = Event(true)
    }



    //alarm 리스트에 기본적으로 들어가는 속성
    // title과 알람 설정화면 실행하는 뷰 데이터가 포함.
    val defaultAlarmData : LiveData<List<Triple<List<StringID>, ColorID, ValueData>>> = liveData {
        val data = mutableListOf<Triple<List<StringID>, ColorID, ValueData>>().apply {
            add(Triple(listOf(StringID(R.string.alarm)), ColorID(R.color.black), ValueEmpty()))
            add(Triple(listOf(StringID(R.string.add_new_alarm)), ColorID(R.color.sky_blue), ValueString(tag = 0)))
        }.toList()

        emit(data)
    }


    fun onCancel() {
        wantEditorClose.value =
            Event(true)
    }
}