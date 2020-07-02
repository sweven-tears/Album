package com.sweven.socket.launch

import com.sweven.socket.client.IClient
import com.sweven.socket.client.SocketClient
import java.net.ConnectException
import java.util.*

object Client001 : IClient {
    val client = SocketClient()
    const val name = "sweven"

    @JvmStatic
    fun main(args: Array<String>) {
        client.addClientListener(this)
        client.connect("192.168.2.13", 8080)
        val scanner = Scanner(System.`in`)
        var msg = scanner.nextLine()
        while (msg != "exit") {
            if (msg == "/help") {
                println("rename@ 改名")
                println("nearby@ 查找网络上的人")
                println("forward@name@ 给昵称为name的人发送消息")
            } else if (msg == "/connect") {
                client.connect("192.168.2.13", 8080)
            } else {
                client.writeUTF(msg)
            }
            msg = scanner.nextLine()
        }

    }

    override fun onSign(): String {
        return name
    }

    override fun readUTF(msg: String) {
        when {
            msg.startsWith("rename^") -> { // 改名反馈
                println(msg.substring("rename^".length))
            }
            msg.startsWith("nearby^") -> {
                val array = msg.substring("nearby^".length)
                println(array)
            }
            msg.startsWith("forward^") -> {
                val temp = msg.substring("forward^".length)
                val index = temp.indexOf("^")
                val message = temp.substring(index + 1)
                println("receive msg form ${temp.substring(0, index)} :$message")
            }
        }
    }

    override fun reconnectByConnect(e: ConnectException?): Int {
        return 10
    }
}