package com.example.todoplusminus.vm


import androidx.lifecycle.*
import com.example.todoplusminus.PMCoroutineSpecification
import com.example.todoplusminus.base.Event
import com.example.todoplusminus.entities.PlanData
import com.example.todoplusminus.entities.PlanMemo
import com.example.todoplusminus.entities.PlanProject
import com.example.todoplusminus.repository.IPlannerRepository
import com.example.todoplusminus.util.DateHelper
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
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
        viewModelScope.launch(dispatcher) {
            repository.refreshPlannerData(LocalDate.now())
        }
    }


    private val _targetDate: MutableLiveData<LocalDate> = MutableLiveData(DateHelper.getCurDate())

    private val _allDatePlanData = repository.getAllPlannerData()

    private val _planProject: LiveData<PlanProject> = Transformations.switchMap(_allDatePlanData) {
        MutableLiveData(PlanProject.create(it))
    }

    val targetDatePlanProject : CombinedLiveData<LocalDate,PlanProject, PlanProject> = CombinedLiveData(_targetDate, _planProject){ a, b ->
        PlanProject.create(b.getPlanDataListByDate(a))
    }

    val planMemo: LiveData<PlanMemo> = Transformations.switchMap(_targetDate) {
        repository.getMemoByDate(it)

    }

    val isEditMode: MutableLiveData<Boolean> = MutableLiveData(false)

    val showEditPlanDataID: MutableLiveData<Event<String>?> = MutableLiveData()

    val showMemoEditor: MutableLiveData<Event<Boolean>> = MutableLiveData(Event(false))

    val showHistoryId: MutableLiveData<Event<String>> = MutableLiveData(Event(""))

    val showCalendar: MutableLiveData<Event<Boolean>> = MutableLiveData(Event(false))

    fun onItemDelete(index: Int) {
        val targetDeleteObj = targetDatePlanProject.value?.getPlanDataByIndex(index)

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

    fun showCalendar() {
        if (this.showCalendar.value!!.peekContent()) {
            this.showCalendar.value = Event(false)
            return
        }

        this.showCalendar.value = Event(true)
    }

    fun updateCountByIndex(count: Int, index: Int) {
        targetDatePlanProject.value?.increasePlanDataCountByIndex(count, index)
        updateByIndex(index)

    }

    fun showMemo() {
        showMemoEditor.value = Event(true)
    }

    fun showHistory(id: String?) {
        if (checkIdEmpty(id)) return
        showHistoryId.value = Event(id!!)
    }

    fun changeDate(year: Int, month: Int, day: Int) {
        val date = LocalDate.of(year, month, day)
        _targetDate.value = date
    }

    private fun updateAll() {
        viewModelScope.launch(dispatcher) {
            repository.updatePlannerDataList(targetDatePlanProject.value!!.getPlanDataList())
        }
    }

    private fun updateByIndex(index: Int) {
        viewModelScope.launch(dispatcher) {
            repository.updatePlannerData(targetDatePlanProject.value!!.getPlanDataByIndex(index))
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

class CombinedLiveData<A, B, C>(private val liveData1: LiveData<A>, private val liveData2: LiveData<B>, private val action : (A, B) -> C) :
    MediatorLiveData<C>() {

    private var a : A? = null
    private var b : B? = null

    init {
        addSource(liveData1) { a ->
            this.a = a
            if(b != null) this.value = action(a, b!!)
        }

        addSource(liveData2) { b ->
            this.b = b
            if(a != null) this.value = action(a!!, b)
        }

    }
}




