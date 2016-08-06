package com.baidu.unbiz.easymapper.util;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 类工具
 *
 * @author zhangxu
 */
public class ClassUtil {

    private static final Set<Class<?>> PRIMITIVE_WRAPPER_TYPES = getWrapperTypes();

    private ClassUtil() {

    }

    private static Set<Class<?>> getWrapperTypes() {
        return new HashSet<Class<?>>(Arrays.<Class<?>>asList(Byte.class, Short.class, Integer.class,
                Long.class, Boolean.class, Character.class, Float.class, Double.class));
    }

    /**
     * Verifies whether a given type is one of the wrapper classes for a primitive type.
     *
     * @param type
     *
     * @return
     */
    public static boolean isPrimitiveWrapper(Class<?> type) {
        return PRIMITIVE_WRAPPER_TYPES.contains(type);
    }

    /**
     * Returns the corresponding wrapper type for the given primitive,
     * or null if the type is not primitive.
     *
     * @param primitiveType
     *
     * @return
     */
    public static Class<?> getWrapperType(Class<?> primitiveType) {
        if (boolean.class.equals(primitiveType)) {
            return Boolean.class;
        } else if (byte.class.equals(primitiveType)) {
            return Byte.class;
        } else if (char.class.equals(primitiveType)) {
            return Character.class;
        } else if (short.class.equals(primitiveType)) {
            return Short.class;
        } else if (int.class.equals(primitiveType)) {
            return Integer.class;
        } else if (long.class.equals(primitiveType)) {
            return Long.class;
        } else if (float.class.equals(primitiveType)) {
            return Float.class;
        } else if (double.class.equals(primitiveType)) {
            return Double.class;
        } else {
            return null;
        }
    }

    /**
     * Returns the corresponding primitive type for the given primitive wrapper,
     * or null if the type is not a primitive wrapper.
     *
     * @param wrapperType
     *
     * @return the corresponding primitive type
     */
    public static Class<?> getPrimitiveType(Class<?> wrapperType) {
        if (Boolean.class.equals(wrapperType)) {
            return Boolean.TYPE;
        } else if (Byte.class.equals(wrapperType)) {
            return Byte.TYPE;
        } else if (Character.class.equals(wrapperType)) {
            return Character.TYPE;
        } else if (Short.class.equals(wrapperType)) {
            return Short.TYPE;
        } else if (Integer.class.equals(wrapperType)) {
            return Integer.TYPE;
        } else if (Long.class.equals(wrapperType)) {
            return Long.TYPE;
        } else if (Float.class.equals(wrapperType)) {
            return Float.TYPE;
        } else if (Double.class.equals(wrapperType)) {
            return Double.TYPE;
        } else {
            return null;
        }
    }

    /**
     * 取得primitive类型的默认值。
     *
     * @param primitiveType 基本类型
     *
     * @return 默认值
     */
    public static Object getPrimitiveDefaultValue(Class<?> primitiveType) {
        if (boolean.class.equals(primitiveType)) {
            return false;
        } else if (byte.class.equals(primitiveType)) {
            return (byte) 0;
        } else if (char.class.equals(primitiveType)) {
            return '\0';
        } else if (short.class.equals(primitiveType)) {
            return (short) 0;
        } else if (int.class.equals(primitiveType)) {
            return 0;
        } else if (long.class.equals(primitiveType)) {
            return 0L;
        } else if (float.class.equals(primitiveType)) {
            return 0F;
        } else if (double.class.equals(primitiveType)) {
            return 0D;
        } else {
            return null;
        }
    }

}
