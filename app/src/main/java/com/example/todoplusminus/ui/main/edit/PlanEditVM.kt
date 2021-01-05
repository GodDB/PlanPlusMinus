package com.example.todoplusminus.ui.main.edit

import android.graphics.Typeface
import android.renderscript.BaseObj
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import com.example.todoplusminus.AppConfig
import com.example.todoplusminus.data.entities.BaseID
import com.example.todoplusminus.util.livedata.Event
import com.example.todoplusminus.data.entities.PlanData
import com.example.todoplusminus.data.repository.IPlannerRepository
import com.example.todoplusminus.util.ColorManager
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class PlanEditVM @Inject constructor(
    private val repository: IPlannerRepository
) {

    init {
        CoroutineScope(Dispatchers.Main).launch {
            mLastestIndex = repository.getLastestIndex().first() ?: 0
        }
    }

    val font: Typeface?
        get() = AppConfig.font

    var mId: BaseID = BaseID.createEmpty()
    val mBgColor: MutableLiveData<Int> = MutableLiveData(ColorManager.getRandomColor())
    val mTitle: MutableLiveData<String> = MutableLiveData("")

    private var mLastestIndex : Int = 0

    /** edit 작업이 끝났는지(done or back or cancel)를 체크한다.
     * */
    var isEditClose: MutableLiveData<Event<Boolean>> = MutableLiveData(
        Event(false)
    )


    fun setId(id: BaseID) {
        mId = id

        if (!checkIsEmptyId(mId))
            CoroutineScope(Dispatchers.Main).launch {
                val data = repository.getPlannerDataById(id)
                setTitle(data.title)
                setBgColor(data.bgColor)
            }
    }


    fun onComplete() {
        if (checkIsEmptyId(mId)) return onCreateItem()
        onUpdatePlanData()
    }

    private fun onCreateItem() {
        if (!checkIsEmptyId(mId) && checkIsEmptyContent()) return
        val newData = PlanData.create().apply {
            this.index = mLastestIndex?.plus(1) ?: 0
            this.title = mTitle.value ?: ""
            this.bgColor = mBgColor.value ?: PlanData.DEFAULT_BG_COLOR
        }

        insertData(newData)
        onClose()
    }


    fun onClose() {
        isEditClose.value = Event(true)
    }

    private fun onUpdatePlanData() {
        if (!checkIsEmptyId(mId) || !checkIsEmptyContent()) {
            updateTitleBgById(mId, mTitle.value!!, mBgColor.value!!)

            onClose()
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

    private fun updateTitleBgById(id: BaseID, title: String, bgColor: Int) {
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

    private fun checkIsEmptyId(id : BaseID): Boolean =
        id.isEmpty()
}

