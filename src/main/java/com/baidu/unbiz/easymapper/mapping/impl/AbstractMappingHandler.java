package com.baidu.unbiz.easymapper.mapping.impl;

import static java.lang.String.format;

import com.baidu.unbiz.easymapper.mapping.MappingHandler;

/**
 * 属性之间做转换的实际代码逻辑生成抽象类
 *
 * @author zhangxu
 */
public abstract class AbstractMappingHandler implements MappingHandler {

    /**
     * 生成代码语句，填充前缀或者后缀
     *
     * @param str  带有通配符的字符串
     * @param args 填充进去的值
     *
     * @return 代码语句
     */
    public static String statement(String str, Object... args) {
        if (str != null && !"".equals(str.trim())) {
            String expr = format(str, args);
            String prefix = "";
            String suffix = "";
            if (!expr.startsWith("\n") || expr.startsWith("}")) {
                prefix = "\n";
            }
            String trimmed = expr.trim();
            if (!"".equals(trimmed) && !trimmed.endsWith(";") && !trimmed.endsWith("}") && !trimmed.endsWith("{")
                    && !trimmed.endsWith("(")) {
                suffix = "; ";
            }
            return prefix + expr + suffix;
        } else if (str != null) {
            return str;
        }
        return "";
    }

}
