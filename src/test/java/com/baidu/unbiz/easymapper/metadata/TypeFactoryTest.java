package com.baidu.unbiz.easymapper.metadata;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

import com.baidu.unbiz.easymapper.pojo.AbstractChild;
import com.baidu.unbiz.easymapper.pojo.Keyable;
import com.baidu.unbiz.easymapper.pojo.ParentInterface;

import com.baidu.unbiz.easymapper.pojo.Child;

/**
 * @author zhangxu
 */
public class TypeFactoryTest {

    @Test
    public void testChild() {
        ParentInterface child = new Child();
        Type<?> type = TypeFactory.valueOf(child.getClass());
        Assert.assertEquals("com.baidu.unbiz.easymapper.pojo.Child", type.getCanonicalName());
        Assert.assertEquals(Child.class, type.getRawType());
        Assert.assertEquals(AbstractChild.class, type.getSuperType().getRawType());
        Assert.assertEquals(false, type.isParameterized());
        Assert.assertEquals(true, type.getSuperType().isParameterized());
        Assert.assertEquals(true, type.isSelfOrAncestorParameterized());
        Assert.assertEquals(TypeFactory.valueOf(AbstractChild.class), type.getSuperType());
        Assert.assertEquals(TypeFactory.valueOf(Keyable.class).getRawType(), type.getInterfaces()[0].getRawType());
        Assert.assertEquals(false, TypeFactory.valueOf(Keyable.class).isAssignableFrom(type));
        Assert.assertEquals(true, TypeFactory.valueOf(AbstractChild.class).isAssignableFrom(type));
    }

    @Test
    public void createTypeFromClass() {
        Type<?> type = TypeFactory.valueOf("java.util.List");
        Assert.assertEquals(List.class, type.getRawType());
    }

    @Test
    public void createTypeFromClass_defaultPackages() {
        Type<?> type = TypeFactory.valueOf("List");
        Assert.assertEquals(List.class, type.getRawType());
        type = TypeFactory.valueOf("String");
        Assert.assertEquals(String.class, type.getRawType());
    }

    @Test
    public void createTypeFromNestedClass() {
        Type<?> type = TypeFactory.valueOf("List<Long>");
        Assert.assertEquals(List.class, type.getRawType());
        Assert.assertEquals(Long.class, type.getNestedType(0).getRawType());
    }

    @Test
    public void createTypeFromMultipleNestedClass() {
        Type<?> type = TypeFactory.valueOf("List<Map<String,Set<Map<String,java.io.File>>>>");
        Assert.assertEquals(List.class, type.getRawType());
        Assert.assertEquals(Map.class, type.getNestedType(0).getRawType());
        Assert.assertEquals(String.class, type.getNestedType(0).getNestedType(0).getRawType());
        Assert.assertEquals(Set.class, type.getNestedType(0).getNestedType(1).getRawType());
        Assert.assertEquals(Map.class, type.getNestedType(0).getNestedType(1).getNestedType(0).getRawType());
        Assert.assertEquals(String.class,
                type.getNestedType(0).getNestedType(1).getNestedType(0).getNestedType(0).getRawType());
        Assert.assertEquals(File.class,
                type.getNestedType(0).getNestedType(1).getNestedType(0).getNestedType(1).getRawType());

    }

    @Test(expected = IllegalArgumentException.class)
    public void createTypeFromMultipleNestedClass_invalidExpression() {
        TypeFactory.valueOf("List<Map<String,Set<Map<String,java.io.File>>>");
    }

    @Test(expected = IllegalArgumentException.class)
    public void createTypeFromMultipleNestedClass_invalidType() {
        TypeFactory.valueOf("List<Map<String,Set<Map<String,java.io.FooBar>>>>");
    }
}
