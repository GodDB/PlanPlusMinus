package com.example.todoplusminus.data.repository

import com.example.todoplusminus.data.entities.*
import com.example.todoplusminus.util.PMCoroutineSpecification
import com.example.todoplusminus.data.source.local.db.PlannerInfoEntity
import com.example.todoplusminus.data.source.local.db.PlannerItemEntity
import com.example.todoplusminus.data.source.local.ILocalDataSource
import com.example.todoplusminus.data.source.file.SharedPrefManager
import com.example.todoplusminus.data.source.local.db.PlannerAlarmEntity
import com.example.todoplusminus.data.source.local.db.PlannerItemAlarm
import com.example.todoplusminus.util.AlarmManagerHelper
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.time.LocalDate
import javax.inject.Inject


class PlannerRepository @Inject constructor(
    private val localSource: ILocalDataSource,
    private val sharedPrefManager: SharedPrefManager,
    private val alarmHelper: AlarmManagerHelper,
    private val dispatcher: CoroutineDispatcher = PMCoroutineSpecification.IO_DISPATCHER
) : IPlannerRepository {

    /**
     * initialize function
     * */

    override suspend fun refreshPlannerData(date: LocalDate) {
        //전달 받은 날짜에 해당하는 info데이터가 있는지 확인한다.
        //즉 전달받은 낧짜에 info 데이터가 없다는 것은 2가지를 의미한다.

        //1. 사용자가 첫 설치해서, 등록한 데이터가 없는 경우 - 이 경우는 처리하지 않는다.
        //2. 새로운 날짜가 되어, 그에 대응되는 info 테이블이 없는 경우 - 이 경우를 처리하기 위함
        //(info, item을 조인해서 데이터를 가져오기 때문에 항상 해당 날짜에 info 데이터가 있어야 한다.)
        withContext(dispatcher) {
            if (!checkWhetherExistInfoDataByDate(date)) {

                //2번째 케이스에 대응된다.
                //planItem의 데이터를 가져와서
                val itemList = getAllPlanItem()
                //planItem이 존재한다면 2번케이스에 대응되는 것.
                if (itemList.isNotEmpty()) {
                    itemList.forEach {
                        val info = generateInfoData(it.id, date)
                        localSource.insertPlanInfo(info)
                    }
                }
            }
        }
    }

    override fun getAllPlanProject(): Flow<PlanProject> =
        localSource.getAllPlannerData().map { PlanProject.create(it) }

    override fun getAllPlanMemo(): Flow<MutableList<PlanMemo>> =
        localSource.getAllPlanMemo()

    override fun getAllPlanDataByDate(date: LocalDate): Flow<MutableList<PlanData>> =
        localSource.getAllPlannerDataByDate(date)

    override fun getPlanProjectById(id: BaseID): Flow<PlanProject> =
        localSource.getAllPlannerDataById(id)
            .map { PlanProject.create(it) }

    override fun getLastestIndex(): Flow<Int?> = localSource.getLastestIndex()

    override fun getMemoByDate(date: LocalDate): Flow<PlanMemo> =
        localSource.getMemoByDate(date)

    override fun getAlarmDataListByPlanId(planId: BaseID): Flow<List<PlanAlarmData>> {
        return localSource.getAlarmDataListByPlanId(planId).map {
            convertAlarmItemListToAlarmDataList(it)
        }
    }

    override fun getAlarmData(alarmId: Int): Flow<PlanAlarmData?> {
        return localSource.getAlarmData(alarmId).map {
            it?.let { convertAlarmItemToAlarmData(it) }
        }
    }

    override fun getLatestAlarmId(): Flow<Int?> {
        return localSource.getLatestAlarmId()
    }

    override fun getPlannerTitle(planId: BaseID): Flow<String> {
        return localSource.getPlannerTitle(planId)
    }

    override suspend fun insertAlarmData(alarmData: PlanAlarmData) {
        withContext(dispatcher) {
            registerRepeatAlarm(alarmData)
            val alarmEntity = convertAlarmDataToAlarmEntity(alarmData)
            localSource.insertAlarmData(alarmEntity)
        }
    }

    override suspend fun updateAlarmData(oldAlarmData: PlanAlarmData, newAlarmData: PlanAlarmData) {
        withContext(dispatcher) {
            unregisterAlarm(oldAlarmData)
            registerRepeatAlarm(newAlarmData)
            val alarmEntity = convertAlarmDataToAlarmEntity(newAlarmData)
            localSource.updateAlarmData(alarmEntity)
        }
    }

    override suspend fun deleteAlarmData(alarmData: PlanAlarmData) {
        withContext(dispatcher) {
            unregisterAlarm(alarmData)
            localSource.deleteAlarmDataById(alarmData.alarmId)
        }
    }

    override suspend fun deleteMemoByDate(date: LocalDate) {
        withContext(dispatcher) {
            localSource.deleteMemoByDate(date)
        }
    }

    override suspend fun deletePlannerDataById(id: BaseID) {
        //알람을 제거한다.
        getAlarmDataListByPlanId(id)
            .take(1)
            .collect { alarmDataList ->
                unregisterAlarmList(alarmDataList)
            }

        localSource.deletePlannerDataById(id)
    }

    override suspend fun insertPlannerData(planData: PlanData) {
        coroutineScope {
            launch(dispatcher) {
                localSource.insertPlannerData(planData)
            }
        }
    }

    override suspend fun updatePlannerDataList(dataList: List<PlanData>) {
        localSource.updatePlannerDataList(dataList)
    }

    override suspend fun updatePlannerData(data: PlanData) {
        localSource.updatePlannerData(data)
    }

    override suspend fun getPlannerDataById(id: BaseID): PlanData =
        withContext(dispatcher) {
            localSource.getPlannerDataById(id)
        }

    override suspend fun deleteAndUpdateAll(deleteTarget: PlanData, updateTarget: List<PlanData>) {
        localSource.deleteAndUpdateAll(deleteTarget, updateTarget)
    }

    override suspend fun updateTitleBgById(id: BaseID, title: String, bgColor: Int) {
        coroutineScope {
            launch(dispatcher) {
                localSource.updateTitleBgById(id, title, bgColor)
            }
        }
    }

    override suspend fun updatePlanMemo(memo: PlanMemo) {
        localSource.updatePlanMemo(memo)
    }

    override suspend fun insertPlanMemo(memo: PlanMemo) {
        coroutineScope {
            launch(dispatcher) {
                localSource.insertPlanMemo(memo)
            }
        }
    }


    //tooltip관련 메소드 ------------------------

    override fun checkShowTooltip1(): Flow<Boolean> {
        return sharedPrefManager.checkShowTooltip1()
    }

    override fun checkShowTooltip2(): Flow<Boolean> {
        return sharedPrefManager.checkShowTooltip2()
    }

    override fun checkShowTooltip3(): Flow<Boolean> {
        return sharedPrefManager.checkShowTooltip3()
    }

    override fun checkShowTooltip4(): Flow<Boolean> {
        return sharedPrefManager.checkShowTooltip4()
    }

    override fun enabledTooltip1() {
        sharedPrefManager.enabledTooltip1()
    }

    override fun enabledTooltip2() {
        sharedPrefManager.enabledTooltip2()
    }

    override fun enabledTooltip3() {
        sharedPrefManager.enabledTooltip3()
    }

    override fun enabledTooltip4() {
        sharedPrefManager.enabledTooltip4()
    }


    /**
     * 전달 받은 date값과 대응되는 info data가 있는지를 확인한다.
     *
     * 일반적으론 사용자가 플랜을 만들었을 때 PlanInfo가 생성되지만, 새로운 날짜가 되었을 때는 그에 대응되는 info데이터가 없으므로
     * info데이터가 있는지를 확인한다.
     *
     * true면 데이터가 있는 것, false면 데이터가 없는 것이다.
     * */
    private suspend fun checkWhetherExistInfoDataByDate(date: LocalDate): Boolean =
        localSource.getAllPlanInfoByDate(date).isNotEmpty()


    /**
     * item data가 있는지를 확인한다.
     *
     * item은 사용자가 등록한 플랜에 대한 메타데이터이며( 플랜 이름 등), 날짜와 무관하게 관리된다.
     * 그러므로 이 데이터가 존재하지 않는다는 것은 사용자가 처음 이 앱을 사용하는 상황일 것이다... (만약 다 삭제한게 아니라)
     *
     * true면 데이터가 있는 것, false면 데이터가 없는 것이다.
     * */
    private suspend fun getAllPlanItem(): List<PlannerItemEntity> =
        localSource.getAllPlanItem()

    private fun generateInfoData(id: BaseID, date: LocalDate) =
        PlannerInfoEntity(0, date, 0, id)

    private fun unregisterAlarmList(alarmDataList: List<PlanAlarmData>) {
        alarmDataList.forEach {
            unregisterAlarm(it)
        }
    }

    private fun registerRepeatAlarm(alarmData: PlanAlarmData) {
        alarmHelper.registerAlarm(alarmData, AlarmManagerHelper.TYPE_REPEAT)
    }

    private fun unregisterAlarm(alarmData: PlanAlarmData) {
        alarmHelper.unregisterAlarm(alarmData)
    }

    companion object {
        fun convertAlarmItemListToAlarmDataList(alarmItemList: List<PlannerItemAlarm>): List<PlanAlarmData> {
            return alarmItemList.map {
                convertAlarmItemToAlarmData(it)
            }
        }

        fun convertAlarmItemToAlarmData(alarmItem: PlannerItemAlarm): PlanAlarmData {
            return PlanAlarmData(
                planId = alarmItem.planItem.id,
                planTitle = alarmItem.planItem.title,
                alarmId = alarmItem.planAlarm.alarmId,
                alarmTime = alarmItem.planAlarm.alarmTime,
                alarmRepeatMonday = alarmItem.planAlarm.alarmMonday,
                alarmRepeatTuesday = alarmItem.planAlarm.alarmTuesday,
                alarmRepeatWednesday = alarmItem.planAlarm.alarmWednesday,
                alarmRepeatThursday = alarmItem.planAlarm.alarmThursday,
                alarmRepeatFriday = alarmItem.planAlarm.alarmFriday,
                alarmRepeatSaturday = alarmItem.planAlarm.alarmSaturday,
                alarmRepeatSunday = alarmItem.planAlarm.alarmSunday
            )
        }

        fun convertAlarmDataToAlarmEntity(alarmData: PlanAlarmData): PlannerAlarmEntity {
            return PlannerAlarmEntity(
                alarmId = alarmData.alarmId,
                alarmTime = alarmData.alarmTime,
                alarmMonday = alarmData.alarmRepeatMonday,
                alarmTuesday = alarmData.alarmRepeatTuesday,
                alarmWednesday = alarmData.alarmRepeatWednesday,
                alarmThursday = alarmData.alarmRepeatThursday,
                alarmFriday = alarmData.alarmRepeatFriday,
                alarmSaturday = alarmData.alarmRepeatSaturday,
                alarmSunday = alarmData.alarmRepeatSunday,
                planId = alarmData.planId
            )
        }
    }
}
