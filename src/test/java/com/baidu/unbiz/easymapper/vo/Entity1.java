package com.baidu.unbiz.easymapper.vo;

/**
 * @author xu.zhang
 */
public class Entity1 {
    String name;

    public static Entity1 of(String name) {
        Entity1 e = new Entity1();
        e.name = name;
        return e;
    }

    @Override
    public String toString() {
        return name;
    }
}
