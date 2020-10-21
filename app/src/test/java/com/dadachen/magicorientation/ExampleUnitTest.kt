package com.dadachen.magicorientation

import com.dadachen.magicorientation.utils.Utils
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun `时间打印测试`(){
        val ms = System.currentTimeMillis()
        val res = Utils.getTime(ms)
        println(res)
    }


}
