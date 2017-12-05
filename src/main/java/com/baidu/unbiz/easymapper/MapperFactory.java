package com.baidu.unbiz.easymapper;

/**
 * 获取单例Mapper的简单工厂
 *
 * @author zhangxu
 */
public class MapperFactory {

    /**
     * 默认的Mapper
     */
    private static volatile Mapper DEFAULT_MAPPER;

    private static final Object LOCK = new Object();

    /**
     * 获取拷贝引用的Mapper
     *
     * @return Mapper
     */
    public static Mapper getCopyByRefMapper() {
        if (DEFAULT_MAPPER == null) {
            synchronized(LOCK) {
                if (DEFAULT_MAPPER == null) {
                    DEFAULT_MAPPER = new CopyByRefMapper();
                }
            }
        }
        return DEFAULT_MAPPER;
    }
}
