package luoluna.sweven.album.entity

/**
 * Created by Sweven on 2020/7/25--17:49.
 * Email: sweventears@163.com
 */
class IMenu<R>(val menu: String, val method: Method<R>) {
    fun execute(vararg params: R) {
        return if (params.isEmpty()) {
            method.invoke(null)
        } else {
            method.invoke(params[0])
        }
    }

    interface Method<R> {
        operator fun invoke(params: R?)
    }
}
