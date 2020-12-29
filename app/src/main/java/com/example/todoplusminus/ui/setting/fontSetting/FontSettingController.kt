package com.example.todoplusminus.ui.setting.fontSetting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todoplusminus.base.DBControllerBase
import com.example.todoplusminus.databinding.ControllerFontSettingBinding
import com.example.todoplusminus.databinding.ControllerFontSettingItemBinding

class FontSettingController : DBControllerBase {
    constructor() : super()
    constructor(args: Bundle?) : super(args)
    constructor(fontSettingVM: FontSettingVM) {
        this.mFontSettingVM = fontSettingVM
    }

    private lateinit var mFontSettingVM: FontSettingVM
    private lateinit var binder: ControllerFontSettingBinding


    override fun connectDataBinding(inflater: LayoutInflater, container: ViewGroup): View {
        binder = ControllerFontSettingBinding.inflate(inflater, container, false)
        binder.vm = mFontSettingVM
        binder.lifecycleOwner = this
        return binder.root
    }

    override fun onViewBound(v: View) {
        configureUI()
        onSubscribe()
    }

    private fun configureUI() {
        binder.fontList.adapter =
            FontListAdapter(
                mFontSettingVM
            )
        binder.fontList.layoutManager = LinearLayoutManager(binder.root.context)
    }

    private fun onSubscribe() {
        mFontSettingVM.allowDownload.observe(this, Observer { event ->
            event.getContentIfNotHandled()?.let { isDownloadAllow ->
                (binder.fontList.adapter as? FontListAdapter)?.setIsAllowDownload(isDownloadAllow)
            }
        })

        mFontSettingVM.downloadingFontName.observe(this, Observer { event ->
            event.getContentIfNotHandled()?.let { fontName ->
                (binder.fontList.adapter as? FontListAdapter)?.setDownloadingFontName(fontName)
            }
        })

        mFontSettingVM.completeDownloadFontName.observe(this, Observer { event ->
            event.getContentIfNotHandled()?.let { fontName ->
                (binder.fontList.adapter as? FontListAdapter)?.setCompleteDownladFontName(fontName)
            }
        })

        mFontSettingVM.closeFontSettingEditor.observe(this, Observer { event ->
            event.getContentIfNotHandled()?.let { isClose ->
                if(isClose) popCurrentController()
            }
        })
    }
}

class FontListAdapter(private val fontSettingVM: FontSettingVM) :
    RecyclerView.Adapter<FontListAdapter.FontListVH>() {

    private val fontListData: MutableList<FontItemData> = mutableListOf()
    private var isAllowDownload: Boolean = true
    private var downloadingFontName: String = ""
    private var completeDownloadFontName : String = ""

    fun setDataList(fontList: List<FontItemData>) {
        this.fontListData.clear()
        this.fontListData.addAll(fontList)
        notifyDataSetChanged()
    }

    fun setIsAllowDownload(value: Boolean) {
        this.isAllowDownload = value
    }

    fun setDownloadingFontName(fontName: String) {
        this.downloadingFontName = fontName
        notifyDataSetChanged()
    }

    fun setCompleteDownladFontName(fontName: String){
        this.completeDownloadFontName = fontName
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FontListVH {
        val vb = ControllerFontSettingItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return FontListVH(vb)
    }

    override fun getItemCount(): Int = fontListData.size

    override fun onBindViewHolder(holder: FontListVH, position: Int) {
        holder.bind()
    }

    inner class FontListVH(private val vb: ControllerFontSettingItemBinding) :
        RecyclerView.ViewHolder(vb.root) {
        fun bind() {
            vb.vm = fontSettingVM
            vb.fontImage.setImageResource(fontListData[adapterPosition].fontImageId)

            vb.fontTypeTv.text = null
            fontListData[adapterPosition].fontType.forEach {
                vb.fontTypeTv.text = "${vb.fontTypeTv.text} ${vb.root.context.getString(it)}"
            }

            val fontName = fontListData[adapterPosition].fontName

            if (downloadingFontName == fontName) vb.loadingIndicator.visibility =
                View.VISIBLE
            else vb.loadingIndicator.visibility = View.GONE

            if (completeDownloadFontName == fontName) vb.completeIndicator.visibility = View.VISIBLE
            else vb.completeIndicator.visibility = View.GONE

            vb.root.setOnClickListener {
                if(isAllowDownload) fontSettingVM.onFontSelect(fontName)
            }
        }
    }

}