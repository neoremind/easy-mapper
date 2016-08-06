package com.baidu.unbiz.easymapper.vo;

import java.io.Serializable;

/**
 * @author zhangxu
 */
public class BaseObject<KEY extends Serializable> implements Serializable {
    private static final long serialVersionUID = 1L;
    protected KEY id;

    public BaseObject() {
    }

    public KEY getId() {
        return this.id;
    }

    public void setId(KEY id) {
        this.id = id;
    }
}
