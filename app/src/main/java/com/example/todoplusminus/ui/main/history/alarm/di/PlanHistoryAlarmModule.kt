package com.example.todoplusminus.ui.main.history.alarm.di

import android.content.Context
import com.example.todoplusminus.data.entities.BaseID
import com.example.todoplusminus.data.entities.PlanAlarmData
import com.example.todoplusminus.data.repository.IPlannerRepository
import com.example.todoplusminus.ui.main.history.alarm.BaseHistoryAlarmVM
import com.example.todoplusminus.ui.main.history.alarm.PlanHistoryAlarmCreateMode
import com.example.todoplusminus.ui.main.history.alarm.PlanHistoryAlarmUpdateMode
import com.example.todoplusminus.util.AlarmManagerHelper
import dagger.Module
import dagger.Provides

@Module
class PlanHistoryAlarmModule {

    @Provides
    fun provideAlarmVM(planId : BaseID, alarmId : Int, repo : IPlannerRepository) : BaseHistoryAlarmVM {
        if(alarmId == PlanAlarmData.NONE_ALARM_ID) return provideAlarmVMCreateMode(planId, repo)
        return provideAlarmVMUpdateMode(planId, alarmId, repo)
    }

    private fun provideAlarmVMCreateMode(planId: BaseID, repo: IPlannerRepository) : BaseHistoryAlarmVM{
        return PlanHistoryAlarmCreateMode(planId, repo)
    }

    private fun provideAlarmVMUpdateMode(planId: BaseID, alarmId: Int, repo: IPlannerRepository) : BaseHistoryAlarmVM{
        return PlanHistoryAlarmUpdateMode(planId, alarmId, repo)
    }

    @Provides
    fun provideAlarmHelper(context : Context) : AlarmManagerHelper{
        return AlarmManagerHelper(context)
    }
}