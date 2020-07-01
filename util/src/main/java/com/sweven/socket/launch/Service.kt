package com.sweven.socket.launch

import com.sweven.socket.service.IService
import com.sweven.socket.service.SocketService
import java.util.*
import kotlin.collections.ArrayList

object Service {
    private var service = SocketService()
    private val persons: MutableList<Person> = ArrayList()

    @JvmStatic
    fun main(args: Array<String>) {
        service.addServiceListener(object : IService {
            override fun readUTF(no: Int, msg: String) {
                onReceiveDeal(no, msg)
            }

            override fun onAccept(no: Int) {
                println("client $no online.")
                service.specifySend(no, "rename@")
                persons.add(Person(no, if (no < 10) "00$no" else if (no < 100) "0$no" else "$no"))
            }

            override fun onStart() {

            }

            override fun onDrops(no: Int) {
                for (i in persons.size - 1 downTo 0) {
                    if (persons[i].id == no) {
                        persons.removeAt(i)
                        break
                    }
                }
            }
        })
        service.start()
        println("service launching......")
        val scanner = Scanner(System.`in`)
        var msg = scanner.nextLine()
        while (msg != "exit") {
            deal(msg)
            msg = scanner.nextLine()
        }
    }

    private fun onReceiveDeal(no: Int, msg: String) {
        println("client $no push message：$msg")
        if (msg.startsWith("rename@")) { //改名指令
            val name = msg.substring("rename@".length)
            val person = Person(no, name)
            fixName(person)
            service.specifySend(no, "rename^fix name:$name success!")
        } else if (msg.startsWith("nearby@")) { // 查找其他用户指令
            var nearby = ""
            persons.forEach {
                nearby += "${it.name}  "
            }
            service.specifySend(no, "nearby^${nearby.trim()}")
        } else if (msg.startsWith("forward@")) { // 给其他人发消息指令
            val temp = msg.substring("forward@".length)
            val index = temp.indexOf("@")
            val toName = temp.substring(0, index)
            val toNo = findNoByName(toName)
            val message = temp.substring(index + 1)
            service.specifySend(toNo, "forward^${if (getName(no) == null) "" else getName(no)}^$message")
        }
    }

    private fun findNoByName(name: String): Int {
        persons.forEach {
            if (it.name == name) {
                return it.id
            }
        }
        return 0
    }

    /**
     * fix name.
     */
    private fun fixName(person: Person) {
        for (i in 0 until persons.size) {
            if (persons[i].id == person.id) {
                persons[i].name = person.name
            }
        }
    }

    private fun deal(msg: String) {
        val arg: String
        if (msg.startsWith("cmd@")) {
            arg = msg.substring("cmd@".length)
            if (arg == "all")
                showClientList()
        } else if (msg.startsWith("all@")) {
            service.send(msg.substring("all@".length))
        } else {
            try {
                val temp = msg.substring(0, msg.indexOf("@"))
                val no = temp.toInt()
                arg = msg.substring(temp.length + 1)
                service.specifySend(no, arg)
            } catch (e: Exception) {
                System.err.println("The format is incorrect!")
            }
        }
    }

    private fun showClientList() {
        val clients = service.allClient
        println("present client has ${clients.size}.")
        for (client in clients) {
            println(client)
        }
    }

    private fun getName(no: Int): String? {
        for (person in persons) {
            if (person.id == no) {
                return person.name
            }
        }
        return null
    }

    class Person(var id: Int, var name: String)
}