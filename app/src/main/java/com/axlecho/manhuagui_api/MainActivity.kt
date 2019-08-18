package com.axlecho.manhuagui_api

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import com.axlecho.api.MHApi
import com.axlecho.api.MHContext
import com.axlecho.api.js.JSApi
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.InputStream


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        checkPermission()

        findViewById<TextView>(R.id.test).setOnClickListener {
            MHApi.context = object : MHContext {
                override fun loadAuthorization(): String {
                    return ""
                }

                override fun saveAuthorization(authorization: String) {
                }

                override fun loadTopTime(source: String): String {
                    return ""
                }

                override fun saveTopTime(time: String, source: String) {

                }

                override fun loadTopCategory(source: String): String {
                    return ""
                }

                override fun saveTopCategory(category: String, source: String) {
                }

                override fun getResourceAsStream(s: String): InputStream {
                    val resId = resources.getIdentifier(s, "raw", packageName)
                    return resources.openRawResource(resId)
                }

                override fun savePlugin(s: String, s1: String) {
                    val spf = getSharedPreferences("plugins", MODE_PRIVATE)
                    spf.edit().putString(s, s1).apply()
                }

                override fun loadPlugin(s: String): String {
                    return "{\"isEnable\":false,\"isJS\":true,\"name\":\"117\",\"path\":\"/storage/emulated/0/EhViewer/plugins/117.zip\"}"
                }

                override fun getPluginNames(): List<String> {
                    val spf = getSharedPreferences("plugins", MODE_PRIVATE)
                    return ArrayList(spf.all.keys)
                }

            }

            val api = JSApi.loadFromPlugin("117")
            val handle = api.info("237146")
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe {
                        it -> Log.v("TEST", it.info.title)
                    }
        }


    }

    private fun checkPermission() {
        //检查权限（NEED_PERMISSION）是否被授权 PackageManager.PERMISSION_GRANTED表示同意授权
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            //用户已经拒绝过一次，再次弹出权限申请对话框需要给用户一个解释
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission
                            .WRITE_EXTERNAL_STORAGE)) {
                Toast.makeText(this, "请开通相关权限，否则无法正常使用本应用！", Toast.LENGTH_SHORT).show()
            }
            //申请权限
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)

        } else {
            Toast.makeText(this, "授权成功！", Toast.LENGTH_SHORT).show()
            Log.e("", "checkPermission: 已经授权！")
        }
    }

}
