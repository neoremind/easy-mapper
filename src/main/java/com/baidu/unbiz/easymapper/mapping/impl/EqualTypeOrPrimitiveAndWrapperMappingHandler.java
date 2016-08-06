package com.baidu.unbiz.easymapper.mapping.impl;

import com.baidu.unbiz.easymapper.codegen.SourceCodeContext;
import com.baidu.unbiz.easymapper.mapping.MappingHandler;
import com.baidu.unbiz.easymapper.metadata.FieldMap;
import com.baidu.unbiz.easymapper.metadata.Type;
import com.baidu.unbiz.easymapper.metadata.VariableRef;

/**
 * 如果type相同、或者都是基本类型以及wrapper类型就用这个处理器直接映射，这里对于type相同采用引用拷贝的策略
 *
 * @author zhangxu
 */
public class EqualTypeOrPrimitiveAndWrapperMappingHandler extends AbstractMappingHandler implements MappingHandler {

    @Override
    public boolean canApply(FieldMap fieldMap) {
        Type<?> sourceType = fieldMap.getAType();
        Type<?> destinationType = fieldMap.getBType();
        return (sourceType.equals(destinationType) || sourceType
                .isWrapperFor(destinationType) || destinationType.isWrapperFor(sourceType));
    }

    @Override
    public String generateMappingCode(FieldMap fieldMap, VariableRef source, VariableRef destination,
                                      SourceCodeContext code) {
        String assignStatement = destination.assignIfPossible(source);
        if (!source.isNullPossible()) {
            return statement(assignStatement);
        } else {
            String elseSetNull = "";
            if (fieldMap.isMapOnNull()) {
                elseSetNull =
                        " else " + destination.ifNotNull() + "{ \n" + destination.assignIfPossible("null") + ";\n }";
            }
            return statement(source.ifNotNull() + "{ \n" + assignStatement) + "\n}" + elseSetNull;
        }
    }

}
