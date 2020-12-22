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
import com.bluelinelabs.conductor.Controller
import com.bluelinelabs.conductor.RouterTransaction
import com.bluelinelabs.conductor.changehandler.FadeChangeHandler
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
        binder.vm = mSettingVM
        binder.lifecycleOwner = this
        return binder.root
    }

    override fun onViewBound(v: View) {
        Log.d("godgod", "settingVC create")
        initSettingList()
        onSubscribe()

    }

    private fun initSettingList() {
        binder.settingList.adapter = SettingListAdapter(mSettingVM)
        binder.settingList.layoutManager = LinearLayoutManager(binder.root.context)
    }

    private fun onSubscribe() {
        mSettingVM.showFontSettingEditor.observe(this, Observer { event ->
            event.getContentIfNotHandled()?.let {  show ->
                if(show) showFontEditor()
            }
        })
    }

    private fun showFontEditor(){
        val fontSettingVM = FontSettingVM()
        val fontController = FontSettingController(fontSettingVM)
        this.pushController(RouterTransaction.with(fontController).apply {
            pushChangeHandler(FadeChangeHandler())
            popChangeHandler(FadeChangeHandler())
        })
    }
}

class SettingListAdapter(private val settingVM: SettingVM) : RecyclerView.Adapter<GenericVH>() {

    class SettingTitleVH(private val vb: ControllerSettingTitleItemBinding,
                         private val vm: SettingVM) :
        GenericVH(vb.root) {
        companion object {
            const val TYPE = 1
        }

        override fun bind(data: Pair<Int, SettingData>) {
            vb.vm = vm
            vb.titleTv.text = vb.root.context.getString(data.first)
        }

    }

    class SettingContainToggleBtnVH(
        private val vb: ControllerSettingToggleItemBinding,
        private val vm: SettingVM
    ) : GenericVH(vb.root) {
        companion object {
            const val TYPE = 2
        }

        override fun bind(data: Pair<Int, SettingData>) {
            val value = (data.second as? ValueBoolean)?.value
            val tag = (data.second as? ValueBoolean)?.tag

            vb.vm = vm
            vb.tag = tag

            vb.titleTv.text = vb.root.context.getString(data.first)
            vb.valueToggle.isChecked = value ?: false
        }
    }

    class SettingContainTextViewVH(
        private val vb: ControllerSettingTextviewItemBinding,
        private val vm: SettingVM
    ) : GenericVH(vb.root) {
        companion object {
            const val TYPE = 3
        }

        override fun bind(data: Pair<Int, SettingData>) {
            val value = (data.second as? ValueString)?.value
            val tag = (data.second as? ValueString)?.tag
            vb.vm = vm
            vb.tag = tag

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
                SettingTitleVH(vb, settingVM)

            }
            SettingContainTextViewVH.TYPE -> {
                val vb = ControllerSettingTextviewItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                SettingContainTextViewVH(vb, settingVM)

            }
            SettingContainToggleBtnVH.TYPE -> {
                val vb = ControllerSettingToggleItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                SettingContainToggleBtnVH(vb, settingVM)
            }
            else -> {
                val vb = ControllerSettingToggleItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                SettingContainToggleBtnVH(vb, settingVM)
            }
        }
    }

    override fun getItemCount(): Int = mDataList.size

    override fun onBindViewHolder(holder: GenericVH, position: Int) {
        holder.bind(mDataList[position])
    }
}