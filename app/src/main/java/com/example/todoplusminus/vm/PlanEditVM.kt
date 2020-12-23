package com.example.todoplusminus.vm

import android.graphics.Typeface
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import com.example.todoplusminus.AppConfig
import com.example.todoplusminus.base.Event
import com.example.todoplusminus.entities.PlanData
import com.example.todoplusminus.repository.IPlannerRepository
import com.example.todoplusminus.util.ColorManager
import kotlinx.coroutines.*

class PlanEditVM(
    private val repository: IPlannerRepository
) {

    val font: Typeface?
        get() = AppConfig.font

    var mId: String = PlanData.EMPTY_ID
    var mBgColor: MutableLiveData<Int> = MutableLiveData(ColorManager.getRandomColor())
    var mTitle: MutableLiveData<String> = MutableLiveData("")

    private var mLastestIndex: LiveData<Int> = repository.getLastestIndex().asLiveData()


    /** edit 작업이 끝났는지(done or back or cancel)를 체크한다.
     * */
    var isEditEnd: MutableLiveData<Event<Boolean>> = MutableLiveData(Event(false))


    fun setId(id: String) {
        mId = id

        if (!checkIsEmptyId())
            CoroutineScope(Dispatchers.Main).launch {
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
            this.index = mLastestIndex.value?.plus(1) ?: 0
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
        CoroutineScope(Dispatchers.Main).launch {
            mTitle.value = title
        }
    }

    fun setBgColor(bgColor: Int) {
        //메인 스레드에서 작동시킨다.
        CoroutineScope(Dispatchers.Main).launch {
            mBgColor.value = bgColor
        }
    }

    private fun updateTitleBgById(id: String, title: String, bgColor: Int) {
        CoroutineScope(Dispatchers.Main).launch {
            repository.updateTitleBgById(id, title, bgColor)
        }
    }

    private fun insertData(planData: PlanData) {
        CoroutineScope(Dispatchers.Main).launch {
            repository.insertPlannerData(planData)
        }
    }

    private fun checkIsEmptyContent(): Boolean =
        (mBgColor.value == 0 || mTitle.value == "" || mTitle.value == null)

    private fun checkIsEmptyId(): Boolean =
        mId == PlanData.EMPTY_ID || mId == ""
}

