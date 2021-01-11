package com.example.todoplusminus.util

import android.content.SharedPreferences
import android.util.Log
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector

import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty


//extension function (preference -> Flow)
fun <T>Preference<T>.asFlow()
        = PreferenceFlow(this)


/**
 * preference의 변경을 전달받아 데이터 스트림을 전달하는 flow
 * */
class PreferenceFlow<T>(
    private val preference : Preference<T>
) : Flow<T> {

    private val prefValue : T by preference

    private val channel: Channel<T> = Channel(Channel.CONFLATED)

    private val prefListener =  SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
        if (key == preference.key) {
            channel.offer(prefValue)
        }
    }

    @InternalCoroutinesApi
    override suspend fun collect(collector: FlowCollector<T>) {
        preference.preferences.registerOnSharedPreferenceChangeListener(prefListener)
        //초기 value emit
        collector.emit(prefValue)

        try {
            for (value in channel) { //suspending...
                collector.emit(value)
            }
        } finally {
            preference.preferences.unregisterOnSharedPreferenceChangeListener(prefListener)
            channel.close()
        }
    }
}

/**
 * Preference flow를 사용하기 위한 기본 데이터 structure
 * */
abstract class Preference<T>(
    open val preferences: SharedPreferences,
    open val key: String
) : ReadOnlyProperty<Any, T>

class StringPreference(
    override val preferences: SharedPreferences,
    override val key: String,
    private val defaultValue: String
) : Preference<String>(preferences, key) {
    override fun getValue(thisRef: Any, property: KProperty<*>): String {
        return preferences.getString(key, defaultValue) ?: ""
    }
}

class BooleanPreference(
    override val preferences: SharedPreferences,
    override val key : String,
    private val defaultValue : Boolean
) : Preference<Boolean>(preferences, key){
    override fun getValue(thisRef: Any, property: KProperty<*>): Boolean {
        return preferences.getBoolean(key, defaultValue)
    }
}