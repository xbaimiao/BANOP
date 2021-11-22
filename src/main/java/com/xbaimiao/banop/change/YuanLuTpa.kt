package com.xbaimiao.banop.change

import com.xbaimiao.banop.module.util.Fun.adaptClass
import com.xbaimiao.banop.module.util.Javassist.redo
import com.xbaimiao.banop.scan.Awake
import com.xbaimiao.banop.scan.EventChanel

/**
 * @Author xbaimiao
 * @Date 2021/11/21 15:06
 * 删掉 跨服tpa的检测更新
 */
object YuanLuTpa {

    @Awake
    fun init() {
        println("修改 yuanluServerDo 的Main类")
        EventChanel.subscribeEvent(
            clazz = "yuan.plugins.serverDo.bukkit.Main"
        ) { className, pool, file ->
            pool.insertClassPath(file.path)
            val ctClass = className.adaptClass()
            ctClass.redo(
                "update",
                """
                    {
                        return;
                    }
                """.trimIndent()
            )
            ctClass.toBytecode()
        }
    }

}