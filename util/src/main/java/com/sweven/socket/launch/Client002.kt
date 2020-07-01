package com.sweven.socket.launch

import com.sweven.console.Console
import com.sweven.socket.client.IClient
import com.sweven.socket.client.SocketClient
import java.util.*

object Client002 {
    @JvmStatic
    fun main(args: Array<String>) {
        val client = SocketClient()
        val name = "jia"
        client.addClientListener(object : IClient {
            override fun onReceive(msg: String) {
                Console.err("receive msg : $msg")
                if (msg.startsWith("rename@")) { // 要求改名
                    client.writeUTF("rename@$name")
                } else if (msg.startsWith("rename^")) { // 改名反馈
                    println(msg.substring("rename^".length))
                } else if (msg.startsWith("nearby^")) {
                    val array = msg.substring("nearby^".length)
                    println(array)
                } else if (msg.startsWith("forward^")) {
                    val temp = msg.substring("forward^".length)
                    val index = temp.indexOf("^")
                    val name = temp.substring(0, index)
                    val message = temp.substring(index + 1)
                    println("receive msg form $name :$message")
                }
            }

            override fun onStart() {
                println("connected.")
            }

            override fun readUTF(no: Int, msg: String?) {
            }

        })
        client.connect("localhost", 80)
        val scanner = Scanner(System.`in`)
        var msg = scanner.nextLine()
        while (msg != "exit") {
            if (msg == "/help") {
                println("rename@ 改名")
                println("nearby@ 查找网络上的人")
                println("forward@name@ 给昵称为name的人发送消息")
            } else {
                client.writeUTF(msg)
            }
            msg = scanner.nextLine()
        }
        client.close()
        scanner.close()
    }
}