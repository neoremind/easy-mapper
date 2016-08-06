package com.baidu.unbiz.easymapper.mapping.impl;

import static java.lang.String.format;

import com.baidu.unbiz.easymapper.codegen.SourceCodeContext;
import com.baidu.unbiz.easymapper.mapping.MappingHandler;
import com.baidu.unbiz.easymapper.metadata.FieldMap;
import com.baidu.unbiz.easymapper.metadata.Property;
import com.baidu.unbiz.easymapper.metadata.VariableRef;

/**
 * 一般用于“拖底”的对象映射，不能处理<code>Collection</code>、<code>Map</code>和<code>Array</code>，
 * 使用{@link com.baidu.unbiz.easymapper.Mapper}来做级联的映射。
 * <p>
 * Note: 默认的策略是当源对象某个属性不为空并且目标对象的属性为空时候，才拷贝，否则就不动了。
 *
 * @author zhangxu
 */
public class Object2ObjectMappingHandler extends AbstractMappingHandler implements MappingHandler {

    @Override
    public boolean canApply(FieldMap fieldMap) {
        return !isPropertyArrayOrCollectionOrMap(fieldMap.getSource()) &&
                !isPropertyArrayOrCollectionOrMap(fieldMap.getDestination());
    }

    @Override
    public String generateMappingCode(FieldMap fieldMap, VariableRef source, VariableRef destination,
                                      SourceCodeContext code) {
        String mapNewObject = destination.assignIfPossible(
                format("(%s)%s.map(%s, %s)", destination.typeName(), code.getMapper(), source,
                        destination.type().getCanonicalName() + ".class"));
        String mapStmt = format(" %s { %s; }", destination.ifNull(), mapNewObject);

        String mapNull = destination.isAssignable() && fieldMap.isMapOnNull() ? format(" else {\n %s { %s; }\n}\n",
                destination.ifNotNull(), destination.assign("null")) : "";
        return statement("%s { \n %s \n } %s", source.ifNotNull(), mapStmt, mapNull);

    }

    /**
     * 属性是否为<code>Collection</code>、<code>Map</code>和<code>Array</code>
     *
     * @param property 属性
     *
     * @return 如果为<code>Collection</code>、<code>Map</code>和<code>Array</code>则返回true
     */
    private boolean isPropertyArrayOrCollectionOrMap(Property property) {
        return property.isArray() || property.isCollection() || property.isMap();
    }
}
