package com.xbaimiao.banop.scan

import java.lang.reflect.Method

/**
 * @Author xbaimiao
 * @Date 2021/11/21 13:56
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class Awake(
    val type: AwakeType = AwakeType.START,
    val loadClass: String = ""
)

enum class AwakeType {
    ENABLE, LOAD_CLASS, START,
}

data class AwakeMethod(
    val awake: Awake,
    val obj: Any?,
    val method: Method
)