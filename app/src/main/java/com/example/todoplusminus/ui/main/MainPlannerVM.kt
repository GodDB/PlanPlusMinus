package com.example.todoplusminus.ui.main


import android.graphics.Typeface
import androidx.lifecycle.*
import com.example.todoplusminus.AppConfig
import com.example.todoplusminus.data.entities.BaseID
import com.example.todoplusminus.util.livedata.TwoCombinedLiveData
import com.example.todoplusminus.util.livedata.Event
import com.example.todoplusminus.data.entities.PlanData
import com.example.todoplusminus.data.entities.PlanMemo
import com.example.todoplusminus.data.entities.PlanProject
import com.example.todoplusminus.data.repository.IPlannerRepository
import com.example.todoplusminus.util.DateHelper
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject


/**
 * planner의 모든 비즈니스 로직을 담당하는 viewModel
 *
 * 기본적으로 conductor를 사용하면 view의 state를 모두 저장해주기 때문에 AAC ViewModel()을 굳이 사용할 필요는 없지만
 * 이번 프로젝트에서는 livedata를 사용했기 때문에 참조 대상인 controller가 deActive 상황일 때 livedata에 의해 controller가가 향받게 된다.
 *  그래서 viewModel()을 사용한다.
 * */
class PlannerViewModel @Inject constructor(
    private val repository: IPlannerRepository
) : ViewModel() {

    init {
        viewModelScope.launch {
            repository.refreshPlannerData(LocalDate.now())
        }
    }

    val font: MutableLiveData<Typeface?> = MutableLiveData(AppConfig.font)

    //planner item의 tv의 텍스트 사이즈를 변경하기 위함
    val planSize : MutableLiveData<Int> = MutableLiveData(AppConfig.planSize)

    private val _targetDate: MutableLiveData<LocalDate> =
        MutableLiveData<LocalDate>(DateHelper.getCurDate())

    val targetDate: LiveData<LocalDate> = _targetDate.switchMap {
        MutableLiveData(it)
    }

    private var _allDatePlanData = repository.getAllPlanProject().asLiveData()

    private val _allDatePlanProject: LiveData<PlanProject> =
        Transformations.switchMap(_allDatePlanData) {
            MutableLiveData(it)
        }

    val allDatePlanData: LiveData<List<PlanData>> = Transformations.switchMap(_allDatePlanData) {
        MutableLiveData(it.getPlanDataList())
    }

    val allDatePlanMemo: LiveData<MutableList<PlanMemo>> = repository.getAllPlanMemo().asLiveData()

    val targetDatePlanProject: TwoCombinedLiveData<LocalDate, PlanProject, PlanProject> =
        TwoCombinedLiveData(
            _targetDate,
            _allDatePlanProject
        ) { a, b ->
            PlanProject.create(b.getPlanDataListByDate(a))
        }

    val targetDatePlanMemo: LiveData<PlanMemo> = Transformations.switchMap(_targetDate) {
        repository.getMemoByDate(it).asLiveData()
    }

    val isEditMode: MutableLiveData<Boolean> = MutableLiveData(false)

    val showEditPlanDataID: MutableLiveData<Event<BaseID>?> = MutableLiveData()

    val showMemoEditor: MutableLiveData<Event<Boolean>> = MutableLiveData(
        Event(false)
    )

    val showHistoryId: MutableLiveData<Event<BaseID>> = MutableLiveData(
        Event(BaseID.createEmpty())
    )

    val showCalendar: MutableLiveData<Event<Boolean>> = MutableLiveData(
        Event(false)
    )

    val showFirecrackerAnim : MutableLiveData<Event<Boolean>> = MutableLiveData(
        Event(false)
    )

    fun reload(){
        font.value = AppConfig.font
        planSize.value = AppConfig.planSize
    }

    fun onItemDelete(index: Int) {
        val targetDeleteObj = targetDatePlanProject.value?.getPlanDataByIndex(index)

        if (targetDeleteObj != null) onDelete(targetDeleteObj)
    }

    fun switchEditMode() {
        resetTargetDate()

        if (checkWhetherEditMode()) {
            isEditMode.value = false
            updateAll()
            return
        }

        isEditMode.value = true
    }


    //creteView 클릭 이벤트
    fun onCreateItemClick(){
        val id = BaseID.createEmpty()
        showEditEditor(id)
    }

    // plan list item 클릭 이벤트
    fun onItemClick(id: BaseID) {
        //editmode면 item클릭 시에 수정화면이 등장한다.
        if (checkWhetherEditMode()) return showEditEditor(id)
        //editmode가 아니라면 history화면이 등장한다.
        showHistory(id)
    }

    fun showEditEditor(id: BaseID) {
        //edit mode가 아니라면 실행하지 않는다.
        if (!checkWhetherEditMode()) return

        this.showEditPlanDataID.value =
            Event(id)
    }

    fun showCalendar() {
        if (this.showCalendar.value!!.peekContent()) {
            this.showCalendar.value =
                Event(false)
            resetTargetDate()
            return
        }

        this.showCalendar.value =
            Event(true)
    }

    fun updateCountByIndex(count: Int, index: Int) {
        targetDatePlanProject.value?.increasePlanDataCountByIndex(count, index)
        updateByIndex(index)

        //animation
        if(count>0) showFirecrackerAnim()
    }

    fun showMemo() {
        resetTargetDate()
        showMemoEditor.value =
            Event(true)
    }

    fun showHistory(id: BaseID) {
        if (checkIdEmpty(id)) return
        showHistoryId.value = Event(id)
    }

    fun changeDate(year: Int, month: Int, day: Int) {
        val date = LocalDate.of(year, month, day)
        _targetDate.value = date
    }

    fun changeDate(date: LocalDate) {
        _targetDate.value = date
    }

    private fun showFirecrackerAnim(){
        this.showFirecrackerAnim.value =
            Event(true)
    }

    private fun updateAll() {
        viewModelScope.launch {
            repository.updatePlannerDataList(targetDatePlanProject.value!!.getPlanDataList())
        }
    }

    private fun updateByIndex(index: Int) {
        viewModelScope.launch {
            repository.updatePlannerData(targetDatePlanProject.value!!.getPlanDataByIndex(index))
        }
    }


    private fun onDelete(planData: PlanData) {
        viewModelScope.launch {
            repository.deletePlannerDataById(planData.id)
        }
    }

    private fun resetTargetDate() {
        _targetDate.value = DateHelper.getCurDate()
    }

    private fun checkWhetherEditMode(): Boolean = isEditMode.value!!

    private fun checkIdEmpty(id: BaseID): Boolean = id.isEmpty()
}




