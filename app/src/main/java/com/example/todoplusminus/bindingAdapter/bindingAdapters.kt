package com.example.todoplusminus.bindingAdapter


import android.graphics.Color
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.core.view.marginTop
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.todoplusminus.controllers.PlanListAdapter
import com.example.todoplusminus.entities.PlanData
import com.example.todoplusminus.ui.CreatePlanView
import com.example.todoplusminus.util.CommonAnimationHelper
import com.example.todoplusminus.util.DpConverter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@BindingAdapter("bind:items")
fun items(rv: RecyclerView, dataList: LiveData<List<PlanData>>) {
    if (rv.adapter == null) return

    dataList.value?.let {
        (rv.adapter as? PlanListAdapter)?.updateItems(it)
    }
}

@BindingAdapter("bind:editMode")
fun setEditMode(view: View, isEdit: Boolean) {
    CommonAnimationHelper.startFadeAnimation(view, isEdit)
}

/**
 * 리사이클러뷰만의 editMode
 *
 * editMode시에 createPlanView의 height만큼 내린다
 * */
@BindingAdapter("bind:editMode")
fun setEditMode(rv: RecyclerView, isEdit: Boolean) {
    if (isEdit){
        val marginParam = (rv.layoutParams as ViewGroup.MarginLayoutParams)
        marginParam.topMargin = DpConverter.dpToPx(100f).toInt()
        rv.layoutParams = marginParam
    }
    else{
        val marginParam = (rv.layoutParams as ViewGroup.MarginLayoutParams)
        marginParam.topMargin = 0
        rv.layoutParams = marginParam
    }

}

@BindingAdapter("bind:backgroundColor")
fun setBackgroundColor(view: CardView, data: PlanData) {
    if (data.count >= 1) view.setCardBackgroundColor(data.bgColor)
    else view.setCardBackgroundColor(Color.WHITE)
}


@BindingAdapter("bind:animation")
fun setAnimation(v: View, isEdit: Boolean) {
    if (isEdit)
        CommonAnimationHelper.startVibrateAnimation(v)
    else
        CommonAnimationHelper.stop(v)
}




