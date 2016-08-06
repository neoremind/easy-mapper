package com.baidu.unbiz.easymapper.exception;

/**
 * 属性映射{@link com.baidu.unbiz.easymapper.codegen.AtoBMapping}字节码生成过程中抛出的异常
 *
 * @author zhangxu
 */
public class MappingCodeGenerationException extends RuntimeException {

    private static final long serialVersionUID = -5278506190465957728L;

    public MappingCodeGenerationException() {
    }

    public MappingCodeGenerationException(String message, Throwable cause) {
        super(message, cause);
    }

    public MappingCodeGenerationException(String message) {
        super(message);
    }

    public MappingCodeGenerationException(Throwable cause) {
        super(cause);
    }

}
