package com.baidu.unbiz.easymapper.mapping;

import com.baidu.unbiz.easymapper.codegen.SourceCodeContext;
import com.baidu.unbiz.easymapper.metadata.FieldMap;
import com.baidu.unbiz.easymapper.metadata.VariableRef;

/**
 * 属性之间做转换的实际代码逻辑生成接口
 *
 * @author zhangxu
 */
public interface MappingHandler {

    /**
     * 是否可以处理源属性和目标属性
     *
     * @param fieldMap 属性映射关系
     *
     * @return 如果可以处理返回true
     */
    boolean canApply(FieldMap fieldMap);

    /**
     * 根据属性映射关系生成代码
     *
     * @param fieldMap    属性映射关系
     * @param source      包含源对象的一个辅助类
     * @param destination 包含目标对象的一个辅助类
     * @param code        源代码上下文
     *
     * @return 源对象到目标对象拷贝的逻辑代码
     */
    String generateMappingCode(FieldMap fieldMap, VariableRef source, VariableRef destination, SourceCodeContext code);

}
