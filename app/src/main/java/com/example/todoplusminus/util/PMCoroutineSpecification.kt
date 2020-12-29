package com.example.todoplusminus.util

import kotlinx.coroutines.Dispatchers

object PMCoroutineSpecification {

    val MAIN_DISPATCHER = Dispatchers.Main

    val IO_DISPATCHER = Dispatchers.IO

    val DEFAULT_DISPATCHER = Dispatchers.Default
}