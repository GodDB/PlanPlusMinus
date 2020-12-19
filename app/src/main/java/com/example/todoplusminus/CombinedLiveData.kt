package com.example.todoplusminus

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData

/**
 * 타입이 다른 두 라이브데이터의 변경을 감지하기 위한 object
 *
 * 두 라이브 데이터 중 하나라도 변경사항이 발생하면 알림을 받는다.
 * */
class TwoCombinedLiveData<A, B, C>(
    private val liveData1: LiveData<A>,
    private val liveData2: LiveData<B>,
    private val action: (A, B) -> C
) :
    MediatorLiveData<C>() {

    private var a: A? = null
    private var b: B? = null

    init {
        addSource(liveData1) { a ->
            this.a = a
            if (b != null) this.value = action(a, b!!)
        }

        addSource(liveData2) { b ->
            this.b = b
            if (a != null) this.value = action(a!!, b)
        }

    }
}


/**
 * 타입이 다른 3개의 라이브데이터의 변경을 감지하기 위한 object
 *
 * 3개의 라이브 데이터 중 하나라도 변경사항이 발생하면 알림을 받는다.
 * */
class ThreeCombinedLiveData<A, B, C, D>(
    private val liveData1: LiveData<A>,
    private val liveData2: LiveData<B>,
    private val livedata3: LiveData<C>,
    private val action: (A, B, C) -> D
) :
    MediatorLiveData<D>() {

    private var a: A? = null
    private var b: B? = null
    private var c: C? = null

    init {
        addSource(liveData1) { a ->
            this.a = a
            if (b != null && c != null) this.value = action(a, b!!, c!!)
        }

        addSource(liveData2) { b ->
            this.b = b
            if (a != null && c != null) this.value = action(a!!, b, c!!)
        }

        addSource(livedata3) { c ->
            this.c = c
            if (a != null && b != null) this.value = action(a!!, b!!, c)
        }

    }
}
