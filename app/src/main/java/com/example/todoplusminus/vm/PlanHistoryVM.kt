package com.example.todoplusminus.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.example.todoplusminus.base.Event
import com.example.todoplusminus.entities.PlanData
import com.example.todoplusminus.entities.PlanProject
import com.example.todoplusminus.repository.PlannerRepository
import com.example.todoplusminus.util.TimeHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

class PlanHistoryVM(val targetId: String, private val repository: PlannerRepository) {

    /*val planProject: LiveData<PlanProject> = MediatorLiveData<PlanProject>().apply {
        val data : LiveData<MutableList<PlanData>> = repository.getAllPlanDataById(targetId)
        this.value = PlanProject.create(null)
        addSource(data){ data ->
            this.value = PlanProject.create(data ?: return@addSource)
        }
    }*/

    private val _mPlanProject: PlanProject =
        PlanProject.create(runBlocking(Dispatchers.IO) { repository.getAllPlanDataById(targetId) })

    val mHistoryTitle: LiveData<String>
        get() = MutableLiveData(_mPlanProject.getPlanDataById(targetId).title)

    val mTabColor: LiveData<Int>
        get() = MutableLiveData(_mPlanProject.getPlanDataBgColorByIndex(0))

    var wantEditorClose
            : MutableLiveData<Event<Boolean>> = MutableLiveData(Event(false))


    fun onCancel() {
        wantEditorClose.value = Event(true)
    }
}