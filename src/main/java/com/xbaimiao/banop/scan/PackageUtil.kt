package com.xbaimiao.banop.scan

import java.io.File
import java.util.jar.JarFile

/**
 * @Author xbaimiao
 * @Date 2021/11/22 20:09
 */
object PackageUtil {

    @JvmStatic
    fun getSelfClass(prefix: String = ""): List<String> {
        val path = this.javaClass.protectionDomain.codeSource.location.file
        val jarFile = JarFile(File(path))
        return getClassList(jarFile, prefix)
    }

    @JvmStatic
    fun getClassList(file: JarFile, prefix: String = ""): List<String> {
        val array = arrayListOf<String>()
        for (entry in file.entries()) {
            if (entry.name.endsWith(".class")) {
                val name = entry.name.replace("/", ".").substring(0, entry.name.length - 6)
                if (prefix == "" || name.startsWith(prefix)) {
                    array.add(name)
                }
            }
        }
        return array
    }


}