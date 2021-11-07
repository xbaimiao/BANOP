package com.xbaimiao.bansetop;

import javassist.CtClass;
import javassist.CtMethod;
import javassist.CtNewMethod;

/**
 * @Author xbaimiao
 * @Date 2021/11/7 10:53
 */
public class ChangeClass {

    public static void insertMethodBefore(CtClass ctClass, String methodName, CtClass[] ctClasses, String code) {
        try {
            // 获取方法
            CtMethod Method = ctClass.getDeclaredMethod(methodName, ctClasses);
            // 先移除方法
            ctClass.removeMethod(Method);
            // 修改此方法名
            Method.setName(methodName + "_impl");
            // 将此方法添加到 CtClass
            ctClass.addMethod(Method);
            // 复制一个新方法，复制原来的方法
            CtMethod newMethod = CtNewMethod.copy(Method, methodName, ctClass, null);
            newMethod.insertBefore(code);
            ctClass.addMethod(newMethod);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
