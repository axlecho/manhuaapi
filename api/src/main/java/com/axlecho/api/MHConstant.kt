package com.axlecho.api

class MHConstant {
    companion object {

        const val HTTP_PROTOCOL_PREFIX = "http://"
        const val HTTPS_PROTOCOL_PREFIX = "https://"

        const val HANHAN_BASE_HOST = "www.hhimm.com"
        const val HANHAN_HOST = HTTP_PROTOCOL_PREFIX + HANHAN_BASE_HOST

        const val BGM_BASE_API = "api.bgm.tv"
        const val BGM_API = HTTP_PROTOCOL_PREFIX + BGM_BASE_API
        const val BGM_BASE_HOST = "bangumi.tv"
        const val BGM_HOST = HTTP_PROTOCOL_PREFIX + BGM_BASE_HOST

        const val MANHUAGUI_BASE_HOST = "www.manhuagui.com"
        const val MANHUAGUI_HOST = HTTPS_PROTOCOL_PREFIX + MANHUAGUI_BASE_HOST


        const val KUKU_BASE_HOST = "comic.ikkdm.com"
        const val KUKU_HOST = HTTP_PROTOCOL_PREFIX + KUKU_BASE_HOST

        const val PICA_BASE_HOST = "picaapi.picacomic.com"
        const val PICA_HOST = HTTPS_PROTOCOL_PREFIX + PICA_BASE_HOST
        const val PICA_BASE_MEDIA = "s3.picacomic.com/static/"
        const val PICA_MEDIA = HTTPS_PROTOCOL_PREFIX + PICA_BASE_MEDIA

        const val USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.81 Safari/537.36"

        const val UNKNOWN_TIME = ""
        const val UNKNOWN_MAN = ""
        const val UNKNOWN_TITLE = ""
    }
}