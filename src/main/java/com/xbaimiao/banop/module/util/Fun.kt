package com.xbaimiao.banop.module.util

import com.xbaimiao.banop.Core
import javassist.CtClass
import java.io.*
import java.net.URLDecoder
import java.nio.file.Paths

/**
 * @Author xbaimiao
 * @Date 2021/11/21 15:11
 */
object Fun {

    @JvmStatic
    fun importPlugin(plugin: String) {
        val file = File("plugins")
        val listFile = file.listFiles() ?: return
        for (file1 in listFile) {
            if (file1.isDirectory) {
                continue
            }
            val path = file1.path.lowercase()
            if (path.contains(plugin.lowercase())) {
                Core.pool.insertClassPath(file1.path)
                break
            }
        }
    }

    @JvmStatic
    fun String.adaptClass(): CtClass {
        return Core.pool.getCtClass(this.replace("/", "."))
    }

    @JvmStatic
    fun adaptPath(clazz: String): String {
        val path = Paths.get(
            Class.forName(clazz).protectionDomain.codeSource.location.toString().substring(6)
        )
        //这样获取核心所在路径，这么做为了下面把核心导入ClassPool，不导入会找不到CraftPlayer类.
        var pathUtf = URLDecoder.decode(path.toFile().path, "utf-8")
        if (System.getProperty("os.name").toLowerCase().contains("linux")) {
            pathUtf = "/$pathUtf"
        }
        return pathUtf
    }

    @JvmStatic
    fun saveResource(name: String, path: String) {
        val file = File(path)
        if (file.parentFile != null && !file.parentFile.exists()) {
            file.parentFile.mkdirs()
        }
        if (!file.exists()) {
            try {
                file.createNewFile()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            toFile(file, this::class.java.classLoader.getResourceAsStream(name)!!)
        }
    }

    @JvmStatic
    fun toFile(file: File, inputStream: InputStream) {
        try {
            val out: OutputStream = FileOutputStream(file)
            val buf = ByteArray(1024)
            var len: Int
            while (inputStream.read(buf).also { len = it } > 0) {
                out.write(buf, 0, len)
            }
            out.close()
            inputStream.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}