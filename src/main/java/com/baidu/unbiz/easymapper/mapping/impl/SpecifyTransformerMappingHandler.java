package com.baidu.unbiz.easymapper.mapping.impl;

import com.baidu.unbiz.easymapper.codegen.SourceCodeContext;
import com.baidu.unbiz.easymapper.mapping.MappingHandler;
import com.baidu.unbiz.easymapper.metadata.FieldMap;
import com.baidu.unbiz.easymapper.metadata.VariableRef;

/**
 * 如果用户自己指定了映射关系逻辑{@link com.baidu.unbiz.easymapper.transformer.Transformer}，则使用，框架不做额外处理
 *
 * @author zhangxu
 */
public class SpecifyTransformerMappingHandler extends AbstractMappingHandler implements MappingHandler {

    @Override
    public boolean canApply(FieldMap fieldMap) {
        return fieldMap.getTransformer() != null;
    }

    @Override
    public String generateMappingCode(FieldMap fieldMap, VariableRef source, VariableRef destination,
                                      SourceCodeContext code) {
        String assignStatement =
                destination.assignIfPossible("%s.transform(%s)", code.getTransformer(fieldMap.getTransformer()),
                        source.asWrapper());
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
