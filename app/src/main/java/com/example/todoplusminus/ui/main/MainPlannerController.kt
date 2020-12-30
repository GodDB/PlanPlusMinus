package com.example.todoplusminus.ui.main

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.*
import com.bluelinelabs.conductor.RouterTransaction
import com.example.todoplusminus.AppConfig
import com.example.todoplusminus.util.ChangeHandlers.MyTransitionCH
import com.example.todoplusminus.R
import com.example.todoplusminus.base.BaseApplication
import com.example.todoplusminus.base.DBControllerBase
import com.example.todoplusminus.databinding.ControllerPlannerBinding
import com.example.todoplusminus.data.entities.PlanData
import com.example.todoplusminus.data.repository.PlannerRepository
import com.example.todoplusminus.ui.customViews.PMCalendarView
import com.example.todoplusminus.databinding.ControllerPlannerItemBinding
import com.example.todoplusminus.di.MainViewModelFactory
import com.example.todoplusminus.ui.main.edit.PlanEditController
import com.example.todoplusminus.util.*
import com.example.todoplusminus.ui.main.edit.PlanEditVM
import nl.dionsegijn.konfetti.models.Shape
import nl.dionsegijn.konfetti.models.Size
import java.time.LocalDate
import javax.inject.Inject
import kotlin.math.max
import kotlin.math.min


class PlannerController : DBControllerBase {

    interface Delegate {
        fun showMemoEditor()
        fun showHistoryEditor(id: String)
    }

    companion object {
        const val TAG = "planner_controller"
    }


    constructor() : super()
    constructor(args: Bundle?) : super(args)
    constructor(delegate : PlannerController.Delegate){
        this.mDelegate = delegate
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val planVM by lazy {
        ViewModelProvider(activity as AppCompatActivity, viewModelFactory)
            .get(PlannerViewModel::class.java)
    }
    private lateinit var binder: ControllerPlannerBinding
    private var mDelegate: Delegate? = null


    override fun connectDataBinding(inflater: LayoutInflater, container: ViewGroup): View {
        (activity?.application as BaseApplication).appComponent.planComponent().create().inject(this)

        binder = DataBindingUtil.inflate(inflater, R.layout.controller_planner, container, false)

        binder.vm = planVM
        binder.lifecycleOwner = this
        return binder.root
    }

    override fun onViewBound(v: View) {
        addEvent()
        configureRV()
        onSubscribe()
    }

    override fun onAttach(view: View) {
        binder.subWatch.start()
        binder.mainWatch.start()

        planVM.reload()

    }

    override fun onDetach(view: View) {
        binder.subWatch.stop()
        binder.mainWatch.stop()
    }

    private fun showPlanEditor(id: String) {
        //todo test
        pushController(RouterTransaction.with(
            PlanEditController(id)
        ).apply {
            pushChangeHandler(MyTransitionCH())
            popChangeHandler(MyTransitionCH())
        })
    }


    private fun addEvent() {
        binder.calendarView.setDelegate(
            object : PMCalendarView.Delegate {
                override fun selectedDate(date: LocalDate) {
                    planVM.changeDate(date)
                }
            }
        )
    }

    private fun configureRV() {
        binder.planList.adapter =
            PlanListAdapter(planVM, this)
        binder.planList.layoutManager = LinearLayoutManager(binder.rootView.context)

        itemTouchHelperCallback =
            ItemTouchHelperCallback(binder.planList.adapter as PlanListAdapter)
        val helper = ItemTouchHelper(itemTouchHelperCallback)
        helper.attachToRecyclerView(binder.planList)

        itemSwipeEventHelper.attachToRecyclerView(binder.planList)
    }

    private lateinit var itemTouchHelperCallback: ItemTouchHelperCallback

    private fun onSubscribe() {

        planVM.isEditMode.observe(this, Observer { editMode ->
            itemTouchHelperCallback.enabledLongPress = editMode
            itemSwipeEventHelper.isSwipeEnabled = !editMode
        })

        planVM.showMemoEditor.observe(this, Observer { event ->
            //livedata 변경 후에 이벤트가 처리된 적 있다면 null, 아니면 변경된 값을 전달받는다.
            event.getContentIfNotHandled()?.let { isShow ->
                if (isShow) mDelegate?.showMemoEditor()
            }
        })

        planVM.showHistoryId.observe(this, Observer { event ->
            //livedata 변경 후에 이벤트가 처리된 적 있다면 null, 아니면 변경된 값을 전달받는다.
            event.getContentIfNotHandled()?.let { id ->
                mDelegate?.showHistoryEditor(id)
            }
        })


        planVM.showEditPlanDataID.observe(this, Observer { event ->
            event?.getContentIfNotHandled()?.let { id ->
                showPlanEditor(id)
            }
        })

        planVM.targetDate.observe(this, Observer { date ->
            binder.calendarView.setSelectDate(date)
        })

        planVM.showFirecrackerAnim.observe(this, Observer { event ->
            event.getContentIfNotHandled()?.let { show ->
                if (show) showFirecrackerAnim()
            }
        })
    }

    private fun planListScrollMoveTo(index: Int) {
        binder.planList.scrollToPosition(index)
    }

    private fun showFirecrackerAnim() {
        val fireCrackerView = binder.firecrackerAnim

        fireCrackerView.build()
            .addColors(
                getColor(R.color.lt_yellow),
                getColor(R.color.lt_orange),
                getColor(R.color.lt_pink),
                getColor(R.color.dk_cyan),
                getColor(R.color.dk_green)
            )
            .setDirection(0.0, 359.0)
            .setSpeed(0f, 10f)
            .setFadeOutEnabled(true)
            .setTimeToLive(500L)
            .addShapes(Shape.RECT, Shape.CIRCLE)
            .addSizes(Size(12, 5F))
            .setPosition(
                fireCrackerView.x + fireCrackerView.width / 2,
                fireCrackerView.y + fireCrackerView.height / 5
            )
            .burst(150)
    }

    private fun getColor(colorId: Int) =
        binder.rootView.context.getColor(colorId)


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
                                        if (position != null) onSwipe("left", position)

                                    }

                                    //오른쪽 swipe로 끝지점에 도달
                                    else if (v.x == maxRight) {
                                        resetLocation(v)
                                        VibrateHelper.start()
                                        result = false

                                        val position = mLayoutManager?.getPosition(v)
                                        if (position != null) onSwipe("right", position)
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

            private fun onSwipe(direction: String, position: Int) {
                when (direction) {
                    "right" -> {
                        if (checkPlusDirectToRight()) planVM.updateCountByIndex(1, position)
                        else planVM.updateCountByIndex(-1, position)
                    }
                    "left" -> {
                        if (checkPlusDirectToRight()) planVM.updateCountByIndex(-1, position)
                        else planVM.updateCountByIndex(1, position)
                    }
                }
            }

            private fun checkPlusDirectToRight(): Boolean =
                AppConfig.swipeDirectionToRight


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
class PlanListAdapter(
    private val planVM: PlannerViewModel,
    private val mLifecycleOwner: LifecycleOwner
) :
    RecyclerView.Adapter<PlanListAdapter.PlanListVH>(),
    ItemTouchHelperCallback.ItemTouchHelperListener {

    private val curDataList = mutableListOf<PlanData>()

    private lateinit var binder: ControllerPlannerItemBinding

    fun updateDiffItems(newDatalist: List<PlanData>?) {
        if (newDatalist == null) return

        val callback = CommonDiffUtil(
            curDataList,
            newDatalist
        )
        val result = DiffUtil.calculateDiff(callback)

        this.curDataList.clear()
        this.curDataList.addAll(newDatalist)
        result.dispatchUpdatesTo(this)
    }

    fun updateAllItems(newDatalist: List<PlanData>?) {
        if (newDatalist == null) return

        this.curDataList.clear()
        this.curDataList.addAll(newDatalist)

        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlanListVH {
        binder = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.controller_planner_item,
            parent,
            false
        )
        binder.lifecycleOwner = mLifecycleOwner
        binder.vm = planVM
        return PlanListVH(binder)
    }

    override fun getItemCount(): Int = curDataList.size


    override fun onBindViewHolder(holder: PlanListVH, position: Int) {
        holder.bind()
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


    inner class PlanListVH(private val binder: ControllerPlannerItemBinding) :
        RecyclerView.ViewHolder(binder.root) {

        fun bind() {
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