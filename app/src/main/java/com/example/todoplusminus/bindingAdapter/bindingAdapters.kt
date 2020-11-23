package com.example.todoplusminus.bindingAdapter


import android.graphics.Color
import android.util.Log
import android.view.View
import androidx.cardview.widget.CardView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.todoplusminus.controllers.PlanListAdapter
import com.example.todoplusminus.entities.PlanData
import com.example.todoplusminus.util.CommonAnimationHelper
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



