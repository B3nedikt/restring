package com.b3nedikt.restring

import android.util.Log

import java.lang.reflect.Field
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method

/**
 * Created by chris on 17/12/14.
 * Copied from Calligraphy:
 * https://github.com/chrisjenx/Calligraphy/blob/master/calligraphy/src/main/java/uk/co/chrisjenx/calligraphy/ReflectionUtils.java
 */
internal object ReflectionUtils {

    private val TAG = ReflectionUtils::class.java.simpleName

    fun getField(clazz: Class<*>, fieldName: String): Field? {
        try {
            val f = clazz.getDeclaredField(fieldName)
            f.isAccessible = true
            return f
        } catch (ignored: NoSuchFieldException) {
        }

        return null
    }

    operator fun getValue(field: Field, obj: Any): Any? {
        try {
            return field.get(obj)
        } catch (ignored: IllegalAccessException) {
        }

        return null
    }

    operator fun setValue(field: Field, obj: Any, value: Any) {
        try {
            field.set(obj, value)
        } catch (ignored: IllegalAccessException) {
        }

    }

    fun getMethod(clazz: Class<*>, methodName: String): Method? {
        val methods = clazz.methods
        for (method in methods) {
            if (method.name == methodName) {
                method.isAccessible = true
                return method
            }
        }
        return null
    }

    fun invokeMethod(`object`: Any, method: Method?, vararg args: Any) {
        try {
            if (method == null) return
            method.invoke(`object`, *args)
        } catch (e: IllegalAccessException) {
            Log.d(TAG, "Can't invoke method using reflection", e)
        } catch (e: InvocationTargetException) {
            Log.d(TAG, "Can't invoke method using reflection", e)
        }

    }
}