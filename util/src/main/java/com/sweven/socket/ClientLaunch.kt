package com.sweven.socket

import java.util.*

object ClientLaunch {
    @JvmStatic
    fun main(args: Array<String>) {
        val socketClient = SocketClient()
        val user = "user001"
        socketClient.connect("127.0.0.1", 80, object : SocketClient.ConnectListener {
            override fun fail() {

            }

            override fun success() {
                socketClient.send("user@$user")
            }

        })
        val scanner = Scanner(System.`in`)
        var msg = scanner.nextLine()
        while (msg != "exit") {
            if (msg.startsWith("sys/")) {
                socketClient.send(msg)
            } else if (msg.startsWith("002/")) {
                socketClient.send(msg)
            }
            msg = scanner.nextLine()
        }
    }
}