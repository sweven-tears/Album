package com.sweven

/**
 * <p>Create by Sweven on 2020/7/10 -- 17:44</p>
 * Email: sweventears@163.com
 */
fun main() {
    val a = AImpl(33)
    Invoke(a).print()
}

class Invoke(a: A) : A by a

class AImpl(var a: Int) : A {
    override fun print() {
        println(a)
    }
}

interface A {
    fun print()
}
