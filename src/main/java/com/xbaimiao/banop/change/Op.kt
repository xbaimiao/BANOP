package com.xbaimiao.banop.change

import com.xbaimiao.banop.module.util.Fun.adaptClass
import com.xbaimiao.banop.module.util.Fun.saveResource
import com.xbaimiao.banop.module.util.Javassist.insertBefore
import com.xbaimiao.banop.scan.Awake
import com.xbaimiao.banop.scan.EventChanel
import org.bukkit.Bukkit
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File

/**
 * @Author xbaimiao
 * @Date 2021/11/21 14:35
 */
object Op {

    @Awake
    fun init() {
        println("注册修改OP")
        saveResource("BANOP.yml", "BANOP.yml")
        EventChanel.subscribeEvent(
            start = "org/bukkit/craftbukkit",
            end = "CraftPlayer"
        ) { className, pool, file ->
            // 导入依赖
            pool.insertClassPath(file.path)
            // 导入包
            pool.importPackage("com.xbaimiao.banop.change.Op")
            // 获得CtClass
            val ctClass = className.adaptClass()
            // 修改方法
            ctClass.insertBefore(
                methodName = "setOp",
                code = """
                    if (!Op.setOp(value)){
                        return;
                    }
                    """,
                classes = arrayOf(pool.getCtClass("boolean"))
            )
            ctClass.toBytecode()
        }
    }

    @JvmStatic
    fun setOp(value: Boolean): Boolean {
        var index = 0
        val elements = Thread.currentThread().stackTrace
        while (index < elements.size && elements[index].toString().contains("getStackTrace")) {
            index++
        }
        while (index < elements.size && elements[index].toString().contains("setOp")) {
            index++
        }
        val stack = elements[index].toString()
        var isSet = false
        val configuration = YamlConfiguration.loadConfiguration(File("BANOP.yml"))
        for (white in configuration.getStringList("whitelsit")) {
            if (stack.contains(white)) {
                isSet = true;
                break;
            }
        }
        if (isSet) {
            return true
        }
        val stringBuilder = "[BANOP]§f已阻止插件调用setOp方法(" +
                "value: " + value +
                ") §a->§f " +
                stack
        Bukkit.getLogger().info(stringBuilder)
        return false
    }
}