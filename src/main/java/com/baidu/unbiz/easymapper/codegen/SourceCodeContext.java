package com.baidu.unbiz.easymapper.codegen;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.baidu.unbiz.easymapper.transformer.Transformer;

/**
 * 源代码上下文
 *
 * @author zhangxu
 */
public class SourceCodeContext {

    /**
     * 生成文件名的自增后缀
     */
    private static final AtomicInteger UNIQUE_CLASS_INDEX = new AtomicInteger();

    /**
     * {@code \u000a} 换行符 LF ('\n').
     */
    public static final char LF = '\n';

    /**
     * 源代码的{@link StringBuilder}
     */
    private StringBuilder sourceBuilder;

    /**
     * 源代码的简单类名
     */
    private String classSimpleName;

    /**
     * 源代码的包名
     */
    private String packageName;

    /**
     * 源代码的全类名
     */
    private String className;

    /**
     * 源代码的父类
     */
    private Class<?> superClass;

    /**
     * 源代码的方法
     */
    private List<String> methods;

    /**
     * 源代码的变量
     */
    private List<String> fields;

    /**
     * 自定义的属性转换关系
     *
     * @see Transformer
     */
    private List<Transformer<Object, Object>> transformers;

    /**
     * 属性转换关系数组的索引
     */
    private int transformerIndices = 0;

    /**
     * 构造方法
     *
     * @param baseClassName class文件的基础名称，一般用AtoB的形式
     * @param superClass    父类
     */
    public SourceCodeContext(final String baseClassName, Class<?> superClass) {
        this.sourceBuilder = new StringBuilder();
        String safeBaseClassName = baseClassName.replace("[]", "$Array");
        this.superClass = superClass;
        this.packageName = "com.baidu.unbiz.generated";
        this.classSimpleName = safeBaseClassName;
        this.methods = new ArrayList<String>();
        this.fields = new ArrayList<String>();

        this.classSimpleName = getUniqueClassName(this.classSimpleName);
        this.className = this.packageName + "." + this.classSimpleName;
        sourceBuilder.append("package ");
        sourceBuilder.append(packageName);
        sourceBuilder.append(LF);
        sourceBuilder.append(LF);
        sourceBuilder.append("public class ");
        sourceBuilder.append(this.classSimpleName);
        sourceBuilder.append(" extends GeneratedMapperBase {");
    }

    /**
     * 加入时间戳以及自增索引后缀的类名
     *
     * @param name 类名
     *
     * @return 加入时间戳以及自增索引后缀的类名
     */
    private String getUniqueClassName(String name) {
        return name + System.nanoTime() + "$" + UNIQUE_CLASS_INDEX.getAndIncrement();
    }

    /**
     * 生成源代码
     *
     * @return 源代码
     */
    public String toSourceFile() {
        return sourceBuilder.toString() + LF + "}";
    }

    /**
     * 加入方法代码
     */
    public void addMethod(String methodSource) {
        sourceBuilder.append(LF);
        sourceBuilder.append(methodSource);
        sourceBuilder.append(LF);
        this.methods.add(methodSource);
    }

    /**
     * 获取mapper的名称，用于级联的情况需要做映射
     *
     * @return mapper的名称，即客户端的入口
     */
    public String getMapper() {
        return "mapper";
    }

    /**
     * 返回所有的自定义属性转换关系
     *
     * @return 自定义属性转换关系
     */
    public Transformer<Object, Object>[] getTransformers() {
        if (transformers == null) {
            return null;
        }
        return transformers.toArray(new Transformer[] {});
    }

    /**
     * 在生成代码过程中获取拿到自定义属性转换关系的代码
     *
     * @param transformer 自定义属性转换关系
     *
     * @return 代码
     */
    public String getTransformer(Transformer<Object, Object> transformer) {
        if (transformers == null) {
            transformers = new ArrayList<Transformer<Object, Object>>(8);
        }
        transformers.add(transformer);
        return "(transformers[" + transformerIndices++ + "])";
    }

    // getter and setter...

    public Class<?> getSuperClass() {
        return superClass;
    }

    public String getClassSimpleName() {
        return classSimpleName;
    }

    public String getPackageName() {
        return packageName;
    }

    public String getClassName() {
        return className;
    }

    public List<String> getMethods() {
        return methods;
    }

    public void setMethods(List<String> methods) {
        this.methods = methods;
    }

    public List<String> getFields() {
        return fields;
    }

    public void setFields(List<String> fields) {
        this.fields = fields;
    }

}
