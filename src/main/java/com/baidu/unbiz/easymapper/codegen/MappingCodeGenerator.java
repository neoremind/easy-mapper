package com.baidu.unbiz.easymapper.codegen;

import static java.lang.String.format;

import java.util.LinkedHashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baidu.unbiz.easymapper.Mapper;
import com.baidu.unbiz.easymapper.codegen.bytecode.ByteCodeManipulator;
import com.baidu.unbiz.easymapper.codegen.bytecode.JavassistByteCodeManipulator;
import com.baidu.unbiz.easymapper.mapping.DefaultMappingStrategy;
import com.baidu.unbiz.easymapper.metadata.ClassMap;
import com.baidu.unbiz.easymapper.metadata.FieldMap;
import com.baidu.unbiz.easymapper.metadata.VariableRef;

/**
 * 框架内部使用的映射对象{@link AtoBMapping}的生成器
 *
 * @author zhangxu
 */
public final class MappingCodeGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(MappingCodeGenerator.class);

    /**
     * 字节码操作工具
     */
    private ByteCodeManipulator byteCodeManipulator;

    /**
     * 将属性做转换、映射的策略
     */
    private DefaultMappingStrategy mappingStrategy;

    /**
     * 默认如果生成源代码失败，则返回这个内部类对象，不返回null给上层
     */
    public static final GeneratedAtoBMappingBase ABSENT_MAPPING = new GeneratedAtoBMappingBase() {
        @Override
        public void map(Object o, Object o2) {
            // nothing to do
        }
    };

    /**
     * 构造方法
     */
    public MappingCodeGenerator() {
        byteCodeManipulator = new JavassistByteCodeManipulator();
        mappingStrategy = new DefaultMappingStrategy();
    }

    /**
     * 根据类型映射关系，使用字节码操作工具，在运行时生成{@link AtoBMapping}的具体实现对象，供客户端做bean映射使用
     *
     * @param classMap 类型映射关系
     * @param mapper   客户端真正执行mapping操作的入口类
     *
     * @return 运行时生成{@link AtoBMapping}的具体实现对象
     */
    public GeneratedAtoBMappingBase build(ClassMap<?, ?> classMap, Mapper mapper) {
        try {
            SourceCodeContext sourceCodeContext =
                    new SourceCodeContext(classMap.getMapperClassName(), GeneratedAtoBMappingBase.class);
            addMapMethod(sourceCodeContext, classMap);
            GeneratedAtoBMappingBase a2bMapping =
                    (GeneratedAtoBMappingBase) byteCodeManipulator.compileClass(sourceCodeContext).newInstance();
            if (LOGGER.isDebugEnabled()) {
                LOGGER.info("Mapping code is\n{}", sourceCodeContext.toSourceFile());
            }
            a2bMapping.setTransformers(sourceCodeContext.getTransformers());
            a2bMapping.setMapper(mapper);
            a2bMapping.setCustomMapping(classMap.getCustomMapping());
            return a2bMapping;
        } catch (Exception e) {
            LOGGER.error("Generating mapping code with error: " + e.getLocalizedMessage(), e);
        }
        return ABSENT_MAPPING;
    }

    /**
     * 加入{@link AtoBMapping#map(Object, Object)}方法到源代码上下文环境中
     *
     * @param code     源代码上下文
     * @param classMap 类型映射关系
     *
     * @return
     */
    private Set<FieldMap> addMapMethod(SourceCodeContext code, ClassMap<?, ?> classMap) {
        Set<FieldMap> mappedFields = new LinkedHashSet<FieldMap>();

        final StringBuilder out = new StringBuilder();
        out.append("\tpublic void map");
        out.append(format("(java.lang.Object a, java.lang.Object b) {\n\n"));
        out.append(format("%s source = ((%s)a);\n", classMap.getAType().getCanonicalName(), classMap.getaType()
                .getCanonicalName()));
        out.append(format("%s destination = ((%s)b);", classMap.getbType().getCanonicalName(), classMap
                .getbType().getCanonicalName()));

        for (FieldMap currentFieldMap : classMap.getFieldsMapping()) {
            FieldMap fieldMap = currentFieldMap;
            mappedFields.add(currentFieldMap);
            String sourceCode = generateMappingCode(code, fieldMap);
            out.append(sourceCode);
        }

        out.append("\n\t\tif(customMapping != null) { \n\t\t\t customMapping.map")
                .append("(source, destination);\n\t\t}");

        out.append("");
        out.append("\n\t}");

        code.addMethod(out.toString());

        return mappedFields;
    }

    /**
     * 生成具体的mapping方法逻辑
     *
     * @param code     源代码上下文
     * @param fieldMap 要映射的属性对
     *
     * @return 映射的逻辑代码
     */
    private String generateMappingCode(SourceCodeContext code, FieldMap fieldMap) {
        final VariableRef sourceProperty = new VariableRef(fieldMap.getSource(), "source");
        final VariableRef destinationProperty = new VariableRef(fieldMap.getDestination(), "destination");

        if (!sourceProperty.isReadable() || ((!destinationProperty.isAssignable()))) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.warn("Skip {} due to source property is not readable or destination property is not assignable",
                        fieldMap);
            }
            return "";
        }

        return mappingStrategy.generateMappingCode(fieldMap, sourceProperty, destinationProperty, code);
    }

}
