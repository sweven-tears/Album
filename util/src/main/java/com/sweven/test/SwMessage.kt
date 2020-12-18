package com.sweven.test

import android.os.Handler
import android.os.Message

/**
 * Created by Sweven on 2020/8/7--16:30.
 */
object SwMessage {
    private val list = arrayListOf<SwBean>()
    private val handler = SHandler()

    fun sendMessage(key: String, value: Any) {
        val msg = SwBean(key, value)
        val message = Message()
        message.obj = msg
        handler.sendMessage(message)
    }

    @JvmStatic
    fun addMessageListener(listener: OnMessageListener, vararg key: String) {
        for (s in key) {
            add(s, listener)
        }
    }

    @JvmStatic
    fun removeMessageListener(key: String) {
        remove(key)
    }

    @JvmStatic
    fun removeMessageListener(listener: OnMessageListener) {
        remove(listener)
    }

    private fun add(key: String, listener: OnMessageListener) {
        if (key.isEmpty()) return
        val bean = SwBean(key, listener)
        list.add(bean)
    }

    private fun remove(key: String) {
        if (key.isEmpty()) return
        for (swBean in list) {
            if (swBean.key === key) {
                list.remove(swBean)
                break
            }
        }
    }

    private fun remove(listener: OnMessageListener) {
        for (swBean in list) {
            if (swBean.obj == listener) {
                list.remove(swBean)
                break
            }
        }
    }


    private class SHandler : Handler() {
        override fun handleMessage(msg: Message) {
            val swBean = msg.obj as SwBean
            for (bean in list) {
                if (bean.key == swBean.key) {
                    (bean.obj as OnMessageListener).onMessage(swBean.obj)
                }
            }
        }
    }

    private class SwBean(var key: String, var obj: Any)

    interface OnMessageListener {
        fun onMessage(data: Any?)
    }
}