package com.baidu.unbiz.easymapper.util;

import com.baidu.unbiz.easymapper.metadata.Type;

/**
 * A到B映射的一对接口
 *
 * @author zhangxu
 */
public interface MappedTypePair<A, B> {

    /**
     * 返回A类型
     *
     * @return A类型
     */
    Type<A> getAType();

    /**
     * 返回B类型
     *
     * @return B类型
     */
    Type<B> getBType();
}
