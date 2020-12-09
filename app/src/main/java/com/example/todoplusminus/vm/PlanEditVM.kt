package com.example.todoplusminus.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoplusminus.PMCoroutineSpecification
import com.example.todoplusminus.base.Event
import com.example.todoplusminus.entities.PlanData
import com.example.todoplusminus.repository.IPlannerRepository
import com.example.todoplusminus.util.ColorManager
import kotlinx.coroutines.*

class PlanEditVM(
    private val repository: IPlannerRepository,
    private val dispatcher: CoroutineDispatcher = PMCoroutineSpecification.IO_DISPATCHER
) : ViewModel() {

    var mId: String = PlanData.EMPTY_ID
    var mBgColor: MutableLiveData<Int> = MutableLiveData(ColorManager.getRandomColor())
    var mTitle: MutableLiveData<String> = MutableLiveData("")

    private var mLastIndex: Int = runBlocking(Dispatchers.IO) {
        repository.getLastIndex()
    }

    /** edit 작업이 끝났는지(done or back or cancel)를 체크한다.
     * */
    var isEditEnd: MutableLiveData<Event<Boolean>> = MutableLiveData(Event(false))


    fun setData(id: String) {
        mId = id

        if (!checkIsEmptyId())
            viewModelScope.launch(dispatcher) {
                val data = repository.getPlannerDataById(id)
                setTitle(data.title)
                setBgColor(data.bgColor)
            }
    }


    fun onComplete() {
        if (checkIsEmptyId()) return onCreateItem()
        onUpdatePlanData()
    }

    private fun onCreateItem() {
        if (!checkIsEmptyId() && checkIsEmptyContent()) return

        val newData = PlanData.create().apply {
            this.index = mLastIndex + 1
            this.title = mTitle.value ?: ""
            this.bgColor = mBgColor.value ?: PlanData.DEFAULT_BG_COLOR
        }

        insertData(newData)
        onEditEnd()
    }


    fun onEditEnd() {
        isEditEnd.value = Event(true)
    }

    private fun onUpdatePlanData() {
        if (!checkIsEmptyId() || !checkIsEmptyContent()) {
            updateTitleBgById(mId, mTitle.value!!, mBgColor.value!!)

            onEditEnd()
        }
    }

    fun setTitle(title: String) {
        //메인 스레드에서 작동시킨다.
        viewModelScope.launch {
            mTitle.value = title
        }
    }

    fun setBgColor(bgColor: Int) {
        //메인 스레드에서 작동시킨다.
        viewModelScope.launch {
            mBgColor.value = bgColor
        }
    }

    private fun updateTitleBgById(id: String, title: String, bgColor: Int) {
        viewModelScope.launch(dispatcher) {
            repository.updateTitleBgById(id, title, bgColor)
        }
    }

    private fun insertData(planData: PlanData) {
        viewModelScope.launch(dispatcher) {
            repository.insertPlannerData(planData)
        }
    }

    private fun checkIsEmptyContent(): Boolean =
        (mBgColor.value == 0 || mTitle.value == "" || mTitle.value == null)

    private fun checkIsEmptyId(): Boolean =
        mId == PlanData.EMPTY_ID || mId == ""
}

