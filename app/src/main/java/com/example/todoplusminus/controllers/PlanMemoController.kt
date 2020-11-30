package com.example.todoplusminus.controllers

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import com.example.todoplusminus.base.DBControllerBase
import com.example.todoplusminus.databinding.ControllerPlanMemoBinding
import com.example.todoplusminus.repository.PlannerRepository
import com.example.todoplusminus.util.DeviceManager
import com.example.todoplusminus.vm.PlanMemoVM
import kotlin.math.max
import kotlin.math.min

class PlanMemoController : DBControllerBase {
    constructor() : super()
    constructor(args: Bundle?) : super(args)
    constructor(repository: PlannerRepository) {
        mRepository = repository
    }

    private var mRepository: PlannerRepository? = null
    private var mVM: PlanMemoVM? = null
    private lateinit var binder: ControllerPlanMemoBinding

    override fun connectDataBinding(inflater: LayoutInflater, container: ViewGroup): View {
        binder = ControllerPlanMemoBinding.inflate(inflater, container, false)
        mVM = PlanMemoVM(mRepository!!)
        binder.vm = mVM
        binder.lifecycleOwner = this

        return binder.root
    }

    override fun onViewBound(v: View) {
        addEvent()
        onSubscribe()
    }

    private fun addEvent() {
        binder.rootView.setOnTouchListener(verticalSlideEventListener)
    }

    private fun onSubscribe() {

    }



    /**
     * 세로 slide 이벤트를 통해
     * 사용자가 편하게 메모 컨트롤러를 종료시킬 수 있게 해주는 리스너
     * */
    private val verticalSlideEventListener = object : View.OnTouchListener {
        private var firstPressedY: Float = 0f
        private var orgY: Float? = null

        private var maxY : Int? = null


        override fun onTouch(v: View, event: MotionEvent): Boolean {
            when (event.actionMasked) {
                MotionEvent.ACTION_DOWN -> {
                    firstPressedY = event.y

                    if(maxY == null) maxY = (v.bottom - v.top)/2
                    if(orgY == null) orgY = v.y
                }
                MotionEvent.ACTION_MOVE -> {
                    val newY = max(orgY?:0f, v.y + event.y - firstPressedY.toInt())
                    v.y = newY
                }

                MotionEvent.ACTION_UP -> {
                    if (v.y > maxY!!) {
                        (binder.root.parent as? ViewGroup)?.removeAllViews()
                        popCurrentController()
                    }
                    else resetLocation(v)
                }
            }
            return true
        }

        private fun resetLocation(v: View) {
            v.y = this.orgY ?: 0f
        }
    }
}