package com.baidu.unbiz.easymapper.codegen.bytecode;

import com.baidu.unbiz.easymapper.codegen.SourceCodeContext;
import com.baidu.unbiz.easymapper.exception.MappingCodeGenerationException;

/**
 * 字节码操作接口
 *
 * @author zhangxu
 */
public interface ByteCodeManipulator {

    /**
     * 从源代码上下文中编译为Class，内部需要主动调用ClassLoader加载到JVM中
     *
     * @param sourceCode 源代码上下文
     *
     * @return 编译后的class
     *
     * @throws MappingCodeGenerationException
     */
    Class<?> compileClass(SourceCodeContext sourceCode) throws MappingCodeGenerationException;

}
