package com.example.todoplusminus.ui.main.memo

import androidx.lifecycle.*
import com.example.todoplusminus.util.livedata.Event
import com.example.todoplusminus.data.entities.PlanMemo
import com.example.todoplusminus.data.repository.IPlannerRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

class PlanMemoVM @Inject constructor(
    private val repository: IPlannerRepository,
    private val targetDate: LocalDate = LocalDate.now()
) {

    val memoData : MutableLiveData<PlanMemo> = MediatorLiveData<PlanMemo>().apply{
        val data = repository.getMemoByDate(targetDate).asLiveData()
        addSource(data){
            this.value = it ?: PlanMemo.create()
        }
    }
        get() {
            //todo databinding object가 editText 변경시에 memoData.value.contents에 값을 채우기 때문에 이 곳에 임시적으로 notify를 할 수 있게끔 만들었다.
            //code readability 측면에선 inversebinding adapter를 구현해서 textChange event마다 notify를 날려주는게 코드 가독성 측면에선 훨씬 좋다
            //그러므로 일단 임시적으로 이와같이 처리하고, 후에 inverbinding adapter를 구현하여 처리한다.
            field.notify()
            return field
        }

    val wantEditorClose
            : MutableLiveData<Event<Boolean>> = MutableLiveData(
        Event(false)
    )

    val showWarningDeleteDialog : MutableLiveData<Event<Boolean>> = MutableLiveData(
        Event(false)
    )


    fun onDone() {
        CoroutineScope(Dispatchers.Main).launch {
            repository.insertPlanMemo(memoData.value!!)
            onCloseEditor()
        }
    }

    fun onCancel() {
        onCloseEditor()
    }

    fun onDelete(){
        CoroutineScope(Dispatchers.Main).launch {
            repository.deleteMemoByDate(targetDate)
        }
    }

    fun showWarningDialog(){
        this.showWarningDeleteDialog.value =
            Event(true)
    }

    private fun onCloseEditor() {
        wantEditorClose.value =
            Event(true)
    }
}


fun <T> MutableLiveData<T>.notify() {
    CoroutineScope(Dispatchers.Main).launch {
        this@notify.value = this@notify.value
    }
}