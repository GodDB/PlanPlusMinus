package com.example.todoplusminus.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.example.todoplusminus.base.Event
import com.example.todoplusminus.entities.PlanData
import com.example.todoplusminus.entities.PlanProject
import com.example.todoplusminus.repository.PlannerRepository

class PlanHistoryVM(private val targetId : String, private val repository : PlannerRepository) {

    val planProject: LiveData<PlanProject> = MediatorLiveData<PlanProject>().apply {
        val data : LiveData<MutableList<PlanData>> = repository.getAllPlanDataById(targetId)
        this.value = PlanProject.create(null)
        addSource(data){ data ->
            this.value = PlanProject.create(data ?: return@addSource)
        }
    }

    var wantEditorClose
            : MutableLiveData<Event<Boolean>> = MutableLiveData(Event(false))


    fun onCancel(){
        wantEditorClose.value = Event(true)
    }
}