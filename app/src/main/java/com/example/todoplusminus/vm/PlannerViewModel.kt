package com.example.todoplusminus.vm


import androidx.lifecycle.*
import com.example.todoplusminus.PMCoroutineSpecification
import com.example.todoplusminus.base.Event
import com.example.todoplusminus.entities.PlanData
import com.example.todoplusminus.entities.PlanMemo
import com.example.todoplusminus.entities.PlanProject
import com.example.todoplusminus.repository.IPlannerRepository
import com.example.todoplusminus.util.TimeHelper
import kotlinx.coroutines.*
import java.time.LocalDate

/**
 * planner의 모든 비즈니스 로직을 담당하는 viewModel
 *
 * 기본적으로 conductor를 사용하면 view의 state를 모두 저장해주기 때문에 AAC ViewModel()을 굳이 사용할 필요는 없지만
 * 이번 프로젝트에서는 livedata를 사용했기 때문에 참조 대상인 controller가 deActive 상황일 때 livedata에 의해 controller가가 향받게 된다.
 *  그래서 viewModel()을 사용한다.
 * */
class PlannerViewModel(
    private val repository: IPlannerRepository,
    private val dispatcher: CoroutineDispatcher = PMCoroutineSpecification.IO_DISPATCHER
) : ViewModel() {

    init {
        viewModelScope.launch (dispatcher) {
            repository.refreshPlannerData(LocalDate.now())
        }
    }

    //https://developer.android.com/topic/libraries/architecture/coroutines?hl=ko
    val planProject: LiveData<PlanProject> = MediatorLiveData<PlanProject>().apply {
        val data: LiveData<MutableList<PlanData>> = repository.getAllPlanDataByDate(TimeHelper.getCurDate())
        addSource(data) {
            this.value = PlanProject.create(it)
        }
    }

    val planMemo: LiveData<PlanMemo> = repository.getMemoByDate(TimeHelper.getCurDate())

    val isEditMode: MutableLiveData<Boolean> = MutableLiveData(false)

    val showEditPlanDataID: MutableLiveData<Event<String>?> = MutableLiveData()

    val showMemoEditor: MutableLiveData<Event<Boolean>> = MutableLiveData(Event(false))

    val showHistoryId: MutableLiveData<Event<String>> = MutableLiveData(Event(""))

    val showCalendar : MutableLiveData<Event<Boolean>> = MutableLiveData(Event(false))

    fun onItemDelete(index: Int) {
        val targetDeleteObj = planProject.value?.getPlanDataByIndex(index)

        if (targetDeleteObj != null) onDelete(targetDeleteObj)
    }

    fun switchEditMode() {
        if (checkWhetherEditMode()) {
            isEditMode.value = false
            updateAll()
            return
        }

        isEditMode.value = true
    }

    fun onItemClick(id: String?) {
        //editmode면 item클릭 시에 수정화면이 등장한다.
        if (checkWhetherEditMode()) return showEditEditor(id)
        //editmode가 아니라면 history화면이 등장한다.
        showHistory(id)
    }

    fun showEditEditor(id: String?) {
        //edit mode가 아니라면 실행하지 않는다.
        if (!checkWhetherEditMode()) return

        if (checkIdEmpty(id)) {
            this.showEditPlanDataID.value = Event(PlanData.EMPTY_ID)
            return
        }

        this.showEditPlanDataID.value = Event(id!!)
    }

    fun showCalendar(){
        if(this.showCalendar.value!!.peekContent()){
            this.showCalendar.value = Event(false)
            return
        }

        this.showCalendar.value = Event(true)
    }

    fun updateCountByIndex(count: Int, index: Int) {
        planProject.value?.increasePlanDataCountByIndex(count, index)
        updateByIndex(index)

    }

    fun showMemo() {
        showMemoEditor.value = Event(true)
    }

    fun showHistory(id: String?) {
        if (checkIdEmpty(id)) return
        showHistoryId.value = Event(id!!)
    }

    private fun updateAll() {
        viewModelScope.launch(dispatcher) {
            repository.updatePlannerDataList(planProject.value!!.getPlanDataList())
        }
    }

    private fun updateByIndex(index: Int) {
        viewModelScope.launch(dispatcher) {
            repository.updatePlannerData(planProject.value!!.getPlanDataByIndex(index))
        }
    }


    private fun onDelete(planData: PlanData) {
        viewModelScope.launch(dispatcher) {
            repository.deletePlannerDataById(planData.id)
        }
    }

    private fun checkWhetherEditMode(): Boolean = isEditMode.value!!

    private fun checkIdEmpty(id: String?): Boolean = (id == "" || id == null)

}

