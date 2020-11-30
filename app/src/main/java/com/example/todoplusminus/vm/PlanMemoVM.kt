package com.example.todoplusminus.vm

import android.util.DisplayMetrics
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.todoplusminus.entities.PlanMemo
import com.example.todoplusminus.repository.PlannerRepository
import com.example.todoplusminus.util.TimeProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class PlanMemoVM(private val repository: PlannerRepository) {

    var memoData: MutableLiveData<PlanMemo> = MutableLiveData(
        runBlocking(Dispatchers.IO) {
            repository.getMemoByDate(TimeProvider.getCurDate()) ?: PlanMemo.create()
        }
    )
        get() {
            //todo databinding object가 editText 변경시에 memoData.value.contents에 값을 채우기 때문에 이 곳에 임시적으로 notify를 할 수 있게끔 만들었다.
            //code readability 측면에선 inversebinding adapter를 구현해서 textChange event마다 notify를 날려주는게 코드 가독성 측면에선 훨씬 좋다
            //그러므로 일단 임시적으로 이와같이 처리하고, 후에 inverbinding adapter를 구현하여 처리한다.
            field.notify()
            return field
        }

    var wantEditorClose: MutableLiveData<Boolean> = MutableLiveData(false)


    fun onDone() {
        CoroutineScope(Dispatchers.IO).launch {
            repository.insertPlanMemo(memoData.value!!)
        }
        onCloseEditor()
    }

    fun onCancel() {
        onCloseEditor()
    }

    private fun onCloseEditor() {
        wantEditorClose.value = true
        wantEditorClose.value = false
    }

    /**
     * onDone을 호출하기 위한 인터페이스 구현체
     *
     * 바인딩 어댑터를 이용해서 onDone을 호출하게 하려고 하는데 람다식으론 에러가 발생함... (무조건은 아니고 간혈적임 ..)
     * 찾아본 결과, kotlin -> java 변환간에 문제점이라고 하는데 어떤건 되고 어떤건 안되서 이해가 안가네...
     *
     * 그래서 람다식으론 처리가 안되서 인터페이스를 이용해서 처리함. 결국 람다 -> 인터페이스 구현체로 변경되니깐
     * */
    val onDoneListener = object : OnDoneListener {
        override fun onDone() {
            this@PlanMemoVM.onDone()
        }

    }
}

interface OnDoneListener {
    fun onDone()
}

fun <T> MutableLiveData<T>.notify() {
    CoroutineScope(Dispatchers.Main).launch {
        this@notify.value = this@notify.value
    }
}