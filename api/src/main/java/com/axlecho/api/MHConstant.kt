package com.axlecho.api

class MHConstant {
    companion object {

        const val HTTP_PROTOCOL_PREFIX = "http://"
        const val HTTPS_PROTOCOL_PREFIX = "https://"

        const val BASE_HOST = "www.hhmmoo.com"
        const val HOST = HTTP_PROTOCOL_PREFIX + BASE_HOST

        const val BGM_BASE_API = "api.bgm.tv"
        const val BGM_API = HTTP_PROTOCOL_PREFIX + BGM_BASE_API
        const val BGM_BASE_HOST = "bangumi.tv"
        const val BGM_HOST = HTTP_PROTOCOL_PREFIX + BGM_BASE_HOST

        const val MANHUAGUI_BASE_HOST = "www.manhuagui.com"
        const val MANHUAGUI_HOST = HTTPS_PROTOCOL_PREFIX + MANHUAGUI_BASE_HOST

        const val USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.81 Safari/537.36"

        const val UNKNOWN_TIME = ""
        const val UNKNOWN_MAN = ""
        const val UNKNOWN_TITLE = ""
    }
}