package com.example.todoplusminus.vm


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.todoplusminus.entities.PlanData
import com.example.todoplusminus.repository.PlannerRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PlannerViewModel(private val repository: PlannerRepository) {

    //room에서 mutablelivedata를 지원하지 않으므로 이렇게 임시로 livedata를 전달받아 mutableLivedata로 변환해서 사용한다.
    var planList: LiveData<MutableList<PlanData>> = repository.getAllPlannerData()
   /* var planList : MutableLiveData<MutableList<PlanData>> = MutableLiveData()*/

    var isEditMode : MutableLiveData<Boolean> = MutableLiveData(false)

    fun onDelete(index: Int) {
        planList.value?.get(index)?.let {
            CoroutineScope(Dispatchers.IO).launch {
                repository.deletePlannerDataById(it.id)
            }
        }
    }

    fun onInsert(){
        CoroutineScope(Dispatchers.IO).launch {
            repository.insertPlannerData(PlanData.create())
        }
    }

    fun switchEditMode(){
        if(checkWhetherEditMode()) {
            isEditMode.value = false
            updateAll()
        }else{
            isEditMode.value = true
        }
    }

    fun updateCountByIndex(count : Int, index : Int){
        planList.value!![index].updateCount(count)
        updateByIndex(index)
    }

    fun updateAll(){
        CoroutineScope(Dispatchers.IO).launch {
            repository.updatePlannerDataList(planList.value!!)
        }
    }

    fun updateByIndex(index : Int){
        CoroutineScope(Dispatchers.IO).launch {
            repository.updatePlannerData(planList.value!![index])
        }
    }

    private fun checkWhetherEditMode() : Boolean = isEditMode.value!!

}
