package com.baidu.unbiz.easymapper.mapping;

import com.baidu.unbiz.easymapper.codegen.SourceCodeContext;
import com.baidu.unbiz.easymapper.exception.MappingCodeGenerationException;
import com.baidu.unbiz.easymapper.metadata.FieldMap;
import com.baidu.unbiz.easymapper.metadata.VariableRef;

/**
 * 将属性做转换、映射的策略
 *
 * @author zhangxu
 */
public class DefaultMappingStrategy {

    /**
     * 根据属性映射关系生成代码
     *
     * @param fieldMap    属性映射关系
     * @param source      包含源对象的一个辅助类
     * @param destination 包含目标对象的一个辅助类
     * @param code        源代码上下文
     * @return 源代码
     */
    public String generateMappingCode(FieldMap fieldMap, VariableRef source, VariableRef destination,
                                      SourceCodeContext code) {
        for (MappingHandler mappingHandler : ServiceLoaderHelper.getInstance().getServiceLoader()) {
            if (mappingHandler.canApply(fieldMap)) {
                return mappingHandler.generateMappingCode(fieldMap, source, destination, code);
            }
        }
        throw new MappingCodeGenerationException("No appropriate mapping strategy found for " + fieldMap);
    }

}
