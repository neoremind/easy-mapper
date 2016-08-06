package com.baidu.unbiz.easymapper.metadata;

import com.baidu.unbiz.easymapper.transformer.Transformer;
import com.baidu.unbiz.easymapper.util.MappedTypePair;

/**
 * 属性关系映射
 */
public class FieldMap implements MappedTypePair<Object, Object> {

    /**
     * 源属性
     */
    private final Property source;

    /**
     * 目标属性
     */
    private final Property destination;

    /**
     * 当源对象某个属性为空是否，是否映射到目标对象中
     */
    private final boolean mapOnNull;

    /**
     * 用户自定义属性转换器
     */
    private final Transformer<Object, Object> transformer;

    public FieldMap(Property a, Property b, boolean mapOnNull,
                    Transformer<Object, Object> transformer) {
        this.source = a;
        this.destination = b;
        this.mapOnNull = mapOnNull;
        this.transformer = transformer;
    }

    public Property getSource() {
        return source;
    }

    public Property getDestination() {
        return destination;
    }

    public Type<Object> getAType() {
        return (Type<Object>) getSource().getType();
    }

    public Type<Object> getBType() {
        return (Type<Object>) getDestination().getType();
    }

    public Transformer<Object, Object> getTransformer() {
        return transformer;
    }

    public boolean isMapOnNull() {
        return mapOnNull;
    }

    @Override
    public String toString() {
        return "FieldMap[" + source.getName() + "(" + source.getType() + ")-->" + destination.getName() + "("
                + destination.getType() + ")]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((destination == null) ? 0 : destination.hashCode());
        result = prime * result + ((source == null) ? 0 : source.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        FieldMap fieldMap = (FieldMap) o;
        if (mapOnNull != fieldMap.mapOnNull) {
            return false;
        }
        if (source != null ? !source.equals(fieldMap.source) : fieldMap.source != null) {
            return false;
        }
        if (destination != null ? !destination.equals(fieldMap.destination) : fieldMap.destination != null) {
            return false;
        }
        return !(transformer != null ? !transformer.equals(fieldMap.transformer) : fieldMap.transformer != null);
    }
}
