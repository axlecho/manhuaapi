package com.axlecho.api.js

import org.mozilla.javascript.Context
import org.mozilla.javascript.ScriptableObject
import java.io.InputStreamReader

class JSEngine {

    private lateinit var cx: Context
    private lateinit var globalscope: ScriptableObject

    private val jquery = InputStreamReader(this.javaClass.classLoader.getResourceAsStream("raw/jquery-1.8.0.js"))
    private val envrhino = InputStreamReader(this.javaClass.classLoader.getResourceAsStream("raw/envrhino-1.2.js"))
    var page = ""

    private lateinit var currentScope: ScriptableObject

    fun init() {
        cx = org.mozilla.javascript.Context.enter()
        globalscope = cx.initStandardObjects()

        cx.optimizationLevel = -1
        cx.evaluateReader(globalscope, envrhino, "envrhino-1.2.js", 1, null)
        cx.evaluateReader(globalscope, jquery, "jquery-1.8.0.js", 1, null)
    }

    fun execute(script: String): String {
        currentScope = cx.initStandardObjects(globalscope, false)
        val result = cx.evaluateString(currentScope, script, "script", 1, null)
        return org.mozilla.javascript.Context.toString(result)
    }

    fun loadPage(_page: String) {
        page = _page.replace("'","\\'")
                .replace("\n","")
                .replace("\r","")

        val script = " var raw = \'$page\'; \n var doc = \$( '<div></div>' ); \n doc.html(raw);"
        println(script)
        execute(script)
    }

    fun loadLibrary(library: String) {
        execute(library)
    }

    fun callFunction(func: String): String {
        val fct = currentScope.get(func, currentScope) as org.mozilla.javascript.Function
        val result = fct.call(cx, currentScope, cx.newObject(currentScope), arrayOf<Any>())
        return org.mozilla.javascript.Context.toString(result)
    }

    fun destroy() {
        org.mozilla.javascript.Context.exit()
    }
}