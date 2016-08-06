package com.baidu.unbiz.easymapper.pojo;

import java.util.List;
import java.util.Map;

public abstract class AbstractChild<T> implements ParentInterface<Map<String, Element>> {

    public abstract List<T> getChildren();

    @Override
    public Map<String, Element> getContent() {
        return null;
    }

    @Override
    public void setContent(Map<String, Element> content) {

    }
}
