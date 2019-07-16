package com.axlecho.api

open class MHException : Exception {
    constructor(detailMessage: String) : super(detailMessage)

    constructor(detailMessage: String, cause: Throwable) : super(detailMessage, cause)
}

class MHNotSupportException : MHException("not support")

class MHNotFoundException : MHException("not found")