package com.sweven.socket.launch

import com.sweven.socket.service.IService
import com.sweven.socket.service.SocketService
import java.util.*
import kotlin.collections.ArrayList

object Service : IService {
    private var service = SocketService("192.168.2.13", 8080)
    private val persons: MutableList<Person> = ArrayList()

    @JvmStatic
    fun main(args: Array<String>) {
        service.addServiceListener(this)
        service.start()
        val scanner = Scanner(System.`in`)
        var msg = scanner.nextLine()
        while (msg != "exit") {
            if (msg=="/restart"){
                service.close()
                service.start()
            }else {
                deal(msg)
            }
            msg = scanner.nextLine()
        }
        service.close()
        scanner.close()
    }

    private fun onReceiveDeal(no: Int, msg: String) {

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

    override fun readUTF(port: Int, msg: String) {
        println("client $port push message：$msg")
        if (msg.startsWith("rename@")) { //改名指令
            val name = msg.substring("rename@".length)
            val person = Person(port, name)
            fixName(person)
            service.specifySend(port, "rename^fix name:$name success!")
        } else if (msg.startsWith("nearby@")) { // 查找其他用户指令
            var nearby = ""
            persons.forEach {
                nearby += "${it.name}  "
            }
            service.specifySend(port, "nearby^${nearby.trim()}")
        } else if (msg.startsWith("forward@")) { // 给其他人发消息指令
            val temp = msg.substring("forward@".length)
            val index = temp.indexOf("@")
            val toName = temp.substring(0, index)
            val toNo = findNoByName(toName)
            val message = temp.substring(index + 1)
            service.specifySend(toNo, "forward^${if (getName(port) == null) "" else getName(port)}^$message")
        }
    }

    override fun onConnected() {
        println("service launching......")
    }

    override fun onAccept(port: Int) {
        println("client $port online.")
        persons.add(Person(port, "$port"))
    }

    override fun onDrops(port: Int) {
        for (i in persons.size - 1 downTo 0) {
            if (persons[i].id == port) {
                println("client ${persons[i].name} offline.")
                persons.removeAt(i)
                break
            }
        }
    }

    class Person(var id: Int, var name: String)
}