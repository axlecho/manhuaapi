package com.axlecho.api.js

import com.axlecho.api.MHApi
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

    private val jquery = InputStreamReader(MHApi.context.getResourceAsStream("jquery"))
    private val envrhino = InputStreamReader(MHApi.context.getResourceAsStream("envrhino"))

    init {
        cx.optimizationLevel = -1
        cx.evaluateReader(globalscope, envrhino, "envrhino.js", 1, null)
        cx.evaluateReader(globalscope, jquery, "jquery.js", 1, null)
    }

    fun destroy() {
        org.mozilla.javascript.Context.exit()
    }

    fun fork(): JSScope {
        return JSScope(cx, globalscope)
    }

}


class JSScope(private val cx: Context, private val globalscope: ScriptableObject) {
    private var page = ""
    private lateinit var currentScope: ScriptableObject

    fun execute(script: String): String {
        currentScope = cx.initStandardObjects(globalscope, false)
        val result = cx.evaluateString(currentScope, script, "script", 1, null)
        return org.mozilla.javascript.Context.toString(result)
    }

    fun loadPage(_page: String) {
        page = _page
                .replace(Regex("<script src=.*?</script>"), "")
                .replace("\\", "\\\\")
                .replace("'", "\\'")
                .replace("\n", "")
                .replace("\r", "")

        val script = " var raw = \'$page\'; \n var doc = \$( '<div></div>' ); \n doc.html(raw);"
        println(script)
        execute(script)
    }

    fun loadLibrary(library: String) {
        execute(library)
    }


    fun callFunction(func: String, vararg args: String): String {
        val fct = currentScope.get(func, currentScope) as org.mozilla.javascript.Function
        val result = fct.call(cx, currentScope, cx.newObject(currentScope), args)
        return org.mozilla.javascript.Context.toString(result)
    }
}