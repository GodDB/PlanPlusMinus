package com.example.todoplusminus.ui.setting

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bluelinelabs.conductor.RouterTransaction
import com.bluelinelabs.conductor.changehandler.FadeChangeHandler
import com.example.todoplusminus.R
import com.example.todoplusminus.base.BaseApplication
import com.example.todoplusminus.base.DBControllerBase
import com.example.todoplusminus.base.GenericVH
import com.example.todoplusminus.ui.common.CommonDialogController
import com.example.todoplusminus.ui.setting.fontSetting.FontSettingController
import com.example.todoplusminus.ui.setting.textsizeSetting.PlanSizeEditorController
import com.example.todoplusminus.databinding.ControllerSettingBinding
import com.example.todoplusminus.databinding.ControllerSettingTextviewItemBinding
import com.example.todoplusminus.databinding.ControllerSettingTitleItemBinding
import com.example.todoplusminus.databinding.ControllerSettingToggleItemBinding
import com.example.todoplusminus.util.ChangeHandlers.VerticalSlideDownCH
import com.example.todoplusminus.util.ExternalAppSender
import javax.inject.Inject

class SettingController : DBControllerBase {

    companion object {
        const val TAG = "setting_controller"
    }

    constructor() : super()
    constructor(args: Bundle?) : super(args)

    private lateinit var binder: ControllerSettingBinding

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val mSettingVM by lazy {
        ViewModelProvider(activity as AppCompatActivity, viewModelFactory)
            .get(SettingVM::class.java)
    }


    override fun connectDagger() {
        super.connectDagger()
        (activity?.application as BaseApplication).appComponent.settingComponent().create()
            .inject(this)
    }


    override fun connectDataBinding(inflater: LayoutInflater, container: ViewGroup): View {
        binder = DataBindingUtil.inflate(inflater, R.layout.controller_setting, container, false)
        binder.vm = mSettingVM
        binder.lifecycleOwner = this
        return binder.root
    }

    override fun onViewBound(v: View) {
        Log.d("godgod", "setting onCreate")
        initSettingList()
        onSubscribe()
    }

    override fun onAttach(view: View) {
        Log.d("godgod", "setting  onAttach")
        mSettingVM.reload()
    }

    private fun initSettingList() {
        binder.settingList.adapter =
            SettingListAdapter(
                mSettingVM,
                this
            )
        binder.settingList.layoutManager = LinearLayoutManager(binder.root.context)
    }

    private fun onSubscribe() {
        mSettingVM.showFontSettingEditor.observe(this, Observer { event ->
            event.getContentIfNotHandled()?.let { show ->
                if (show) showFontEditor()
            }
        })

        mSettingVM.showPlanSizeEditor.observe(this, Observer { event ->
            event.getContentIfNotHandled()?.let { show ->
                if (show) showPlanSizeEditor()
            }
        })

        mSettingVM.showWarningDialog.observe(this, Observer { event ->
            event.getContentIfNotHandled()?.let { show ->
                if (show) showWarningDialog()
            }
        })

        mSettingVM.startSendShareMessage.observe(this, Observer { event ->
            event.getContentIfNotHandled()?.let {  send ->
                if(send){
                    val sender = ExternalAppSender()
                    sender.sendShareAppMessage(activity!!)
                }
            }

        })
    }

    private fun showWarningDialog() {
        val delegate = object :
            CommonDialogController.Delegate {
            override fun onComplete() {
                mSettingVM.onDeleteAllData()
                popCurrentController()
            }

            override fun onCancel() {
                popCurrentController()
            }

        }
        //todo 다국어
        val dialog =
            CommonDialogController(
                delegate,
                "정말 데이터를 \n모두 삭제하시겠습니까?"
            )
        pushController(RouterTransaction.with(dialog).apply {
            pushChangeHandler(FadeChangeHandler(false))
            popChangeHandler(FadeChangeHandler())
        })
    }

    private fun showFontEditor() {
        this.pushController(RouterTransaction.with(FontSettingController()).apply {
            pushChangeHandler(FadeChangeHandler())
            popChangeHandler(FadeChangeHandler())
        })
    }

    private fun showPlanSizeEditor() {
        val planSizeDelegate = object :
            PlanSizeEditorController.Delegate {
            override fun onComplete(value: Int) {
                mSettingVM.setPlanSize(value)
                popCurrentController()
            }

            override fun onCancel() {
                popCurrentController()
            }
        }

        val planSizeEditor =
            PlanSizeEditorController(
                planSizeDelegate
            )
        this.pushController(RouterTransaction.with(planSizeEditor).apply {
            pushChangeHandler(VerticalSlideDownCH(false))
            popChangeHandler(VerticalSlideDownCH())
        })
    }
}

class SettingListAdapter(
    private val settingVM: SettingVM,
    private val mLifecycleOwner: LifecycleOwner
) : RecyclerView.Adapter<GenericVH>() {

    class SettingTitleVH(
        private val vb: ControllerSettingTitleItemBinding
    ) :
        GenericVH(vb.root) {
        companion object {
            const val TYPE = 1
        }

        override fun bind(data: Triple<Int, Int, ValueData>) {
            vb.titleTv.text = vb.root.context.getString(data.first)

            //color
            vb.titleTv.setTextColor(ContextCompat.getColor(vb.root.context,data.second))
        }
    }

    class SettingContainToggleBtnVH(
        private val vb: ControllerSettingToggleItemBinding
    ) : GenericVH(vb.root) {
        companion object {
            const val TYPE = 2
        }

        override fun bind(data: Triple<Int, Int, ValueData>) {
            val value = (data.third as? ValueBoolean)?.value
            val tag = (data.third as? ValueBoolean)?.tag
            vb.tag = tag

            vb.titleTv.text = vb.root.context.getString(data.first)
            vb.valueToggle.isChecked = value ?: false

            //color
            vb.titleTv.setTextColor(ContextCompat.getColor(vb.root.context,data.second))
        }
    }

    class SettingContainTextViewVH(
        private val vb: ControllerSettingTextviewItemBinding
    ) : GenericVH(vb.root) {
        companion object {
            const val TYPE = 3
        }

        override fun bind(data: Triple<Int, Int, ValueData>) {
            val value = (data.third as? ValueString)?.value
            val tag = (data.third as? ValueString)?.tag
            vb.tag = tag

            vb.titleTv.text = vb.root.context.getString(data.first)
            vb.valueTv.text = value

            //color
            vb.titleTv.setTextColor(ContextCompat.getColor(vb.root.context,data.second))
        }

    }

    private var mDataList: MutableList<Triple<Int, Int, ValueData>> = mutableListOf()

    fun setSettingDatas(datas: List<Triple<Int, Int, ValueData>>) {
        mDataList.clear()
        mDataList.addAll(datas)
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        val item = mDataList[position]
        return when (item.third) {
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
                vb.vm = settingVM
                vb.lifecycleOwner = mLifecycleOwner
                SettingTitleVH(
                    vb
                )

            }
            SettingContainTextViewVH.TYPE -> {
                val vb = ControllerSettingTextviewItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                vb.vm = settingVM
                vb.lifecycleOwner = mLifecycleOwner
                SettingContainTextViewVH(
                    vb
                )

            }
            SettingContainToggleBtnVH.TYPE -> {
                val vb = ControllerSettingToggleItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                vb.vm = settingVM
                vb.lifecycleOwner = mLifecycleOwner
                SettingContainToggleBtnVH(
                    vb
                )
            }
            else -> {
                val vb = ControllerSettingToggleItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                vb.vm = settingVM
                vb.lifecycleOwner = mLifecycleOwner
                SettingContainToggleBtnVH(
                    vb
                )
            }
        }
    }

    override fun getItemCount(): Int = mDataList.size

    override fun onBindViewHolder(holder: GenericVH, position: Int) {
        holder.bind(mDataList[position])
    }
}