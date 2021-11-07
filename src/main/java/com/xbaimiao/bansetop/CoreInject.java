package com.xbaimiao.bansetop;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.lang.instrument.Instrumentation;
import java.net.URLDecoder;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CoreInject {

    public static final String craftPlayerClass = "CraftPlayer";
    public static final String obc = "org/bukkit/craftbukkit";
    public static YamlConfiguration configuration;

    public static void premain(String agentArgs, Instrumentation inst) {
        IO.saveResource("BANOP.yml", "BANOP.yml");
        //注意 在这里使用Bukkit.getServer() 会返回null 因为服务器还没有启动.
        inst.addTransformer((loader, className, classBeingRedefined, protectionDomain, classfileBuffer) -> {
            if (className.startsWith(obc) && className.endsWith(craftPlayerClass)) {
                ClassPool pool = ClassPool.getDefault();
                //获取Javassist的储存类的池
                try {
                    Path path = Paths.get(
                            Class.forName("org.bukkit.Bukkit").getProtectionDomain().getCodeSource().getLocation().toString().substring(6)
                    );
                    //这样获取核心所在路径，这么做为了下面把核心导入ClassPool，不导入会找不到CraftPlayer类.
                    String pathUtf = URLDecoder.decode(path.toFile().getPath(), "utf-8");
                    if (System.getProperty("os.name").toLowerCase().contains("linux")) {
                        pathUtf = "/" + pathUtf;
                    }
                    System.out.println(pathUtf);
                    //路径转码 避免路径有中文而乱码报错
                    pool.insertClassPath(pathUtf);
                    configuration = YamlConfiguration.loadConfiguration(new File("BANOP.yml"));
                    pool.importPackage("org.bukkit.Bukkit");
                    pool.importPackage("com.xbaimiao.bansetop.CoreInject");
                    CtClass ctClass = pool.getCtClass(className.replace("/", "."));
                    ChangeClass.insertMethodBefore(
                            ctClass,
                            "setOp",
                            new CtClass[]{pool.getCtClass("boolean")},
                            "if (!CoreInject.setOp(value)){\n" +
                                    "            return;\n" +
                                    "        }"
                    );
                    return ctClass.toBytecode();
                } catch (ClassNotFoundException | NotFoundException | CannotCompileException | IOException exc) {
                    exc.printStackTrace();
                }
            }
            return null;
        });
    }

    public static boolean setOp(boolean value) {
        int index = 0;
        StackTraceElement[] elements = Thread.currentThread().getStackTrace();
        while (index < elements.length && elements[index].toString().contains("getStackTrace")) {
            index++;
        }
        while (index < elements.length && elements[index].toString().contains("setOp")) {
            index++;
        }
        String stack = elements[index].toString();
        boolean isSet = false;
        for (String white : configuration.getStringList("whitelsit")) {
            if (stack.contains(white)) {
                isSet = true;
                break;
            }
        }
        if (isSet){
            return true;
        }
        String stringBuilder = "[BANOP]§f已阻止插件调用setOp方法(" +
                "value: " + value +
                ") §a->§f " +
                stack;
        Bukkit.getLogger().info(stringBuilder);
        return false;
    }

    /**
     * 假如这个就是setOp方法
     * 给他前面加点代码
     */
    public void test(boolean value) {
        if (!CoreInject.setOp(value)) {
            return;
        }
    }

}
