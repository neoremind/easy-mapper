package com.baidu.unbiz.easymapper.util;

/**
 * 可计算的接口
 *
 * @author zhangxu
 */
public interface Computable<A, V> {

    /**
     * 输入{@code A}返回{@code V}
     *
     * @param arg 参数
     *
     * @return V
     *
     * @throws Exception 可抛出任何异常
     */
    V compute(A arg) throws Exception;
}
