package com.example.todoplusminus.vm

import androidx.lifecycle.MutableLiveData
import com.example.todoplusminus.base.Event
import com.example.todoplusminus.repository.PlannerRepository

class PlanHistoryVM(private val mRepository: PlannerRepository) {

    var wantEditorClose
            : MutableLiveData<Event<Boolean>> = MutableLiveData(Event(false))


    fun onCancel(){
        wantEditorClose.value = Event(true)
    }
}