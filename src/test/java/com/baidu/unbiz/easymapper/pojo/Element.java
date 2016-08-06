package com.baidu.unbiz.easymapper.pojo;

import java.util.HashMap;
import java.util.Map;

public class Element {

    private Map<String, Object> attributes = new HashMap<String, Object>();

    public Object getAttribute(String name) {
        return attributes.get(name);
    }

    public void setAttribute(String name, Object value) {
        attributes.put(name, value);
    }
}