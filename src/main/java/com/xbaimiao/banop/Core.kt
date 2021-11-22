package com.xbaimiao.banop

import com.xbaimiao.banop.module.path.PathMap
import com.xbaimiao.banop.scan.*
import com.xbaimiao.banop.scan.PackageUtil.getSelfClass
import javassist.ClassPool
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.bukkit.Bukkit
import org.bukkit.event.Listener
import java.lang.instrument.Instrumentation

object Core {

    val list: ArrayList<EventChanel> = ArrayList()
    lateinit var pool: ClassPool
    private val awakes: MutableList<AwakeMethod> = ArrayList()

    @JvmStatic
    fun premain(agentArgs: String?, inst: Instrumentation) {
        for (aClass in getSelfClass("com.xbaimiao.banop")) {
            try {
                val c = Class.forName(aClass)
                val kClass = c.kotlin
                val instance: Any? = kClass.objectInstance
                for (method in c.declaredMethods) {
                    val awake = method.getAnnotation(Awake::class.java)
                    if (awake != null) {
                        var obj: Any? = null
                        if (instance != null) {
                            obj = instance
                        }
                        awakes.add(AwakeMethod(awake, obj, method))
                    }
                }
            } catch (e: ClassNotFoundException) {
                continue
            } catch (e: NoClassDefFoundError) {
                continue
            }
        }
        GlobalScope.launch {
            for (awake in awakes) {
                if (awake.awake.type == AwakeType.START) {
                    awake.method.invoke(awake.obj)
                }
                if (awake.awake.type == AwakeType.DELAY) {
                    GlobalScope.launch {
                        delay(awake.awake.delay)
                        awake.method.invoke(awake.obj)
                    }
                }
            }
        }
        pool = ClassPool.getDefault()
        inst.addTransformer { _, className, _, _, _ ->
            for (event in list) {
                if (event.clazz == "") {
                    if (className.startsWith(event.start) && className.endsWith(event.end)) {
                        return@addTransformer event.func.invoke(
                            className,
                            pool,
                            PathMap[className.replace("/", ".")]
                        )
                    }
                } else {
                    if (className.equals(event.clazz, ignoreCase = true)) {
                        return@addTransformer event.func.invoke(
                            className,
                            pool,
                            PathMap[className.replace("/", ".")]
                        )
                    }
                }
            }
            null
        }
        GlobalScope.launch {
            while (checkServer()) {
                delay(50)
            }
            while (checkServer2()) {
                delay(50)
            }
            for (awake in awakes) {
                if (awake.awake.type == AwakeType.ENABLE) {
                    awake.method.invoke(awake.obj)
                }
            }
            registerEvent()
        }
    }

    /**
     * 注册bukkit监听器
     */
    private fun registerEvent() {
        for (aClass in getSelfClass()) {
            val c = Class.forName(aClass)
            if (c.getAnnotation(BukkitEvent::class.java) != null) {
                val kClass = c.kotlin
                if (kClass.objectInstance != null) {
                    if (kClass.objectInstance is Listener) {
                        Bukkit.getPluginManager().registerEvents(
                            kClass.objectInstance as Listener,
                            Bukkit.getPluginManager().plugins.random()
                        )
                    }
                    continue
                }
                val obj = c.newInstance()
                if (obj is Listener) {
                    Bukkit.getPluginManager().registerEvents(obj, Bukkit.getPluginManager().plugins.random())
                }
            }
        }
    }

    private fun checkServer(): Boolean {
        return try {
            Class.forName("org.bukkit.event.Listener")
            false
        } catch (e: Exception) {
            true
        }
    }

    private fun checkServer2(): Boolean {
        try {
            if (Bukkit.getServer() != null) {
                if (Bukkit.getPluginManager() != null) {
                    if (Bukkit.getPluginManager().plugins.isNotEmpty()) {
                        Bukkit.getPluginManager().plugins.forEach {
                            if (!it.isEnabled) {
                                return true
                            }
                        }
                        return false
                    }
                }
            }
            return true
        } catch (e: Exception) {
            return true
        }
    }

}