package com.baidu.unbiz.easymapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.baidu.unbiz.easymapper.codegen.AtoBMapping;
import com.baidu.unbiz.easymapper.metadata.ClassMap;
import com.baidu.unbiz.easymapper.metadata.FieldMap;
import com.baidu.unbiz.easymapper.metadata.IntrospectorPropertyResolver;
import com.baidu.unbiz.easymapper.metadata.MapperKey;
import com.baidu.unbiz.easymapper.metadata.Property;
import com.baidu.unbiz.easymapper.metadata.PropertyResolver;
import com.baidu.unbiz.easymapper.metadata.Type;
import com.baidu.unbiz.easymapper.transformer.Transformer;
import com.baidu.unbiz.easymapper.util.MappedTypePair;

/**
 * 类映射关系的构造器
 *
 * @param <A> 源类型
 * @param <B> 目标类型
 *
 * @author zhangxu
 */
public class ClassMapBuilder<A, B> implements MappedTypePair {

    /**
     * 源类型
     */
    private final Type<A> aType;

    /**
     * 目标类型
     */
    private final Type<B> bType;

    /**
     * 源类型的属性Map，键为属性名，值为属性
     */
    private Map<String, Property> aProperties;

    /**
     * 目标类型的属性Map，键为属性名，值为属性
     */
    private Map<String, Property> bProperties;

    /**
     * 用户自定义的映射扩展策略，主要做{@link Transformer}的存储
     */
    private Map<String, MappingExtension> mappingExtensions;

    /**
     * 不进行映射的属性名称
     */
    private Set<String> excludeProperties;

    /**
     * 最终调用{@link #build()}生成的属性映射关系，用于后续对象做mapping的参考依据
     */
    private List<FieldMap> fieldsMapping;

    /**
     * 当源对象中的某个属性为空时，是否映射到目标对象
     */
    private boolean mapOnNull;

    /**
     * 用户自定义的mapping，用于字节码生成的其他策略的补充逻辑
     */
    private AtoBMapping<A, B> customMapping;

    /**
     * 属性解析器
     */
    private PropertyResolver propertyResolver;

    /**
     * 客户端调用的入口mapper
     */
    private Mapper mapper;

    /**
     * 构造方法
     *
     * @param aType  源类型
     * @param bType  目标类型
     * @param mapper 客户端调用的入口mapper
     */
    public ClassMapBuilder(Type<A> aType, Type<B> bType, Mapper mapper) {
        this.aType = aType;
        this.bType = bType;
        this.mapper = mapper;
        propertyResolver = new IntrospectorPropertyResolver();
    }

    /**
     * 属性映射
     *
     * @param sourceField 源类型中的属性名称
     * @param targetField 目标类型中的属性名称
     *
     * @return ClassMapBuilder
     */
    public ClassMapBuilder<A, B> field(String sourceField, String targetField) {
        putMappingExtension(sourceField, new MappingExtension(targetField));
        return this;
    }

    /**
     * 属性映射
     *
     * @param sourceField 源类型中的属性名称
     * @param targetField 目标类型中的属性名称
     * @param sourceType  源属性类型
     * @param targetType  目标属性类型
     * @param transformer 自定义属性转换器
     * @param <S>         源属性的Class
     * @param <D>         目标属性的Class
     *
     * @return ClassMapBuilder
     */
    public <S, D> ClassMapBuilder<A, B> field(String sourceField, String targetField, Class<S> sourceType,
                                              Class<D> targetType, Transformer<S, D> transformer) {
        putMappingExtension(sourceField, new MappingExtension(targetField, (Transformer<Object, Object>) transformer));
        return this;
    }

    /**
     * 属性映射
     *
     * @param sourceField 源类型中的属性名称
     * @param targetField 目标类型中的属性名称
     * @param <S>         源属性的Class
     * @param <D>         目标属性的Class
     *
     * @return ClassMapBuilder
     */
    public <S, D> ClassMapBuilder<A, B> field(String sourceField, String targetField, Transformer<S, D> transformer) {
        putMappingExtension(sourceField, new MappingExtension(targetField, (Transformer<Object, Object>) transformer));
        return this;
    }

    /**
     * 当源对象中的某个属性为空时，是否映射到目标对象
     *
     * @param mapOnNull 是否映射
     *
     * @return ClassMapBuilder
     */
    public ClassMapBuilder<A, B> mapOnNull(boolean mapOnNull) {
        this.mapOnNull = mapOnNull;
        return this;
    }

    /**
     * 排除映射源类型中的某些属性
     *
     * @param properties 1个或多个属性名称
     *
     * @return ClassMapBuilder
     */
    public ClassMapBuilder<A, B> exclude(String... properties) {
        if (excludeProperties == null) {
            excludeProperties = new HashSet<String>();
        }
        if (properties != null) {
            excludeProperties.addAll(Arrays.asList(properties));
        }
        return this;
    }

    /**
     * 用户自定义的mapping策略。
     * 不受{@link #mapOnNull}和{@link #exclude(String...)}的影响。
     *
     * @param mapping 用户自定义的两个类型的映射策略
     *
     * @return ClassMapBuilder
     */
    public ClassMapBuilder<A, B> customMapping(AtoBMapping<A, B> mapping) {
        this.customMapping = mapping;
        return this;
    }

    /**
     * 构造类映射关系
     *
     * @return 类型映射关系
     */
    public ClassMap<A, B> build() {
        aProperties = propertyResolver.getProperties(aType);
        bProperties = propertyResolver.getProperties(bType);
        fieldsMapping = new ArrayList<FieldMap>();
        for (String propertyName : aProperties.keySet()) {
            if (propertyName.equals("class") ||
                    (excludeProperties != null && excludeProperties.contains(propertyName))) {
                continue;
            }
            String bPropertyName = propertyName;
            Transformer<Object, Object> transformer = null;
            if (mappingExtensions != null && mappingExtensions.containsKey(propertyName)) {
                bPropertyName = mappingExtensions.get(propertyName).getDestinationName();
                transformer = mappingExtensions.get(propertyName).getTransformer();
            }
            if (bProperties.containsKey(bPropertyName)) {
                FieldMap fieldMap = new FieldMap(aProperties.get(propertyName),
                        bProperties.get(bPropertyName), mapOnNull, transformer);
                fieldsMapping.add(fieldMap);
            }
        }
        return new ClassMap<A, B>(aType, bType, fieldsMapping, mapOnNull, customMapping);
    }

    /**
     * 向客户端入口的mapper注册类型映射关系
     *
     * @return Mapper
     */
    public Mapper register() {
        mapper.registerClassMap(new MapperKey(aType, bType), this);
        return mapper;
    }

    /**
     * 先注册，然后直接调用映射
     *
     * @param sourceObject 源对象
     * @param targetClass  目标类型
     * @param <A>          源类型class
     * @param <B>          目标类型class
     *
     * @return 映射后的目标对象
     */
    public <A, B> B registerAndMap(A sourceObject, Class<B> targetClass) {
        register();
        return mapper.map(sourceObject, targetClass);
    }

    /**
     * 先注册，然后直接调用映射
     *
     * @param sourceObject 源对象
     * @param targetObject 目标对象
     * @param <A>          源类型class
     * @param <B>          目标类型class
     *
     * @return 映射后的目标对象
     */
    public <A, B> B registerAndMap(A sourceObject, B targetObject) {
        register();
        return mapper.map(sourceObject, targetObject);
    }

    /**
     * 内部使用的置Mapping策略的方法
     *
     * @param sourceField      源属性名称
     * @param mappingExtension 属性映射策略
     */
    private void putMappingExtension(String sourceField, MappingExtension mappingExtension) {
        if (mappingExtensions == null) {
            mappingExtensions = new HashMap<String, MappingExtension>();
        }
        mappingExtensions.put(sourceField, mappingExtension);
    }

    /**
     * 获取源类型
     *
     * @return 源类型Type
     */
    public Type<A> getAType() {
        return aType;
    }

    /**
     * 获取目标类型
     *
     * @return 目标类型Type
     */
    public Type<B> getBType() {
        return bType;
    }

    /**
     * 类型映射的策略
     */
    class MappingExtension {

        /**
         * 自定义转换器
         */
        private Transformer<Object, Object> transformer;

        /**
         * 目标属性的名称
         */
        private String destinationName;

        /**
         * 构造方法
         *
         * @param destinationName 目标属性名称
         */
        public MappingExtension(String destinationName) {
            this.destinationName = destinationName;
        }

        /**
         * 构造方法
         *
         * @param destinationName 目标属性名称
         * @param transformer     自定义转换器
         */
        public MappingExtension(String destinationName,
                                Transformer<Object, Object> transformer) {
            this.destinationName = destinationName;
            this.transformer = transformer;
        }

        public Transformer<Object, Object> getTransformer() {
            return transformer;
        }

        public String getDestinationName() {
            return destinationName;
        }
    }

}
