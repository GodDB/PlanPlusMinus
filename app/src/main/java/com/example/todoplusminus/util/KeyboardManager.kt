package com.example.todoplusminus.util

import android.accessibilityservice.AccessibilityService
import android.app.Service
import android.content.Context
import android.os.Handler
import android.os.Message
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import java.util.*
import java.util.concurrent.atomic.AtomicBoolean

object KeyboardManager {

    private lateinit var mSoftKeyboard : AccessibilityService.SoftKeyboardController

    fun setUp(context : Context){
        val inputManager = context.getSystemService(Service.INPUT_METHOD_SERVICE)
    }
}

