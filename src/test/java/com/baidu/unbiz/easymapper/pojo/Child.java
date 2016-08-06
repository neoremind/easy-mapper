package com.baidu.unbiz.easymapper.pojo;

import java.util.List;

import com.google.common.collect.Lists;

public class Child extends AbstractChild implements Keyable<Long>, Buildable<List<String>> {

    @Override
    public List getChildren() {
        return null;
    }

    @Override
    public Long getId() {
        return Long.MAX_VALUE;
    }

    @Override
    public List<String> build() {
        return Lists.newArrayList();
    }
}

