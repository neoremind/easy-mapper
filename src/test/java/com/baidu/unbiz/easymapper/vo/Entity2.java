package com.baidu.unbiz.easymapper.vo;

/**
 * @author xu.zhang
 */
public class Entity2 {
    String name;

    public static Entity2 of(String name) {
        Entity2 e = new Entity2();
        e.name = name;
        return e;
    }

    @Override
    public String toString() {
        return name;
    }
}
