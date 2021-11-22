package com.xbaimiao.banop;

import com.xbaimiao.banop.module.path.PathMap;
import com.xbaimiao.banop.scan.*;
import javassist.ClassPool;
import kotlinx.coroutines.BuildersKt;
import kotlinx.coroutines.CoroutineStart;
import kotlinx.coroutines.GlobalScope;

import java.lang.instrument.Instrumentation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Core {

    public static List<Listener> list = new ArrayList<>();
    public static ClassPool pool;

    public static List<AwakeMethod> awakes = new ArrayList<>();

//    public static void main(String[] args) {
//        try {
//            Class<?> c = YuanLuTpa.class;
//            Object INSTANCE = c.getDeclaredField("INSTANCE").get(null);
//            for (Method method : c.getDeclaredMethods()) {
//                Awake awake = method.getAnnotation(Awake.class);
//                if (awake != null) {
//                    if (Modifier.isStatic(method.getModifiers())) {
//                        method.invoke(null);
//                        continue;
//                    }
//                    if (INSTANCE != null) {
//                        method.invoke(INSTANCE);
//                        continue;
//                    }
//                    method.invoke(c.newInstance());
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    public static void premain(String agentArgs, Instrumentation inst) {
        for (String aClass : PackageUtil.getSelfClass("com.xbaimiao.banop")) {
            try {
                Class<?> c = Class.forName(aClass);
                Object INSTANCE;
                try {
                    INSTANCE = c.getDeclaredField("INSTANCE").get(null);
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    INSTANCE = null;
                }
                for (Method method : c.getDeclaredMethods()) {
                    Awake awake = method.getAnnotation(Awake.class);
                    if (awake != null) {
                        Object obj = null;
                        if (INSTANCE != null) {
                            obj = INSTANCE;
                        }
                        awakes.add(new AwakeMethod(awake, obj, method));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            for (AwakeMethod awake : awakes) {
                if (awake.getAwake().type().equals(AwakeType.START)) {
                    awake.getMethod().invoke(awake.getObj());
                    awakes.remove(awake);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        pool = ClassPool.getDefault();
        inst.addTransformer((loader, className, classBeingRedefined, protectionDomain, classfileBuffer) -> {
            try {
                for (Listener event : list) {
                    if (Objects.equals(event.getClazz(), "")) {
                        if (className.startsWith(event.getStart()) && className.endsWith(event.getEnd())) {
                            return event.getFunc().invoke(className, pool, PathMap.get(className.replace("/", ".")));
                        }
                    } else {
                        if (className.equalsIgnoreCase(event.getClazz())) {
                            return event.getFunc().invoke(className, pool, PathMap.get(className.replace("/", ".")));
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        });
    }


}