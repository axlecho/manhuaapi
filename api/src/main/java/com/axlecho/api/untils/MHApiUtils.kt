package com.axlecho.api.untils

import com.axlecho.api.MHComicInfo
import com.axlecho.api.MhNotFoundException
import io.reactivex.Observable


fun match(info: MHComicInfo, t: List<MHComicInfo>): Observable<MHComicInfo> {
    var target: MHComicInfo? = null

    if (t.isEmpty()) {
        throw MhNotFoundException()
    }

    for (c in t) {
        if (c.title == info.title) {
            target = c
        }
    }

    if (target == null) {
        throw MhNotFoundException()
    }

    return Observable.just(target)
}

