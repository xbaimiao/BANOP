package com.xbaimiao.banop.module.path

import com.xbaimiao.banop.module.util.Fun
import com.xbaimiao.banop.scan.Awake
import com.xbaimiao.banop.scan.PackageUtil
import java.io.File
import java.util.jar.JarFile

/**
 * @Author xbaimiao
 * @Date 2021/11/22 20:19
 */
object PathMap {

    /**
     * 储存插件类对应的文件 方便导入
     */
    val pathMap = HashMap<File, List<String>>()

    @Awake
    fun init() {
        val file = File("plugins")
        val listFile = file.listFiles() ?: return
        for (plugin in listFile) {
            if (plugin.isDirectory || !plugin.name.endsWith(".jar")) {
                continue
            }
            pathMap[plugin] = PackageUtil.getClassList(JarFile(plugin))
        }
    }

    @JvmStatic
    operator fun get(clazzName: String): File {
        pathMap.forEach { (k, v) ->
            v.forEach {
                if (it.equals(clazzName, ignoreCase = true)) {
                    return k
                }
            }
        }
        return File(bukkit)
    }

    val bukkit get() = Fun.adaptPath("org.bukkit.Bukkit")

}