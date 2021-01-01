package com.example.todoplusminus.util

//context에 의존적인 것들에 대한 id wrapper
//가독성을 위해서 사용한다.
sealed class ContextID(open var id : Int)

//String id값을 저장시키기 위한 wrapper 클래스
data class StringID(override var id : Int) : ContextID(id)

//Color id값을 저장시키기 위한 wrapper 클래스
data class ColorID(override var id : Int) : ContextID(id)