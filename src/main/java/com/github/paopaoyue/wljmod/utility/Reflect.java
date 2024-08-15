package com.github.paopaoyue.wljmod.utility;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class Reflect {

    private static final Logger logger = LogManager.getLogger(Reflect.class.getName());

    public static <O, T> T getPrivate(Class<O> cls, Object obj, String varName, Class<T> type) {
        try {
            Field f = cls.getDeclaredField(varName);
            f.setAccessible(true);
            return type.cast(f.get(obj));
        } catch (IllegalAccessException | NoSuchFieldException e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    public static <O, T> T getStaticPrivate(Class<O> cls, String varName, Class<T> type) {
        return getPrivate(cls, null, varName, type);
    }

    public static <O, T> void setPrivate(Class<O> cls, Object obj, String varName, T value) {
        try {
            Field f = cls.getDeclaredField(varName);
            f.setAccessible(true);
            f.set(obj, value);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            logger.error(e.getMessage(), e);
        }
    }

    public static <O, T> void setStaticPrivate(Class<O> cls, String varName, T value) {
        setPrivate(cls, null, varName, value);
    }

    public static Object invokePrivateMethod(Object obj, String methodName, Object... args) {
        try {
            Class<?> clazz = obj.getClass();
            Class<?>[] parameterTypes = new Class<?>[args.length];
            for (int i = 0; i < args.length; i++) {
                parameterTypes[i] = args[i].getClass();
            }
            Method method = clazz.getDeclaredMethod(methodName, parameterTypes);
            method.setAccessible(true);
            return method.invoke(obj, args);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }
}
