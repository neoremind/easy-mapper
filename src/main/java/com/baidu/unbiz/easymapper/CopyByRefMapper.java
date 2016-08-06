package com.baidu.unbiz.easymapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baidu.unbiz.easymapper.codegen.AtoBMapping;
import com.baidu.unbiz.easymapper.codegen.MappingCodeGenerator;
import com.baidu.unbiz.easymapper.exception.MappingException;
import com.baidu.unbiz.easymapper.metadata.ClassMap;
import com.baidu.unbiz.easymapper.metadata.MapperKey;
import com.baidu.unbiz.easymapper.metadata.TypeFactory;
import com.baidu.unbiz.easymapper.util.Computable;
import com.baidu.unbiz.easymapper.util.Memoizer;
import com.baidu.unbiz.easymapper.util.ReflectionUtil;

/**
 * 基于引用拷贝的Mapper
 *
 * @author zhangxu
 */
public class CopyByRefMapper implements Mapper {

    private static final Logger LOGGER = LoggerFactory.getLogger(CopyByRefMapper.class);

    /**
     * 类型映射关系缓存
     */
    private Memoizer<MapperKey, ClassMap<Object, Object>> classMapCache;

    /**
     * 已经经过映射注册{@link ClassMapBuilder#register()}的Mapper缓存
     */
    private Memoizer<MapperKey, AtoBMapping<Object, Object>> mapperCache;

    /**
     * 源代码生成器
     */
    private MappingCodeGenerator codeGenerator;

    /**
     * 构造方法
     */
    public CopyByRefMapper() {
        this.classMapCache = new Memoizer<MapperKey, ClassMap<Object, Object>>();
        this.mapperCache = new Memoizer<MapperKey, AtoBMapping<Object, Object>>();
        codeGenerator = new MappingCodeGenerator();
    }

    /**
     * 清理
     */
    public void clear() {
        if (this.classMapCache != null) {
            this.classMapCache.clear();
        }
        if (this.mapperCache != null) {
            this.mapperCache.clear();
        }
    }

    /**
     * 注册类型映射关系
     *
     * @param key     映射的key
     * @param builder 映射关系构造器
     * @param <A>     源类型
     * @param <B>     目标类型
     *
     * @return 类型映射关系
     */
    @Override
    public <A, B> ClassMap<A, B> registerClassMap(MapperKey key, final ClassMapBuilder<A, B> builder) {
        try {
            ClassMap<A, B> classMap = (ClassMap<A, B>) classMapCache.compute(key,
                    new Computable<MapperKey, ClassMap<Object, Object>>() {
                        @Override
                        public ClassMap<Object, Object> compute(MapperKey arg) throws Exception {
                            return (ClassMap<Object, Object>) builder.build();
                        }
                    });
            return classMap;
        } catch (InterruptedException e) {
            throw new MappingException("Memoizer has been interrupted", e);
        }
    }

    /**
     * 从源类型到目标类型做一个映射
     *
     * @param aType 源类型
     * @param bType 目标类型
     * @param <A>   源类型class
     * @param <B>   目标类型class
     *
     * @return 类型映射关系构造器
     */
    @Override
    public <A, B> ClassMapBuilder<A, B> mapClass(Class<A> aType, Class<B> bType) {
        return new ClassMapBuilder<A, B>(TypeFactory.valueOf(aType), TypeFactory.valueOf(bType), this);
    }

    /**
     * 执行mapping操作
     *
     * @param sourceObject 源对象
     * @param targetClass  目标类型
     * @param <A>          源类型
     * @param <B>          目标类型
     *
     * @return 目标对象
     *
     * @throws MappingException
     */
    @Override
    public <A, B> B map(A sourceObject, Class<B> targetClass) throws MappingException {
        return map(sourceObject, new TargetHolder<B>(null, targetClass));
    }

    /**
     * 执行mapping操作
     *
     * @param sourceObject 源对象
     * @param targetObject 目标对象
     * @param <A>          源类型
     * @param <B>          目标类型
     *
     * @return 目标对象
     *
     * @throws MappingException
     */
    @Override
    public <A, B> B map(A sourceObject, B targetObject) throws MappingException {
        return map(sourceObject, new TargetHolder<B>(targetObject, (Class<B>) targetObject.getClass()));
    }

    /**
     * 执行mapping操作
     *
     * @param sourceObject 源对象
     * @param targetHolder 目标对象holder
     * @param <A>          源类型
     * @param <B>          目标类型
     *
     * @return 目标对象
     */
    private <A, B> B map(A sourceObject, TargetHolder<B> targetHolder) {
        try {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.info("Mapping {} to {}", TypeFactory.valueOf(sourceObject.getClass()),
                        TypeFactory.valueOf(targetHolder.getTargetClass()));
            }
            MapperKey key = new MapperKey(TypeFactory.valueOf(sourceObject.getClass()),
                    TypeFactory.valueOf(targetHolder.getTargetClass()));
            final ClassMap<A, B> classMap = (ClassMap<A, B>) classMapCache.get(key);
            if (classMap == null) {
                throw new MappingException(
                        "No class map found for " + key + ", make sure type or nested type is registered beforehand");
            }
            AtoBMapping<A, B> mapper =
                    (AtoBMapping<A, B>) mapperCache.compute(key,
                            new Computable<MapperKey, AtoBMapping<Object, Object>>() {
                                @Override
                                public AtoBMapping<Object, Object> compute(MapperKey arg) throws Exception {
                                    return codeGenerator.build(classMap, self());
                                }
                            });
            if (mapper == MappingCodeGenerator.ABSENT_MAPPING) {
                throw new MappingException("Generating mapping code failed for " + classMap + ", this should not "
                        + "happen, probably the framework could not handle mapping correctly based on your bean");
            }
            B b = targetHolder.getB();
            mapper.map(sourceObject, b);
            return b;
        } catch (InterruptedException e) {
            throw new MappingException("Memoizer has been interrupted", e);
        } catch (MappingException e) {
            throw e;
        } catch (Exception e) {
            throw new MappingException(e);
        }
    }

    /**
     * mapping自己的引用
     *
     * @return Mapper
     */
    private Mapper self() {
        return this;
    }

    /**
     * 目标对象holder
     *
     * @param <B>
     */
    class TargetHolder<B> {

        /**
         * 目标对象
         */
        B b;

        /**
         * 目标类型
         */
        Class<B> targetClass;

        /**
         * 构造方法
         *
         * @param b           目标对象
         * @param targetClass 目标类型
         */
        public TargetHolder(B b, Class<B> targetClass) {
            this.b = b;
            this.targetClass = targetClass;
        }

        /**
         * 获取目标对象，如果为空则用反射新建一个
         *
         * @return 目标对象
         */
        public B getB() {
            if (b == null) {
                return ReflectionUtil.newInstance(targetClass);
            }
            return b;
        }

        public Class<B> getTargetClass() {
            return targetClass;
        }
    }
}
