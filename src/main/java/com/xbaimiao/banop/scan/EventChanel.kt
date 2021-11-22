package com.xbaimiao.banop.scan

import com.xbaimiao.banop.Core
import javassist.ClassPool
import java.io.File

/**
 * @Author xbaimiao
 * @Date 2021/11/21 14:38
 */
class EventChanel(
    val clazz: String,
    val start: String,
    val end: String,
    val func: (String, ClassPool, File) -> ByteArray?
) {

    companion object {

        @JvmStatic
        fun subscribeEvent(
            clazz: String = "",
            start: String = "",
            end: String = "",
            func: (String, ClassPool, File) -> ByteArray?
        ) {
            Core.list.add(EventChanel(clazz.replace(".", "/"), start, end, func))
        }

    }

}