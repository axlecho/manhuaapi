package com.axlecho.api

class MHConstant {
    companion object {
        val HTTP_PROTOCOL_PREFIX = "http://"
        val HTTPS_PROTOCOL_PREFIX = "https://"
        val BASE_HOST = "www.hhmmoo.com"
        val HOST = HTTP_PROTOCOL_PREFIX + BASE_HOST

        val BGM_BASE_API = "api.bgm.tv"
        val BGM_API = HTTP_PROTOCOL_PREFIX + BGM_BASE_API
        val BGM_BASE_HOST = "bangumi.tv"
        val BGM_HOST = HTTP_PROTOCOL_PREFIX +  BGM_BASE_HOST

        val USER_AGENT ="Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.81 Safari/537.36"

        val UNKNOWN_TIME = ""
        val UNKNOWN_MAN = ""
        val UNKNOWN_TITILE = ""
    }
}