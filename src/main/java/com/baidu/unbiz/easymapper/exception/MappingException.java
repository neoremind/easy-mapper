package com.baidu.unbiz.easymapper.exception;

/**
 * 映射过程中的异常
 *
 * @author zhangxu
 */
public class MappingException extends RuntimeException {

    private static final long serialVersionUID = -5278506190465957728L;

    public MappingException() {
    }

    public MappingException(String message, Throwable cause) {
        super(message, cause);
    }

    public MappingException(String message) {
        super(message);
    }

    public MappingException(Throwable cause) {
        super(cause);
    }

}
