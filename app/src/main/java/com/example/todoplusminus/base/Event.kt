package com.example.todoplusminus.base

/** livedata + Viewmodel의 이중 처리를 막기 위한 event wrapper class
 *  livedata의 타입에 wrapping하여 사용한다.
 *
 * 사용 예)
 * livedata의 flag를 이용해서 view를 갱신할 경우 livedata.value = true 후에 livedata.value = false로
 * 다시 변경해주는 이중 작업을 해야할 경우에 사용하면
 * livedata.value = true후 false로 값을 변경해주지 않아도 된다.
 * (hasBeenHandled flag를 통해 이중처리를 막는다.)
 * */
open class Event<T>(private val content: T) {

    //이벤트가 처리 되었는가?
    var hasBeenHandled = false
        private set

    //이벤트를 가져오기 위한 api
    fun getContentIfNotHandled(): T? =
        //이벤트가 이미 처리되었다면 null
        if (hasBeenHandled) null
        //이벤트가 처리되지 않았다면 값을 반환한다.
        else {
            hasBeenHandled = true
            content
        }

    /** 이벤트 처리여부와 관련없이 현재 라이브데이터가 가지고 있는 데이터를 가져오는 api*/
    fun peekContent() : T = content
}