package com.axlecho.api.js

import com.axlecho.api.MHApi
import com.axlecho.api.untils.MHNode
import org.mozilla.javascript.Context
import org.mozilla.javascript.ScriptableObject
import java.io.InputStreamReader

class JSEngine {
    companion object {
        val INSTANCE: JSEngine by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            JSEngine()
        }
    }

    private var cx: Context = org.mozilla.javascript.Context.enter()
    private var globalscope: ScriptableObject = cx.initStandardObjects()
    private val tools = InputStreamReader(MHApi.context.getResourceAsStream("tools"))

    init {
        cx.optimizationLevel = -1
        val jsoup = Context.javaToJS(com.axlecho.api.untils.MHNode(), globalscope)
        val log = Context.javaToJS(System.out,globalscope)
        val javaNet = Context.javaToJS(com.axlecho.api.js.JSNetworkTool(),globalscope)

        ScriptableObject.putProperty(globalscope, "jsoup", jsoup)
        ScriptableObject.putProperty(globalscope, "log", log)
        ScriptableObject.putProperty(globalscope,"javaNet",javaNet)

        cx.evaluateReader(globalscope, tools, "tools.js", 1, null)
        org.mozilla.javascript.Context.exit()
    }

    fun destroy() {
        org.mozilla.javascript.Context.exit()
    }

    fun fork(): JSScope {
        return JSScope(globalscope)
    }

}


class JSScope(private val globalscope: ScriptableObject) {
    private var page = ""
    private var currentScope: ScriptableObject

    init {
        val cx: Context = org.mozilla.javascript.Context.enter()
        cx.optimizationLevel = -1
        currentScope = cx.initStandardObjects(globalscope, false)
        org.mozilla.javascript.Context.exit()
    }

    fun execute(script: String): String {
        val cx: Context = org.mozilla.javascript.Context.enter()
        cx.optimizationLevel = -1
        val result = cx.evaluateString(currentScope, script, "script", 1, null)
        return org.mozilla.javascript.Context.toString(result)
    }

    fun loadPage(_page: String) {
        page = _page
                .replace("\\", "\\\\")
                .replace("'", "\\'")
                .replace("\n", "")
                .replace("\r", "")

        val script = "var raw = \'$page\';\n jsoup.loadFromString(raw);"
        println(script)
        execute(script)
    }

    fun loadLibrary(library: String) {
        println(library)
        execute(library)
    }


    fun callFunction(func: String, vararg args: String): String {
        val cx: Context = org.mozilla.javascript.Context.enter()
        cx.optimizationLevel = -1
        val fct = currentScope.get(func, currentScope) as org.mozilla.javascript.Function
        val result = fct.call(cx, currentScope, cx.newObject(currentScope), args)
        return org.mozilla.javascript.Context.toString(result)
    }
}