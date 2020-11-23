package com.example.todoplusminus.vm


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.todoplusminus.entities.PlanData
import com.example.todoplusminus.repository.PlannerRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * planner의 모든 비즈니스 로직을 담당하는 viewModel
 *
 * 기본적으로 conductor를 사용하면 view의 state를 모두 저장해주기 때문에 AAC ViewModel()을 굳이 사용할 필요는 없지만
 * 이번 프로젝트에서는 livedata를 사용했기 때문에 참조 대상인 controller가 deActive 상황일 때 livedata에 의해 controller가가 향받게 된다.
 *  그래서 viewModel()을 사용한다.
 * */
class PlannerViewModel(private val repository: PlannerRepository) : ViewModel() {

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
