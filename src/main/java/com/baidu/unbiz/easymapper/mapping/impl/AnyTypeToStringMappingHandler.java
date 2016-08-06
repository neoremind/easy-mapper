package com.baidu.unbiz.easymapper.mapping.impl;

import com.baidu.unbiz.easymapper.codegen.SourceCodeContext;
import com.baidu.unbiz.easymapper.mapping.MappingHandler;
import com.baidu.unbiz.easymapper.metadata.FieldMap;
import com.baidu.unbiz.easymapper.metadata.VariableRef;

/**
 * 任意类型映射到<code>String</code>
 *
 * @author zhangxu
 */
public class AnyTypeToStringMappingHandler extends AbstractMappingHandler implements MappingHandler {

    @Override
    public boolean canApply(FieldMap fieldMap) {
        return String.class.equals(fieldMap.getDestination().getType().getRawType());
    }

    @Override
    public String generateMappingCode(FieldMap fieldMap, VariableRef source, VariableRef destination,
                                      SourceCodeContext code) {
        if (source.isPrimitive()) {
            return statement(destination.assign("\"\"+ %s", source));
        } else {
            if (fieldMap.isMapOnNull()) {
                return statement(
                        "if (" + source.notNull() + ") {" + statement(destination.assign("%s.toString()", source))
                                + "} else {" + statement(destination.assign("null")) + "}");
            } else {
                return statement(source.ifNotNull() + destination.assign("%s.toString()", source));
            }
        }
    }

}
