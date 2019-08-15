package com.axlecho.api

import com.axlecho.api.untils.MHStringUtils
import com.axlecho.api.untils.tranferTimePica
import com.axlecho.api.untils.transferTime
import org.junit.Assert
import org.junit.Test


class KitTest {
    @Test
    fun transferTimeTest() {
        val ret = transferTime("2019/5/12 2:06:21")
        Assert.assertEquals(1557597981000, ret)
    }

    @Test
    fun  transferPicaTimeTest() {

        val ret = tranferTimePica("2019-07-08T11:02:56.024Z")
        Assert.assertEquals(1562554976024, ret)
    }
    @Test
    fun testStringFilter() {
        val testString = "12_p7.html"
        val ret = MHStringUtils.match("_p\\d+",testString,0)
        println(ret)
    }



    @Test
    fun testStringMather() {
        var ret = MHStringUtils.match("a","asdfasdfasdf",0)
        print(ret)
    }
}
