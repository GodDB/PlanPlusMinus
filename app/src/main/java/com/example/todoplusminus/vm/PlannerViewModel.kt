package com.example.todoplusminus.vm


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.todoplusminus.entities.PlanData
import com.example.todoplusminus.repository.PlannerRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.max

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

    var isEditMode: MutableLiveData<Boolean> = MutableLiveData(false)

    var editPlanDataID: MutableLiveData<String?> = MutableLiveData(null)

    var isShowMemoEditor : MutableLiveData<Boolean> = MutableLiveData(false)
    var isShowHistoryEditor : MutableLiveData<Boolean> = MutableLiveData(false)


    /**
     * delete가 불리는 순간은 사용자가 편집모드에서 해당 데이터를 삭제 했을 경우다
     *
     * 그러므로 그런 상황이 발생하면 전체 인덱스가 변경되므로 재정렬 후에 삭제 작업에 들어간다.
     * */
    fun onItemDelete(index: Int) {
        Log.d("godgod", "delete index   =  $index")
        val targetDeleteObj = planList.value!![index]
        onDelete(targetDeleteObj)
    }

    /*  fun onDelete(index: Int) {
          planList.value?.get(index)?.let {
              CoroutineScope(Dispatchers.IO).launch {
                  repository.deletePlannerDataById(it.id)
              }
          }

      }*/


    fun switchEditMode() {
        if (checkWhetherEditMode()) {
            isEditMode.value = false
            updateAll()
        } else {
            isEditMode.value = true
        }
    }

    fun setTargetEditDataId(id : String?){
        //edit mode가 아니라면 실행하지 않는다.
        if(!checkWhetherEditMode()) return


        if(id == null || id == "") this.editPlanDataID.value = PlanData.EMPTY_ID
        this.editPlanDataID.value = id
    }

    fun updateCountByIndex(count: Int, index: Int) {
        planList.value!![index].incrementCount(count)
        updateByIndex(index)

    }

    fun updateBgColorByIndex(bgColor: Int, index: Int) {
        planList.value!![index].bgColor = bgColor
        updateByIndex(index)
    }

    fun clearEditPlanId(){
        editPlanDataID.value = null
    }

    fun showMemo(){
        Log.d("godgod", "showMemo()")
        isShowMemoEditor.value = true
        isShowMemoEditor.value = false
    }

    fun showHistory(){
        isShowHistoryEditor.value = true
        isShowHistoryEditor.value = false
    }

    private fun updateAll() {
        CoroutineScope(Dispatchers.IO).launch {
            repository.updatePlannerDataList(planList.value!!)
        }
    }

    private fun updateByIndex(index: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            repository.updatePlannerData(planList.value!![index])
        }
    }

    private fun deleteAndUpdateAll(targetDelete: PlanData?, targetUpdate: List<PlanData>?) {
        if (targetDelete == null || targetUpdate == null) return

        CoroutineScope(Dispatchers.IO).launch {
            repository.deleteAndUpdateAll(targetDelete, targetUpdate)
        }
    }


    private fun onDelete(planData: PlanData) {
        CoroutineScope(Dispatchers.IO).launch {
            repository.deletePlannerDataById(planData.id)
        }
    }


    private fun checkWhetherEditMode(): Boolean = isEditMode.value!!

    /**
     * 사용자에게 보여지는 planData와 planData 자체의 index를 동기화 시키기 위한 함수
     *
     * 사용자에게는 index의 역순으로 보여지기 때문에 reverse 처리한다.
     * */
    private fun rearrangeIndex() {
        if (planList.value == null) return
        planList.value!!.reversed().forEachIndexed { index, planData ->
            planData.index = index
        }
    }
}
