package com.axlecho.api

open class MhException : Exception {
    constructor(detailMessage: String) : super(detailMessage)

    constructor(detailMessage: String, cause: Throwable) : super(detailMessage, cause)
}

class MhNotSupportException : MhException("not support")

class MhNotFoundException : MhException("not found")