package com.example.todoplusminus.vm

import androidx.lifecycle.LiveData
import com.example.todoplusminus.entities.PlanMemo
import com.example.todoplusminus.repository.PlannerRepository
import com.example.todoplusminus.util.TimeProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PlanMemoVM(private val repository : PlannerRepository) {

    var memoData : PlanMemo = repository.getMemoByDate(TimeProvider.getCurDate()).value ?: PlanMemo.create()

    fun onDone(){
        CoroutineScope(Dispatchers.IO).launch {
            repository.insertPlanMemo(memoData)
        }
    }

    fun onCancel(){
    }


}