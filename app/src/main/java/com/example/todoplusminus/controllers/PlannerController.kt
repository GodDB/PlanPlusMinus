package com.example.todoplusminus.controllers

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.*
import com.bluelinelabs.conductor.RouterTransaction
import com.example.todoplusminus.MyTransitionCH
import com.example.todoplusminus.R
import com.example.todoplusminus.base.DBControllerBase
import com.example.todoplusminus.databinding.ControllerPlannerBinding
import com.example.todoplusminus.databinding.PlanListItemBinding
import com.example.todoplusminus.entities.PlanData
import com.example.todoplusminus.repository.PlannerRepository
import com.example.todoplusminus.util.*
import com.example.todoplusminus.vm.PlanEditVM
import com.example.todoplusminus.vm.PlannerViewModel
import com.example.todoplusminus.vm.ViewModelFactory
import kotlin.math.max
import kotlin.math.min


class PlannerController : DBControllerBase {

    interface Delegate{
        fun showMemoEditor()
        fun showHistoryEditor()
    }

    companion object {
        const val TAG = "planner_controller"
    }

    private lateinit var binder: ControllerPlannerBinding
    private lateinit var planVM: PlannerViewModel
    private var mDelegate : Delegate? = null

    private var repository : PlannerRepository? = null

    constructor() : super()
    constructor(args: Bundle?) : super(args)
    constructor(repository : PlannerRepository, delegate : Delegate?){
        this.repository = repository
        mDelegate = delegate
    }

    override fun connectDataBinding(inflater: LayoutInflater, container: ViewGroup): View {
        binder = DataBindingUtil.inflate(inflater, R.layout.controller_planner, container, false)

        val factory = ViewModelFactory(repository!!) // aac viewModel은 factory를 이용해서 생성함.
        planVM = ViewModelProviders.of(activity as AppCompatActivity, factory)
            .get(PlannerViewModel::class.java)

        binder.vm = planVM
        binder.lifecycleOwner = this
        return binder.root
    }

    override fun onViewBound(v: View) {
        Log.d("godgod", "onCreate")
        binder.subWatch.start()
        binder.mainWatch.startAnimation()

        addEvent()
        configureRV()
        onSubscribe()
    }

    private fun showPlanEditor(id: String) {
        //todo test

        pushController(RouterTransaction.with(
            PlanEditController(PlanEditVM(repository!!).apply {
                setData(id)
            })
        ).apply {
            pushChangeHandler(MyTransitionCH())
            popChangeHandler(MyTransitionCH())
        })
    }


    private fun addEvent() {
    }

    private fun configureRV() {
        binder.planList.adapter = PlanListAdapter(planVM)
        binder.planList.layoutManager = LinearLayoutManager(activity!!)

        itemTouchHelperCallback =
            ItemTouchHelperCallback(binder.planList.adapter as PlanListAdapter)
        val helper = ItemTouchHelper(itemTouchHelperCallback)
        helper.attachToRecyclerView(binder.planList)

        itemSwipeEventHelper.attachToRecyclerView(binder.planList)
    }

    private lateinit var itemTouchHelperCallback: ItemTouchHelperCallback

    private fun onSubscribe() {

        //room에선 mutableLivedata를 지원하지 않는다.
        //그런 상황에서 livedata를 mutablelivedata로 변환이 필요하고
        //room이 livedata를 리턴할 때 비동기적으로 데이터를 반환하기 때문에 데이터를 반환하는 시점을 알기 위해선
        //observing을 통할 수 밖에 없다.
        //하지만 livedata를 observing 하기 위해선 lifecycleowner가 필요하고 그 소유자는 이 controller 클래스이기 때문에
        //어쩔 수 없이 이곳에서 room db값을 전달받으면 vm의 planList에 데이터를 전달한다...
        /*planVM._planList.observe(this, Observer { items ->
            planVM.planList.value = items
        })*/
        planVM.isEditMode.observe(this, Observer { editMode ->
            itemTouchHelperCallback.enabledLongPress = editMode
            itemSwipeEventHelper.isSwipeEnabled = !editMode
        })

        planVM.isShowMemoEditor.observe(this, Observer {isShow ->
            if(isShow) {
                Log.d("godgod", "memo show")
                mDelegate?.showMemoEditor()
            }
        })


        planVM.editPlanDataID.observe(this, Observer { id ->
            if(id != null && id != ""){
                showPlanEditor(id)
                planVM.clearEditPlanId()
            }
        })


    }

    private fun planListScrollMoveTo(index: Int) {
        binder.planList.scrollToPosition(index)
    }

    /** adapter에게 위임하기 위한 delegate object
     *
     *  현재는 item click시에 전달된다.
     * */
/*    private val planListDelegate = object : PlanListAdapter.Delegate {
        override fun showPlanEditor(index: Int, bgColor: Int, title: String) {
            this@PlannerController.showPlanEditor(index, bgColor, title)
        }
    }*/


    /**
     * 리사이클러뷰의 swipe 이벤트를 담당하는 object
     *
     * 다른 곳에서 재활용 할 일이 없으므로, 익명객체로 생성한다.
     * editMode에선 작동하지 않는다.
     * */
    private val itemSwipeEventHelper = object {
        var isSwipeEnabled = true

        private var mRecyclerView: RecyclerView? = null
        private var mLayoutManager: RecyclerView.LayoutManager? = null

        //swipe로 이동할 수 있는 view의 x좌표
        private val minLeft = -DpConverter.dpToPx(70f)
        private val maxRight
            get() = DeviceManager.getDeviceWidth() + DpConverter.dpToPx(70f) - viewWidth!!

        //뷰의 원래 위치
        private var viewWidth: Int? = null
        private val originX
            get() = DeviceManager.getDeviceWidth() / 2 - (viewWidth?.div(2) ?: 0)

        //사용자가 처음 눌렀을 때의 위치
        private var firstPressedX = 0f

        private var result = true

        fun attachToRecyclerView(rv: RecyclerView?) {
            mRecyclerView = rv
            mLayoutManager = rv?.layoutManager
            mRecyclerView?.addOnItemTouchListener(mItemTouchListener)
        }

        private val mItemTouchListener = object : RecyclerView.OnItemTouchListener {
            private var targetView: View? = null

            override fun onTouchEvent(rv: RecyclerView, event: MotionEvent) {}

            override fun onInterceptTouchEvent(rv: RecyclerView, event: MotionEvent): Boolean {
                if (isSwipeEnabled)
                    when (event.actionMasked) {
                        MotionEvent.ACTION_DOWN -> {
                            firstPressedX = event.x
                            targetView = rv.findChildViewUnder(event.x, event.y)
                            viewWidth = targetView?.width
                        }

                        MotionEvent.ACTION_MOVE -> {
                            if (result) {
                                targetView?.let { v ->
                                    v.x = min(
                                        maxRight,
                                        max(originX + event.x - firstPressedX, minLeft)
                                    )

                                    //왼쪽 swipe로 끝지점에 도달
                                    if (v.x == minLeft) {
                                        resetLocation(v)
                                        VibrateHelper.start()
                                        result = false

                                        val position = mLayoutManager?.getPosition(v)
                                        if (position != null) {
                                            planVM.updateCountByIndex(-1, position)
                                        }
                                    }

                                    //오른쪽 swipe로 끝지점에 도달
                                    else if (v.x == maxRight) {
                                        resetLocation(v)
                                        VibrateHelper.start()
                                        result = false

                                        val position = mLayoutManager?.getPosition(v)
                                        if (position != null) {
                                            planVM.updateCountByIndex(1, position)
                                        }
                                    }
                                }
                            }
                        }

                        MotionEvent.ACTION_UP -> {
                            targetView?.let { v ->
                                resetLocation(v)
                            }
                            result = true
                        }
                    }
                return false
            }

            override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}
        }

        // 뷰의 위치를 원래대로 복귀시킨다.
        private fun resetLocation(v: View) {
            v.x = originX
        }
    }
}


/**
 * plan list
 * recyclerView adapter
 * */
class PlanListAdapter(private val planVM: PlannerViewModel) :
    RecyclerView.Adapter<PlanListAdapter.PlanListVH>(),
    ItemTouchHelperCallback.ItemTouchHelperListener {

    private val curDataList = mutableListOf<PlanData>()

    private lateinit var binder: PlanListItemBinding

    fun updateDiffItems(newDatalist: List<PlanData>) {
        val callback = CommonDiffUtil(
            curDataList,
            newDatalist
        )
        val result = DiffUtil.calculateDiff(callback)

        this.curDataList.clear()
        this.curDataList.addAll(newDatalist)
        result.dispatchUpdatesTo(this)
    }

    fun updateAllItems(newDatalist: List<PlanData>) {
        this.curDataList.clear()
        this.curDataList.addAll(newDatalist)

        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlanListVH {
        binder = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.plan_list_item,
            parent,
            false
        )
        return PlanListVH(binder)
    }

    override fun getItemCount(): Int = curDataList.size


    override fun onBindViewHolder(holder: PlanListVH, position: Int) {
        holder.bind(planVM)
    }

    override fun onItemMove(fromPosition: Int, toPosition: Int): Boolean {
        //이동할 객체 저장
        val data = curDataList[fromPosition]
        //이동할 객체 삭제
        curDataList.remove(data)
        //이동하고 싶은 position에 추가
        curDataList.add(toPosition, data)

        //adapter에게 데이터 이동을 알린다.
        notifyItemMoved(fromPosition, toPosition)
        //변경이 완료 되었으므로 인덱스를 재 정렬 한다.
        rearrangeIndex()

        //vibrator
        VibrateHelper.start()

        return true
    }

    override fun onSwipe(position: Int, direction: Int) {
        //직접 구현해서 사용함.
    }


    /**
     * 인덱스를 재 정렬 한다.
     * vm이 지닌 livedata의 list와
     * 어댑터가 지닌 list가 같은 주소값을 지니고 있으므로
     * curList의 index를 변경하면 vm의 index도 변경된다.
     */
    private fun rearrangeIndex() {
        curDataList.reversed().forEachIndexed { index, planData ->
            planData.index = index
        }
    }

    inner class PlanListVH(private val binder: PlanListItemBinding) :
        RecyclerView.ViewHolder(binder.root) {

        fun bind(planVM: PlannerViewModel) {
            binder.vm = planVM
            binder.index = adapterPosition
            binder.executePendingBindings()

        }
    }
}


/**
 * recyclerView item들을 이동시켜주는 object
 * */
class ItemTouchHelperCallback(private val listener: ItemTouchHelperListener) :
    ItemTouchHelper.Callback() {

    interface ItemTouchHelperListener {
        fun onItemMove(fromPosition: Int, toPosition: Int): Boolean
        fun onSwipe(position: Int, direction: Int)
    }

    var enabledLongPress = false

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        val dragFlags = ItemTouchHelper.DOWN or ItemTouchHelper.UP
        val swipeFlags = ItemTouchHelper.START or ItemTouchHelper.END
        return makeMovementFlags(dragFlags, swipeFlags)
    }

    override fun isLongPressDragEnabled(): Boolean = enabledLongPress
    override fun isItemViewSwipeEnabled(): Boolean = false

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean = listener.onItemMove(viewHolder.adapterPosition, target.adapterPosition)

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
    }

}