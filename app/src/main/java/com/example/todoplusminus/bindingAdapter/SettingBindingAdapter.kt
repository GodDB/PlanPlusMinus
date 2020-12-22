package com.example.todoplusminus.bindingAdapter

import android.util.Log
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.todoplusminus.controllers.SettingListAdapter
import com.example.todoplusminus.vm.SettingData


// -- setting vm

@BindingAdapter("setSettingItems")
fun setSettingItems(rv : RecyclerView, itemList : List<Pair<Int, SettingData>>?){
    if(itemList == null) return

    val adapter = (rv.adapter as? SettingListAdapter) ?: return
    adapter.setSettingDatas(itemList)
}


// -- fontsetting vm

