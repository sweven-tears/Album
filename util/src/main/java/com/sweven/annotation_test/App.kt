package com.sweven.annotation_test

import android.os.Build

/**
 * 测试用例
 * <p>Create by Sweven on 2020/7/3 -- 17:25</p>
 * Email: sweventears@163.com
 */

fun main() {
    val app = App()
    System.err.println("app.b = ${app.student?.name}")
}


@Say("app create")
class App {

    @Say("a is int")
    var a = 12

    var b = true

    @Say("student is Object")
    var student: Student? = null

    @Say
    constructor(name: String) {
        if (student != null) {
            this.student!!.name = name
        }
    }

    @Say("this is a constructor")
    constructor() {
        Inject.init(this)
    }

    @Say("this is method")
    fun init(student: Student): Student {
        return student
    }


}

object Inject {
    fun init(app: App) {
        injectType(app)
        injectField(app)
        injectMethod(app)
        injectConstructor(app)
    }

    private fun injectConstructor(app: App) {
        for (declaredConstructor in app.javaClass.declaredConstructors) {
            val say = declaredConstructor.getAnnotation(Say::class.java)
            if (say != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    val parameters = declaredConstructor.parameters
                    if (parameters.isNotEmpty()) {
                        var type = parameters[0].type
                        val `in` = App("haiya")
                    }
                }
                println(say.value)
            }
        }
    }

    private fun injectMethod(app: App) {
        for (declaredMethod in app.javaClass.declaredMethods) {
            if (declaredMethod.name.endsWith("\$annotations")) continue
            val say = declaredMethod.getAnnotation(Say::class.java)
            if (say != null) {
                println(declaredMethod.name + ":" + say.value)
            }
        }
    }

    private fun injectField(app: App) {
        for (declaredField in app.javaClass.declaredFields) {
            try {
                val declaredMethod = app.javaClass.getDeclaredMethod("${declaredField.name}\$annotations")
                val say = declaredMethod.getAnnotation(Say::class.java)
                if (say != null) {
                    if (declaredField.type == Student::class.java) {
                        val haha = app.init(Student("haha", 22))
                        declaredField.isAccessible = true
                        declaredField.set(app, haha)
                    }
                    println(say.value)
                }

            } catch (e: NoSuchMethodException) {
                continue
            }

        }
    }

    private fun injectType(app: App) {
        val say = app.javaClass.getAnnotation(Say::class.java)
        if (say != null) {
            println(say.value)
        }
    }
}

@Retention(AnnotationRetention.RUNTIME)
annotation class Say(val value: String = "null")

class Student(var name: String, var age: Int)