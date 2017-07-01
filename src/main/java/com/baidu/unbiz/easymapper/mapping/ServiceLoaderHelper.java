package com.baidu.unbiz.easymapper.mapping;

import java.util.ServiceLoader;

/**
 * ServiceLoaderHelper
 * <p>
 * To acquire service loader in a thread safe way.
 *
 * @author xu.zhang
 */
public class ServiceLoaderHelper {

    private static volatile ServiceLoaderHelper INSTANCE = null;
    private static final Object LOCK = new Object();

    /**
     * 属性之间做转换的实际代码逻辑生成的各种策略处理器
     */
    private static volatile ServiceLoader<MappingHandler> handlers;

    public static ServiceLoaderHelper getInstance() {
        if (INSTANCE == null) {
            synchronized (LOCK) {
                if (INSTANCE == null) {
                    INSTANCE = new ServiceLoaderHelper();
                }
            }
        }
        return INSTANCE;
    }

    public ServiceLoader<MappingHandler> getServiceLoader() {
        return handlers;
    }

    /**
     * 使用SPI基础从<code>META-INF/services</code>中加载处理器
     * <p/>
     * fix issue: https://github.com/neoremind/easy-mapper/issues/6
     */
    private ServiceLoaderHelper() {
        handlers = ServiceLoader.load(MappingHandler.class);
    }

}
