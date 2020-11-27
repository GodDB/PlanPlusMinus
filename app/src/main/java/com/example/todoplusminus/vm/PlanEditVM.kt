package com.example.todoplusminus.vm

import androidx.lifecycle.MutableLiveData
import com.example.todoplusminus.repository.PlannerRepository
import com.example.todoplusminus.util.ColorManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PlanEditVM(private val repository: PlannerRepository) {

    var mId : String = ""
    var mBgColor: MutableLiveData<Int> = MutableLiveData(ColorManager.getRandomColor())
    var mTitle: MutableLiveData<String> = MutableLiveData("")

    /**
     * create일땐 setData를 호출하지 않는다.
     *
     * 기존 planData를 업데이트 하는 상황에서만 사용한다
     * */
    fun setData(id : String){
        mId = id

        CoroutineScope(Dispatchers.IO).launch {
            val data = repository.getPlannerDataById(id)
            setTitle(data.title)
            setBgColor(data.bgColor)
        }
    }

    fun onCreatePlanData() {

    }

    fun onUpdatePlanData() {

    }

    fun setTitle(title : String){
        //메인 스레드에서 작동시킨다.
        CoroutineScope(Dispatchers.Main).launch {
            mTitle.value = title
        }
    }

    fun setBgColor(bgColor : Int){
        //메인 스레드에서 작동시킨다.
        CoroutineScope(Dispatchers.Main).launch {
            mBgColor.value = bgColor
        }
    }

    private fun checkIsEmpty(): Boolean = (mBgColor.value == 0 || mTitle.value == "" || mId == "")


}