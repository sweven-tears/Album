package com.sweven.console

import android.util.Log

/**
 * <p>Create by Sweven on 2020/7/9 -- 10:12</p>
 * Email: sweventears@163.com
 */
object Log {
    var DEBUG: Boolean = false

    @JvmStatic
    fun v(tag: String, vararg obj: String) {
        if (!DEBUG) return
        Log.v(tag, msg(obj))
    }

    @JvmStatic
    fun d(tag: String, vararg obj: String) {
        if (!DEBUG) return
        Log.d(tag, msg(obj))
    }

    @JvmStatic
    fun i(tag: String, vararg obj: String) {
        if (!DEBUG) return
        Log.i(tag, msg(obj))
    }

    @JvmStatic
    fun w(tag: String, vararg obj: String) {
        if (!DEBUG) return
        Log.w(tag, msg(obj))
    }

    @JvmStatic
    fun e(tag: String, vararg obj: String) {
        if (!DEBUG) return
        Log.e(tag, msg(obj))
    }

    @JvmStatic
    fun wft(tag: String, vararg obj: String) {
        if (!DEBUG) return
        Log.wtf(tag, msg(obj))
    }

    private fun msg(obj: Array<out String>?): String {
        if (obj == null) return ""
        val builder = StringBuilder()
        for (s in obj) {
            builder.append(s)
            builder.append(" ")
        }
        return builder.toString().trim()
    }
}