package com.axlecho.api.untils

import com.axlecho.api.MHComicInfo
import com.axlecho.api.MHNotFoundException
import io.reactivex.Observable


fun match(info: MHComicInfo, t: List<MHComicInfo>): Observable<MHComicInfo> {
    var target: MHComicInfo? = null

    if (t.isEmpty()) {
        throw MHNotFoundException()
    }

    for (c in t) {
        if (c.title == info.title) {
            target = c
        }
    }

    if (target == null) {
        throw MHNotFoundException()
    }

    return Observable.just(target)
}

