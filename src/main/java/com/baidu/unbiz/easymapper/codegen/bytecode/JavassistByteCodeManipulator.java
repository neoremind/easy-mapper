package com.baidu.unbiz.easymapper.codegen.bytecode;

import static com.baidu.unbiz.easymapper.util.SystemPropertyUtil.ENABLE_WRITE_CLASS_FILE;
import static com.baidu.unbiz.easymapper.util.SystemPropertyUtil.ENABLE_WRITE_SOURCE_FILE;
import static com.baidu.unbiz.easymapper.util.SystemPropertyUtil.WRITE_CLASS_FILE_ABSOLUTE_PATH;
import static com.baidu.unbiz.easymapper.util.SystemPropertyUtil.WRITE_SOURCE_FILE_ABSOLUTE_PATH;
import static com.baidu.unbiz.easymapper.util.SystemPropertyUtil.getSystemProperty;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baidu.unbiz.easymapper.codegen.AtoBMapping;
import com.baidu.unbiz.easymapper.codegen.SourceCodeContext;
import com.baidu.unbiz.easymapper.exception.MappingCodeGenerationException;

import javassist.CannotCompileException;
import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtNewMethod;
import javassist.NotFoundException;

/**
 * 使用Javassist动态运行时生成{@link AtoBMapping}接口具体实现的字节码操作类
 *
 * @author zhangxu
 */
public class JavassistByteCodeManipulator implements ByteCodeManipulator {

    private static Logger LOGGER = LoggerFactory.getLogger(JavassistByteCodeManipulator.class);

    /**
     * Javassist的类池
     */
    private ClassPool classPool;

    /**
     * 是否启用输出源文件到本地文件系统
     */
    protected final boolean enableWriteSourceFile;

    /**
     * 是否启用输出编译后的class文件到本地文件系统
     */
    protected final boolean enableWriteClassFile;

    /**
     * 如果启用{@link #enableWriteSourceFile}，那么输出的文件路径
     *
     * @see com.baidu.unbiz.easymapper.util.SystemPropertyUtil#WRITE_SOURCE_FILE_ABSOLUTE_PATH
     */
    protected final String pathToWriteSourceFile;

    /**
     * 如果启用{@link #enableWriteClassFile}，那么输出的文件路径
     *
     * @see com.baidu.unbiz.easymapper.util.SystemPropertyUtil#WRITE_CLASS_FILE_ABSOLUTE_PATH
     */
    protected final String pathToWriteClassFile;

    /**
     * 默认的输出路径
     */
    protected static final String CLASS_PATH = "classpath:/";

    /**
     * 构造方法
     */
    public JavassistByteCodeManipulator() {
        this.enableWriteSourceFile = Boolean.valueOf(getSystemProperty(ENABLE_WRITE_SOURCE_FILE, "false"));
        this.enableWriteClassFile = Boolean.valueOf(getSystemProperty(ENABLE_WRITE_CLASS_FILE, "false"));
        this.pathToWriteSourceFile = getSystemProperty(WRITE_SOURCE_FILE_ABSOLUTE_PATH, CLASS_PATH);
        this.pathToWriteClassFile = getSystemProperty(WRITE_CLASS_FILE_ABSOLUTE_PATH, CLASS_PATH);
        this.classPool = new ClassPool();
        this.classPool.appendSystemPath();
        this.classPool.insertClassPath(new ClassClassPath(this.getClass()));
        LOGGER.debug(
                "Initialize {} done, enableWriteSourceFile={}, enableWriteClassFile={}, pathToWriteSourceFile={}, "
                        + "pathToWriteClassFile={}", this.getClass().getSimpleName(), enableWriteSourceFile,
                enableWriteClassFile, pathToWriteSourceFile, pathToWriteClassFile);
    }

    /**
     * 根据SourceCodeContext来编译字节码生成Class
     *
     * @param sourceCode 源代码上下文
     *
     * @return 编译后的class
     */
    public Class<?> compileClass(SourceCodeContext sourceCode) throws MappingCodeGenerationException {
        StringBuilder className = new StringBuilder(sourceCode.getClassName());
        CtClass byteCodeClass = classPool.makeClass(className.toString());

        Class<?> compiledClass;
        try {
            writeSourceFile(sourceCode);
            CtClass abstractMapperClass = classPool.get(sourceCode.getSuperClass().getCanonicalName());
            byteCodeClass.setSuperclass(abstractMapperClass);

            for (String fieldDef : sourceCode.getFields()) {
                try {
                    byteCodeClass.addField(CtField.make(fieldDef, byteCodeClass));
                } catch (CannotCompileException e) {
                    LOGGER.error(
                            "An exception occurred while compiling: " + fieldDef + " for " + sourceCode.getClassName(),
                            e);
                    throw e;
                }
            }

            for (String methodDef : sourceCode.getMethods()) {
                try {
                    byteCodeClass.addMethod(CtNewMethod.make(methodDef, byteCodeClass));
                } catch (CannotCompileException e) {
                    LOGGER.error(
                            "An exception occurred while compiling the following method:\n\n " + methodDef + "\n\n for "
                                    + sourceCode.getClassName() + "\n", e);
                    throw e;
                }
            }

            compiledClass = byteCodeClass.toClass(Thread.currentThread().getContextClassLoader(),
                    this.getClass().getProtectionDomain());

            writeClassFile(byteCodeClass);
        } catch (CannotCompileException e) {
            throw new MappingCodeGenerationException(
                    String.format("Failed to compile class %s, reason is %s", className, e.getMessage()), e);
        } catch (NotFoundException e) {
            throw new MappingCodeGenerationException(
                    String.format("Failed to compile class %s, reason is %s", className, e.getMessage()), e);
        }
        return compiledClass;
    }

    /**
     * 在字节码生成过程中写编译后的class文件到文件系统
     *
     * @throws IOException
     */
    protected void writeClassFile(CtClass byteCodeClass) {
        if (byteCodeClass == null) {
            return;
        }
        if (enableWriteClassFile) {
            try {
                File parentDir = preparePackageOutputPath(this.pathToWriteClassFile, "");
                byteCodeClass.writeFile(parentDir.getAbsolutePath());
            } catch (CannotCompileException e) {
                throw new IllegalArgumentException(e);
            } catch (IOException e) {
                LOGGER.error("Failed to write class file " + e.getMessage(), e);
            }
        }
    }

    /**
     * 在字节码生成过程中写源文件到文件系统
     *
     * @throws IOException
     */
    protected void writeSourceFile(SourceCodeContext sourceCode) {
        if (sourceCode == null) {
            return;
        }
        if (enableWriteSourceFile) {
            FileWriter fw = null;
            try {
                File parentDir = preparePackageOutputPath(this.pathToWriteSourceFile, sourceCode.getPackageName());
                File sourceFile = new File(parentDir, sourceCode.getClassSimpleName() + ".java");
                fw = new FileWriter(sourceFile);
                fw.append(sourceCode.toSourceFile());
            } catch (IOException e) {
                LOGGER.error("Failed to write source file " + e.getMessage(), e);
            } finally {
                if (fw != null) {
                    try {
                        fw.close();
                    } catch (IOException e) {
                        LOGGER.error("Failed to close source file " + e.getMessage(), e);
                    }
                }
            }
        }
    }

    /**
     * 准备要输出文件的目录
     *
     * @param basePath    父目录
     * @param packageName 要输出文件的包名
     *
     * @return 文件地址
     *
     * @throws IOException
     */
    protected File preparePackageOutputPath(String basePath, String packageName) throws IOException {
        String packagePath = packageName.replaceAll("\\.", "/");
        String path;
        if (basePath.startsWith(CLASS_PATH)) {
            path = getClass().getResource(basePath.substring(CLASS_PATH.length()))
                    .getFile().toString();
        } else {
            path = basePath;
            if (!path.endsWith("/")) {
                path += "/";
            }
        }
        File parentDir = new File(path + packagePath);
        if (!parentDir.exists() && !parentDir.mkdirs()) {
            throw new IOException("Could not create package directory for " + packageName);
        }
        return parentDir;
    }

}
