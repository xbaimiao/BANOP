package com.xbaimiao.banop.module.util

import javassist.CtClass
import javassist.CtMethod
import javassist.CtNewMethod

/**
 * @Author xbaimiao
 * @Date 2021/11/22 19:38
 */
object Javassist {

    @JvmStatic
    fun CtClass.insertBefore(methodName: String, code: String, classes: Array<CtClass> = arrayOf()) {
        // 获取方法
        val method = this.getDeclaredMethod(methodName, classes)
        // 先移除方法
        this.removeMethod(method)
        // 修改此方法名
        method.name = methodName + "_impl"
        // 将此方法添加到 CtClass
        this.addMethod(method)
        // 复制一个新方法，复制原来的方法
        val newMethod = CtNewMethod.copy(method, methodName, this, null)
        newMethod.insertBefore(code)
        this.addMethod(newMethod)
    }

    @JvmStatic
    fun CtClass.redo(methodName: String, code: String, clClasses: Array<CtClass> = arrayOf()) {
        val method = this.getDeclaredMethod(methodName, clClasses)
        this.removeMethod(method)
        method.name = methodName + "_impl"
        this.addMethod(method)
        val newMethod = CtMethod(method.returnType, methodName, clClasses, this)
        newMethod.setBody(code)
        this.addMethod(newMethod)
    }

}