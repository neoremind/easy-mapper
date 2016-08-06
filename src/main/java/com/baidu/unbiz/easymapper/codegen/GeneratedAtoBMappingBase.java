package com.baidu.unbiz.easymapper.codegen;

import com.baidu.unbiz.easymapper.Mapper;
import com.baidu.unbiz.easymapper.transformer.Transformer;

/**
 * 生成的字节码的父类，主要是将一些属性注入进去，便于具体的映射实现子类去使用这些变量。
 *
 * @author zhangxu
 */
public abstract class GeneratedAtoBMappingBase implements AtoBMapping {

    /**
     * 自定义的属性转换规则
     */
    protected Transformer<Object, Object>[] transformers;

    /**
     * 做属性映射的执行者，客户端代码的调用入口，一般用于级联的映射
     */
    protected Mapper mapper;

    /**
     * 用户自定义的mapping行为
     */
    protected AtoBMapping customMapping;

    // getter and setter...

    public void setTransformers(
            Transformer<Object, Object>[] transformers) {
        this.transformers = transformers;
    }

    public void setMapper(Mapper mapper) {
        this.mapper = mapper;
    }

    public void setCustomMapping(AtoBMapping customMapping) {
        this.customMapping = customMapping;
    }
}