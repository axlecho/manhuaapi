package com.axlecho.api.module.rank

import com.axlecho.api.module.MHCategoryArea
import com.axlecho.api.module.MHModuleCategoryTime

data class MHModuleRankPageFrame(
        var timeCategorys: ArrayList<MHModuleCategoryTime> = ArrayList(),
        var areaCategorys: ArrayList<MHCategoryArea> = ArrayList())