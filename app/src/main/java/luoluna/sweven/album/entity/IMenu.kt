package luoluna.sweven.album.entity

/**
 * Created by Sweven on 2020/7/25--17:49.
 * Email: sweventears@163.com
 */
class IMenu<T>(val menu: String, private val method: Method<T>) {
    fun execute(vararg obj: Any?): T {
        return method.invoke(*obj)
    }

    interface Method<T> {
        operator fun invoke(vararg obj: Any?): T
    }

}