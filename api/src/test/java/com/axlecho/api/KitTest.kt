package com.axlecho.api

import com.axlecho.api.untils.transferTime
import org.junit.Assert
import org.junit.Test

class KitTest {
    @Test
    fun transferTimeTest() {
        val ret = transferTime("2019/5/12 2:06:21")
        Assert.assertEquals(1557597981000, ret)
    }
}
