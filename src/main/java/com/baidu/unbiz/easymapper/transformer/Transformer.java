package com.baidu.unbiz.easymapper.transformer;

/**
 * 用户自定义的属性转换接口
 *
 * @author zhangxu
 */
public interface Transformer<S, D> {

    /**
     * 自定义属性转换
     *
     * @param source 源对象
     *
     * @return 目标对象
     */
    D transform(S source);

}
