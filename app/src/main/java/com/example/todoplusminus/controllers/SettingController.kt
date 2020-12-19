package com.example.todoplusminus.controllers

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.todoplusminus.R
import com.example.todoplusminus.base.DBControllerBase
import com.example.todoplusminus.base.GenericVH
import com.example.todoplusminus.databinding.ControllerSettingBinding
import com.example.todoplusminus.databinding.ControllerSettingTextviewItemBinding
import com.example.todoplusminus.databinding.ControllerSettingTitleItemBinding
import com.example.todoplusminus.databinding.ControllerSettingToggleItemBinding

class SettingController : DBControllerBase {

    companion object{
        const val TAG = "setting_controller"
    }

    private lateinit var binder: ControllerSettingBinding

    constructor() : super()
    constructor(args: Bundle?) : super(args)

    override fun connectDataBinding(inflater: LayoutInflater, container: ViewGroup): View {
        binder = DataBindingUtil.inflate(inflater, R.layout.controller_setting, container, false)
        binder.lifecycleOwner = this
        return binder.root
    }

    override fun onViewBound(v: View) {
        initSettingList()
        onSubscribe()

    }

    private fun initSettingList(){

    }

    private fun onSubscribe(){

    }
}

class SettingListAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    class SettingTitleVH(vb : ControllerSettingTitleItemBinding) : GenericVH(vb.root){
        companion object{
            const val TYPE = 1
        }

        override fun bind() {
        }

    }
    class SettingContainToggleBtnVH(vb : ControllerSettingToggleItemBinding) : GenericVH(vb.root){
        companion object{
            const val TYPE = 2
        }

        override fun bind() {
        }

    }
    class SettingContainTextViewVH(vb : ControllerSettingTextviewItemBinding) : GenericVH(vb.root){
        companion object{
            const val TYPE = 3
        }

        override fun bind() {
        }

    }

    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        TODO("Not yet implemented")
    }
}