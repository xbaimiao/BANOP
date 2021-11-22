package com.xbaimiao.banop.change

import com.xbaimiao.banop.module.util.Fun.adaptClass
import com.xbaimiao.banop.module.util.Javassist.redo
import com.xbaimiao.banop.scan.Awake
import com.xbaimiao.banop.scan.EventChanel

/**
 * @Author xbaimiao
 * @Date 2021/11/22 19:50
 */
object Test {

    @Awake
    fun a() {
        EventChanel.subscribeEvent(
            clazz = "com.xbaimiao.plain.Main"
        ) { clazz, pool, file ->
            pool.insertClassPath(file.path)
            val ctClass = clazz.adaptClass()
            ctClass.redo(
                "onEnable", "        {" +
                        "System.out.println(\"我是你爹\");\n" +
                        "}"
            )
            ctClass.toBytecode()
        }
    }

}