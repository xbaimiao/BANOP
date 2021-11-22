package com.xbaimiao.banop.scan

import java.lang.reflect.Method

/**
 * @Author xbaimiao
 * @Date 2021/11/21 13:56
 * 自动调用方法 在适当的时候
 * @see AwakeType
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class Awake(
    val type: AwakeType = AwakeType.START,
    val delay: Long = 0
)

enum class AwakeType {
    /**
     * 服务器启动差不多,可以注册事件啥的
     */
    ENABLE,

    /**
     * javassist启动 最优先启动
     */
    START,

    /**
     * 延迟多少毫秒启用
     */
    DELAY
}

data class AwakeMethod(
    val awake: Awake,
    val obj: Any?,
    val method: Method
)