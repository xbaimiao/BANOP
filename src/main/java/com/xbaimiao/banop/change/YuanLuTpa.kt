package com.xbaimiao.banop.change

import com.xbaimiao.banop.module.util.Fun.adaptClass
import com.xbaimiao.banop.module.util.Javassist.insertBefore
import com.xbaimiao.banop.scan.Awake
import com.xbaimiao.banop.scan.EventChanel

/**
 * @Author xbaimiao
 * @Date 2021/11/21 15:06
 */
object YuanLuTpa {

    @Awake
    fun init() {
        println("注册修改 yuanluServerDo 的Main类")
        EventChanel.subscribeEvent(
            clazz = "yuan/plugins/serverDo/bukkit/Main"
        ) { className, pool, file ->
//            Fun.importPlugin("yuanluServerDo")
            pool.insertClassPath(file.path)
            val ctClass = className.adaptClass()
            ctClass.insertBefore(
                methodName = "update",
                code = """
                    if (true){
                        return;
                    }
                """.trimIndent()
            )
            ctClass.toBytecode()
        }
    }

}