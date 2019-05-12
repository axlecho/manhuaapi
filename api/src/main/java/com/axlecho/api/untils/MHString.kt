package com.axlecho.api.untils

import java.text.SimpleDateFormat
import java.util.*

fun transferTime(timeStr: String): Long {
    val sdf = SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.CHINA)
    return sdf.parse(timeStr).time
}