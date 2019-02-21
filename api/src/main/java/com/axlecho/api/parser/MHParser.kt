package com.axlecho.api.parser

import com.axlecho.api.module.rank.MHModuleRankPage

class MHParser {
    companion object {
        private val Tag: String = MHParser::javaClass.name
        fun parseRank(html: String): MHModuleRankPage {
            return MHParserRank.parse(html)
        }
    }
}
