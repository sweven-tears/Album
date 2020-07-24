package com.sweven.instance

/**
 * Created by Sweven on 2020/7/24--9:40.
 */
class InstanceDemo {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
        }

        //饿汉模式 直接将class 换成 object
        object _a

        // 懒汉模式
        private var _b: InstanceDemo? = null
            get() {
                if (field == null) {
                    field = InstanceDemo()
                }
                return field
            }

        fun getB(): InstanceDemo {
            return _b!!
        }

        // 线程安全的懒汉模式
        private var _c: InstanceDemo? = null
            get() {
                if (field == null) {
                    field = InstanceDemo()
                }
                return field
            }

        @Synchronized
        fun getC(): InstanceDemo {
            return _c!!
        }

        // 双重效验锁模式
        private val _d: InstanceDemo by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) { InstanceDemo() }
        fun getD(): InstanceDemo {
            return _d
        }

        // 静态内部类模式
        private val _e: InstanceDemo = AHold.hold
        fun getE(): InstanceDemo {
            return _e
        }

        // Double Check
        @Volatile
        private var f: InstanceDemo? = null
        fun getF() = f ?: synchronized(this) {
            f ?: InstanceDemo().also { f = it }
        }
    }

    private object AHold {
        var hold = InstanceDemo()
    }

    var a = "a"
}