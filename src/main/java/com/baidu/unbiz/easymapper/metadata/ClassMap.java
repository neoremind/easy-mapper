package com.baidu.unbiz.easymapper.metadata;

import java.util.List;

import com.baidu.unbiz.easymapper.codegen.AtoBMapping;
import com.baidu.unbiz.easymapper.util.MappedTypePair;

/**
 * 类型映射关系
 *
 * @param <A> 源类型
 * @param <B> 目标类型
 */
public class ClassMap<A, B> implements MappedTypePair<A, B> {

    /**
     * 源类型
     */
    private Type<A> aType;

    /**
     * 目标类型
     */
    private Type<B> bType;

    /**
     * 待映射的属性关系列表
     */
    private List<FieldMap> fieldsMapping;

    /**
     * 当源对象某个属性为空是否，是否映射到目标对象中
     */
    private Boolean mapOnNull;

    /**
     * 用户自定义的mapping
     */
    private AtoBMapping<A, B> customMapping;

    /**
     * 构造方法
     *
     * @param aType         源类型
     * @param bType         目标类型
     * @param fieldsMapping 待映射的属性关系列表
     * @param mapOnNull     当源对象某个属性为空是否，是否映射到目标对象中
     * @param customMapping 用户自定义的mapping
     */
    public ClassMap(Type<A> aType, Type<B> bType, List<FieldMap> fieldsMapping, boolean mapOnNull,
                    AtoBMapping<A, B> customMapping) {
        this.aType = aType;
        this.bType = bType;
        this.fieldsMapping = fieldsMapping;
        this.mapOnNull = mapOnNull;
        this.customMapping = customMapping;
    }

    public Type<A> getAType() {
        return aType;
    }

    public Type<B> getBType() {
        return bType;
    }

    public String getATypeName() {
        return aType.getSimpleName();
    }

    public String getBTypeName() {
        return bType.getSimpleName();
    }

    public String getMapperClassName() {
        return "EasyMapper_" + getBTypeName() + "_" + getATypeName() + "_Mapper";
    }

    @Override
    public int hashCode() {
        int result = 31;
        result = result + (aType == null ? 0 : aType.hashCode());
        result = result + (bType == null ? 0 : bType.hashCode());
        return result;
    }

    public String toString() {
        return getClass().getSimpleName() + "([A]:" + aType + ", [B]:" + bType + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ClassMap<?, ?> classMap = (ClassMap<?, ?>) o;
        if (aType != null ? !aType.equals(classMap.aType) : classMap.aType != null) {
            return false;
        }
        if (bType != null ? !bType.equals(classMap.bType) : classMap.bType != null) {
            return false;
        }
        return true;
    }

    public Type<A> getaType() {
        return aType;
    }

    public Type<B> getbType() {
        return bType;
    }

    public Boolean getMapOnNull() {
        return mapOnNull;
    }

    public List<FieldMap> getFieldsMapping() {
        return fieldsMapping;
    }

    public AtoBMapping<A, B> getCustomMapping() {
        return customMapping;
    }

}
