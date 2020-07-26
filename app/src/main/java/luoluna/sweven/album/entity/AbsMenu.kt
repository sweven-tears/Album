package luoluna.sweven.album.entity

import java.lang.reflect.Method

/**
 * Created by Sweven on 2020/7/25--8:55.
 * Email: sweventears@163.com
 */
abstract class AbsMenu protected constructor(val menu: String, val method: String) {
    private val _method: Method?

    fun execute(vararg obj: Any?): Any? {
        try {
            if (this._method == null)
                throw NoSuchMethodException(javaClass.name + "." + this.method + "()")
            val method: Method = this._method
            method.isAccessible = true
            return method.invoke(this, *obj)
        } catch (e: Exception) {
            throw e
        }
    }

    operator fun invoke(name: String, vararg obj: Any?): Any? {
        val method = findMethod(name, 0)
        try {
            if (method == null)
                throw NoSuchMethodException(javaClass.name + "." + name + "()")
            method.isAccessible = true
            return method.invoke(this, *obj)
        } catch (e: Exception) {
            throw e
        }
    }

    fun <T> getField(name: String, defValue: T): T? {
        return try {
            val field = javaClass.getDeclaredField(name)
            field[this] as T
        } catch (e: Exception) {
            defValue
        }
    }

    fun <T> setField(name: String, value: T): Boolean {
        try {
            val field = javaClass.getDeclaredField(name)
            field.isAccessible = true
            field[this] = value
            return true
        } catch (e: Exception) {
            throw e
        }
    }

    /**
     * @param type 0:无参;1:有参
     * @return [Method]
     */
    private fun findMethod(methodName: String, type: Int): Method? {
        try {
            if (type == 0) {
                return javaClass.getDeclaredMethod(methodName)
            } else if (type == 1) {
                for (declaredMethod in javaClass.declaredMethods) {
                    if (declaredMethod.name == methodName) {
                        return declaredMethod
                    }
                }
                throw NoSuchMethodException("${javaClass.name}.$methodName()")
            }
        } catch (e: Exception) {
            if (type == 0) {
                return findMethod(methodName, 1)
            }
            throw e
        }
        throw Exception()
    }

    init {
        this._method = findMethod(this.method, 0)
    }
}