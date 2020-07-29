package luoluna.sweven.album.entity

/**
 * Created by Sweven on 2020/7/25--17:49.
 * Email: sweventears@163.com
 */
class IMenu<R, T>(val menu: String, private val method: Method<R, T>) {
    fun execute(vararg params: R): T {
        return if (params.isEmpty()) {
            method.invoke(null)
        } else {
            method.invoke(params[0])
        }
    }

    interface Method<R, T> {
        operator fun invoke(params: R?): T
    }
}