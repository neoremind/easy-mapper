package com.baidu.unbiz.easymapper.codegen;

/**
 * 框架内部使用的从A到B的映射接口，使用字节码生成工具生成该接口的具体实现，可以直接由Classloader加载到JVM中使用，
 * 一旦建立后续的所有映射工作都可以直接使用具体的实现来执行，相比于传统的通过反射机制来做的映射，这种方式避免的过高的性能开销。
 *
 * @author zhangxu
 */
public interface AtoBMapping<A, B> {

    /**
     * 把A类型的对象中的属性，级联地复制到B类型的对象中
     *
     * @param a 输入的源对象
     * @param b 待赋值映射的目标对象
     */
    void map(A a, B b);

}
