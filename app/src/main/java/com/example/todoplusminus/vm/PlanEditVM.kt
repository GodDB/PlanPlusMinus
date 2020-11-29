package com.example.todoplusminus.vm

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.todoplusminus.entities.PlanData
import com.example.todoplusminus.repository.PlannerRepository
import com.example.todoplusminus.util.ColorManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class PlanEditVM(private val repository: PlannerRepository) {

    var mId: String = PlanData.EMPTY_ID
    var mBgColor: MutableLiveData<Int> = MutableLiveData(ColorManager.getRandomColor())
    var mTitle: MutableLiveData<String> = MutableLiveData("")

    private var mLastIndex: Int = runBlocking(Dispatchers.IO) {
        repository.getLastIndex()
    }

    /** edit 작업이 끝났는지(done or back or cancel)를 체크한다.
     * */
    var isEditEnd: MutableLiveData<Boolean> = MutableLiveData(false)

    /**
     * create일땐 setData를 호출하지 않는다.
     *
     * 기존 planData를 업데이트 하는 상황에서만 사용한다
     * */
    fun setData(id: String) {
        mId = id

        if (id != PlanData.EMPTY_ID) {
            CoroutineScope(Dispatchers.IO).launch {
                val data = repository.getPlannerDataById(id)
                setTitle(data.title)
                setBgColor(data.bgColor)
            }
        }
    }

    fun onComplete() {
        if (mId == PlanData.EMPTY_ID) onCreateItem()
        else onUpdatePlanData()
    }

    private fun onCreateItem() {
        if (mId == PlanData.EMPTY_ID && !checkIsEmpty()) {
            val newData = PlanData.create().apply {
                this.index = mLastIndex + 1
                this.title = mTitle.value ?: ""
                this.bgColor = mBgColor.value ?: PlanData.DEFAULT_BG_COLOR
            }
            insertData(newData)

            onEditEnd()
        }
    }

    fun onEditEnd() {
        isEditEnd.value = true
        isEditEnd.value = false
    }

    private fun onUpdatePlanData() {
        if (mId != PlanData.EMPTY_ID || !checkIsEmpty()) {
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

    private fun updateTitleBgById(id : String, title: String, bgColor: Int){
        CoroutineScope(Dispatchers.IO).launch {
            repository.updateTitleBgById(id, title, bgColor)
        }
    }

    private fun insertData(planData: PlanData) {
        CoroutineScope(Dispatchers.IO).launch {
            repository.insertPlannerData(planData)
        }
    }

    private fun checkIsEmpty(): Boolean =
        (mBgColor.value == 0 || mTitle.value == "" || mTitle.value == null)


}

