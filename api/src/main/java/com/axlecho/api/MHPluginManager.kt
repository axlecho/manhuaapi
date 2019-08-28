package com.axlecho.api

import com.axlecho.api.js.JSPluginInfo
import com.axlecho.api.untils.MHZip
import com.google.gson.Gson

data class MHPlugin(val name: String, var isEnable: Boolean, var isJS: Boolean, var path: String)

class MHPluginManager private constructor() {


    companion object {
        val INSTANCE: MHPluginManager by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            MHPluginManager()
        }
    }

    fun init() {
        for (source in MHApiSource.getSourceList()) {
            savePlugin(MHPlugin(source, true, false, ""))
        }
    }

    fun names(): List<String> {
        return MHApi.context.getPluginNames()
    }

    fun plugins(): List<MHPlugin> {
        val ret = mutableListOf<MHPlugin>()
        for (name in names()) {
            ret.add(loadPlugin(name))
        }
        return ret
    }

    fun livePlugin(): List<MHPlugin> {
        return plugins().filter { it.isEnable }
    }

    fun parserPlugin(path: String): MHPlugin {
        val zip = MHZip(path)
        val info = Gson().fromJson<JSPluginInfo>(zip.text("package.json"), JSPluginInfo::class.java)
        return MHPlugin(info.name, false, true, path)
    }


    fun savePlugin(plugin: MHPlugin) {
        MHApi.context.savePlugin(plugin.name, Gson().toJson(plugin, MHPlugin::class.java))
    }

    fun loadPlugin(name: String): MHPlugin {
        return Gson().fromJson(MHApi.context.loadPlugin(name), MHPlugin::class.java)
    }
}