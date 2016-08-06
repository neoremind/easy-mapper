package com.baidu.unbiz.easymapper.util;

/**
 * 系统环境变量工具类
 *
 * @author zhangxu
 */
public class SystemPropertyUtil {

    /**
     * 是否把源文件在字节码生成过程中输出到本地文件
     */
    public static final String ENABLE_WRITE_SOURCE_FILE = "com.baidu.unbiz.easymapper.enableWriteSourceFile";

    /**
     * 如果{@link #ENABLE_WRITE_SOURCE_FILE}为true，那么把源文件写入的本地文件绝对路径。
     * <p>
     * 如果不设置设置，而{@link #ENABLE_WRITE_SOURCE_FILE}为仍然为true，则默认输出到"classpath:/com/baidu/unbiz/easymapper/generated/".
     */
    public static final String WRITE_SOURCE_FILE_ABSOLUTE_PATH =
            "com.baidu.unbiz.easymapper.writeSourceFileAbsolutePath";

    /**
     * 是否把Class字节码在字节码生成过程中输出到本地文件
     */
    public static final String ENABLE_WRITE_CLASS_FILE = "com.baidu.unbiz.easymapper.enableWriteClassFile";

    /**
     * 如果{@link #ENABLE_WRITE_CLASS_FILE}为true，那么把源文件写入的本地文件绝对路径。
     * <p>
     * 如果不设置设置，而{@link #ENABLE_WRITE_CLASS_FILE}为仍然为true，则默认输出到"classpath:/com/baidu/unbiz/easymapper/generated/".
     */
    public static final String WRITE_CLASS_FILE_ABSOLUTE_PATH =
            "com.baidu.unbiz.easymapper.writeClassFileAbsolutePath";

    /**
     * Get system property using the same key
     *
     * @param key         -D parameter or  shell defined system environment property
     * @param defautValue default value
     *
     * @return system property string
     */
    public static String getSystemProperty(String key, String defautValue) {
        return getSystemProperty(key, key, defautValue);
    }

    /**
     * Get system property
     *
     * @param dKey        -D parameter
     * @param shellKey    shell defined system environment property
     * @param defautValue default value
     *
     * @return system property string
     */
    public static String getSystemProperty(String dKey, String shellKey, String defautValue) {
        String value = System.getProperty(dKey);
        if (value == null || value.length() == 0) {
            value = System.getenv(shellKey);
            if (value == null || value.length() == 0) {
                value = defautValue;
            }
        }
        return value;
    }

}
