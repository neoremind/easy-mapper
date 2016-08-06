package com.baidu.unbiz.easymapper.mapping.impl;

import com.baidu.unbiz.easymapper.codegen.SourceCodeContext;
import com.baidu.unbiz.easymapper.mapping.MappingHandler;
import com.baidu.unbiz.easymapper.metadata.FieldMap;
import com.baidu.unbiz.easymapper.metadata.VariableRef;

/**
 * 如果目标类是源类的一个子类，那么直接拷贝引用
 *
 * @author zhangxu
 */
public class CanBeAssignedMappingHandler extends AbstractMappingHandler implements MappingHandler {

    @Override
    public boolean canApply(FieldMap fieldMap) {
        return fieldMap.getDestination().isAssignableFrom(fieldMap.getSource());
    }

    @Override
    public String generateMappingCode(FieldMap fieldMap, VariableRef source, VariableRef destination,
                                      SourceCodeContext code) {
        StringBuilder out = new StringBuilder();
        if (!source.isPrimitive()) {
            out.append("\n" + source.ifNotNull() + "{");
        }
        out.append(statement(destination.assignIfPossible(source)));
        if (!source.isPrimitive()) {
            out.append("\n }");
            if (fieldMap.isMapOnNull() && !destination.isPrimitive()) {
                out.append(" else {" +
                        destination.assignIfPossible("null") +
                        "\n }");
            }
        }
        return out.toString();
    }

}
