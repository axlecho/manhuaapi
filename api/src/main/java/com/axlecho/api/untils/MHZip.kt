package com.axlecho.api.untils

import com.axlecho.api.MHException
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.util.zip.ZipFile


class MHZip(val path: String) {
    private val src = File(path)

    fun text(file: String): String {
        if (!src.exists()) throw MHException("File not find -- ${src.absolutePath}")
        if (!src.isFile || !src.canRead()) throw  MHException("Could not read file -- ${src.absolutePath}")
        val zf = ZipFile(path)
        for (entry in zf.entries().toList()) {
            if (entry.name == file) {
                return BufferedReader(InputStreamReader(zf.getInputStream(entry))).readText()
            }
        }
        throw MHException("$file not found in ${src.name}")
    }
}