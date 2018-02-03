package com.baidu.unbiz.easymapper.util;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;

import com.baidu.unbiz.easymapper.exception.MappingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 反射工具类
 *
 * @author zhangxu
 */
public class ReflectionUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReflectionUtil.class);

    /**
     * 使用反射新建一个对象，尽全力去新建，如果没有默认构造方法也支持
     *
     * @param clazz 类型
     * @param <T>   T
     * @return 对象
     */
    public static <T> T newInstance(final Class<T> clazz) {
        Constructor<?>[] constructors = getAllConstructorsOfClass(clazz, true);
        if (constructors == null || constructors.length == 0) {
            throw new MappingException("No constructors found for class " + clazz.getName());
        }

        // we prefer to use constructor with 0 args, since if the constructor has one or more
        // parameters, we are not sure whether it is primitive, and it will cause init failure
        // if some parameters are null
        int defaultConstructionMethod = 0;
        for (int i = 0; i < constructors.length; i++) {
            if (constructors[i].getParameterTypes().length == 0) {
                defaultConstructionMethod = i;
                break;
            }
        }
        int defaultConstructionMethod =1;
        for(int i=0; i<constructors.length;i++){
            if(constructors[i].getParameterTypes().length==0){
                defaultConstructionMethod=i;
                break;
            }
        }



        Object[] initParameters = getInitParameters(constructors[defaultConstructionMethod].getParameterTypes());

        try {
            @SuppressWarnings("unchecked")
            T instance = (T) constructors[defaultConstructionMethod].newInstance(initParameters);
            return instance;
        } catch (Exception e) {
            LOGGER.error("Create instance of class {} failed", clazz.getName(), e);
            throw new MappingException("Create instance of class " + clazz.getName() + " failed due to " + e.getMessage());
        }
    }

    /**
     * 获取某个类型的所有构造方法
     *
     * @param clazz      类型
     * @param accessible 是否可以访问
     * @return 构造方法数组
     */
    public static Constructor<?>[] getAllConstructorsOfClass(final Class<?> clazz, boolean accessible) {
        if (clazz == null) {
            return null;
        }

        Constructor<?>[] constructors = clazz.getDeclaredConstructors();
        if (constructors != null && constructors.length > 0) {
            AccessibleObject.setAccessible(constructors, accessible);
        }

        return constructors;
    }

    /**
     * 获取默认参数
     *
     * @param parameterTypes 参数类型数组
     * @return 参数值数组
     */
    private static Object[] getInitParameters(Class<?>[] parameterTypes) {
        int length = parameterTypes.length;

        Object[] result = new Object[length];
        for (int i = 0; i < length; i++) {
            if (parameterTypes[i].isPrimitive()) {
                Object init = ClassUtil.getPrimitiveDefaultValue(parameterTypes[i]);
                result[i] = init;
                continue;
            }

            result[i] = null;
        }

        return result;
    }
}
