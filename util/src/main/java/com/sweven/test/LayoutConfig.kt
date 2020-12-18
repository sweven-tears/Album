package com.sweven.test

/**
 * Created by Sweven on 2020/8/10--16:25.
 */
class LayoutConfig(var name: String, var x: Int, var y: Int, var xSpec: Int, var ySpec: Int) {
    var blank:Boolean = false

    companion object {
        /**
         * 验证是否有组件错位排列
         */
        @JvmStatic
        fun verifyDislocate(list: MutableList<LayoutConfig>): Boolean {
            val full = Array(4) { BooleanArray(4) }
            for (i in 0..3) {
                for (j in 0..3) {
                    full[i][j] = false
                }
            }
            val size = list.size
            try {
                for (i in 0 until size) {
                    val config = list[i]
                    val x = config.x
                    val y = config.y
                    full[x][y] = true
                    var xStep = config.xSpec - 1
                    var yStep = config.ySpec - 1
                    while (xStep >= 0) {
                        while (yStep >= 0) {
                            full[x + xStep][y + yStep] = true
                            yStep--
                        }
                        yStep = config.ySpec - 1
                        xStep--
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                return false
            }
            for (i in 0..3) {
                for (j in 0..3) {
                    if (!full[i][j]) {
                        val layoutConfig = LayoutConfig("blank", i, j, 1, 1)
                        layoutConfig.blank = true
                        list.add(layoutConfig)
                    }
                }
            }
            return true
        }
    }
}