package com.baidu.unbiz.easymapper;

import com.baidu.unbiz.easymapper.metadata.ClassMap;
import com.baidu.unbiz.easymapper.metadata.MapperKey;

/**
 * 提供两个JavaBean属性单向映射能力的接口。
 * <p>
 * 常见的POJO/VO/PO/BO/DTO等各种对象可以互相转换，利用该接口可以实现bean之间属性的拷贝和转换。
 *
 * @author zhangxu
 */
public interface Mapper {

    /**
     * 执行mapping操作
     *
     * @param sourceObject     源对象
     * @param destinationClass 目标类型
     * @param <A>              源类型
     * @param <B>              目标类型
     *
     * @return 目标对象
     */
    <A, B> B map(A sourceObject, Class<B> destinationClass);

    /**
     * 执行mapping操作
     *
     * @param sourceObject      源对象
     * @param destinationObject 目标对象
     * @param <A>               源类型
     * @param <B>               目标类型
     *
     * @return 目标对象
     */
    <A, B> B map(A sourceObject, B destinationObject);

    /**
     * 从源类型到目标类型做一个映射
     *
     * @param aType 源类型
     * @param bType 目标类型
     * @param <A>   源类型class
     * @param <B>   目标类型class
     *
     * @return 类型映射关系构造器
     */
    <A, B> ClassMapBuilder<A, B> mapClass(Class<A> aType, Class<B> bType);

    /**
     * 注册类型映射关系
     *
     * @param key     映射的key
     * @param builder 映射关系构造器
     * @param <A>     源类型
     * @param <B>     目标类型
     *
     * @return 类型映射关系
     */
    <A, B> ClassMap<A, B> registerClassMap(MapperKey key, ClassMapBuilder<A, B> builder);

}
