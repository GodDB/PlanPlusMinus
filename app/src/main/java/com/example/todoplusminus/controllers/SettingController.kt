package com.example.todoplusminus.controllers

import android.os.Bundle
import android.text.Layout
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todoplusminus.R
import com.example.todoplusminus.base.DBControllerBase
import com.example.todoplusminus.base.GenericVH
import com.example.todoplusminus.databinding.ControllerSettingBinding
import com.example.todoplusminus.databinding.ControllerSettingTextviewItemBinding
import com.example.todoplusminus.databinding.ControllerSettingTitleItemBinding
import com.example.todoplusminus.databinding.ControllerSettingToggleItemBinding
import com.example.todoplusminus.vm.*
import java.lang.RuntimeException

class SettingController : DBControllerBase {

    companion object {
        const val TAG = "setting_controller"
    }

    private lateinit var binder: ControllerSettingBinding
    private lateinit var mSettingVM: SettingVM

    constructor() : super()
    constructor(args: Bundle?) : super(args)
    constructor(settingVM: SettingVM) {
        this.mSettingVM = settingVM
    }

    override fun connectDataBinding(inflater: LayoutInflater, container: ViewGroup): View {
        binder = DataBindingUtil.inflate(inflater, R.layout.controller_setting, container, false)
        binder.lifecycleOwner = this
        return binder.root
    }

    override fun onViewBound(v: View) {
        initSettingList()
        onSubscribe()

    }

    private fun initSettingList() {
        binder.settingList.adapter = SettingListAdapter()
        binder.settingList.layoutManager = LinearLayoutManager(binder.root.context)
    }

    private fun onSubscribe() {
        mSettingVM.settingDataList.observe(this, Observer {
            (binder.settingList.adapter as? SettingListAdapter)?.setSettingDatas(it)
        })
    }
}

class SettingListAdapter : RecyclerView.Adapter<GenericVH>() {

    class SettingTitleVH(private val vb: ControllerSettingTitleItemBinding) : GenericVH(vb.root) {
        companion object {
            const val TYPE = 1
        }

        override fun bind(data: Pair<Int, SettingData>) {
            vb.titleTv.text = vb.root.context.getString(data.first)
        }

    }

    class SettingContainToggleBtnVH(private val vb: ControllerSettingToggleItemBinding) : GenericVH(vb.root) {
        companion object {
            const val TYPE = 2
        }

        override fun bind(data: Pair<Int, SettingData>) {
            val value = (data.second as? ValueBoolean)?.value

            vb.titleTv.text = vb.root.context.getString(data.first)

        }

    }

    class SettingContainTextViewVH(private val vb: ControllerSettingTextviewItemBinding) : GenericVH(vb.root) {
        companion object {
            const val TYPE = 3
        }

        override fun bind(data: Pair<Int, SettingData>) {
            val value = (data.second as? ValueString)?.value

            vb.titleTv.text = vb.root.context.getString(data.first)
            vb.valueTv.text = value
        }

    }

    private var mDataList: MutableList<Pair<Int, SettingData>> = mutableListOf()

    fun setSettingDatas(datas: List<Pair<Int, SettingData>>) {
        mDataList.clear()
        mDataList.addAll(datas)
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        val item = mDataList[position]
        return when (item.second) {
            is ValueEmpty -> SettingTitleVH.TYPE
            is ValueBoolean -> SettingContainToggleBtnVH.TYPE
            is ValueString -> SettingContainTextViewVH.TYPE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenericVH {
        return when (viewType) {
            SettingTitleVH.TYPE -> {
                val vb = ControllerSettingTitleItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                SettingTitleVH(vb)

            }
            SettingContainTextViewVH.TYPE -> {
                val vb = ControllerSettingTextviewItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                SettingContainTextViewVH(vb)

            }
            SettingContainToggleBtnVH.TYPE -> {
                val vb = ControllerSettingToggleItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                SettingContainToggleBtnVH(vb)
            }
            else -> {
                val vb = ControllerSettingToggleItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                SettingContainToggleBtnVH(vb)
            }
        }
    }

    override fun getItemCount(): Int = mDataList.size

    override fun onBindViewHolder(holder: GenericVH, position: Int) {
        holder.bind(mDataList[position])
    }
}