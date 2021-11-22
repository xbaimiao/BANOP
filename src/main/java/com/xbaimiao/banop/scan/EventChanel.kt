package com.xbaimiao.banop.scan

import com.xbaimiao.banop.Core
import javassist.ClassPool
import java.io.File

/**
 * @Author xbaimiao
 * @Date 2021/11/21 14:38
 */
object EventChanel {

    @JvmStatic
    fun subscribeEvent(
        clazz: String = "",
        start: String = "",
        end: String = "",
        func: (String, ClassPool, File) -> ByteArray?
    ) {
        Core.list.add(Listener(clazz.replace(".", "/"), start, end, func))
    }

}

class Listener(
    val clazz: String,
    val start: String,
    val end: String,
    val func: (String, ClassPool, File) -> ByteArray?
)