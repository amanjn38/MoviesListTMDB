package com.finance.networksdk

import android.util.Log

internal object HttpLogger {
    private var isLogsRequired = true
    fun d(tag: String?, message: String?) {
        if (isLogsRequired) Log.d(tag, message!!)
    }

    fun e(tag: String?, message: String?) {
        if (isLogsRequired) Log.e(tag, message!!)
    }
}