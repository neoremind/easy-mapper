package com.baidu.unbiz.easymapper;

import java.util.List;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import com.baidu.unbiz.easymapper.exception.MappingException;
import com.baidu.unbiz.easymapper.pojo.Address;
import com.baidu.unbiz.easymapper.pojo.Address2;
import com.baidu.unbiz.easymapper.pojo.Person;
import com.baidu.unbiz.easymapper.pojo.PersonDto;
import com.baidu.unbiz.easymapper.util.SystemPropertyUtil;
import com.google.common.collect.Lists;

/**
 * JDK8 style testing
 *
 * @author zhangxu
 */
public class EasyMapperSample {

    @BeforeClass
    public static void prepare() {
        System.setProperty(SystemPropertyUtil.ENABLE_WRITE_SOURCE_FILE, "true");
        System.setProperty(SystemPropertyUtil.ENABLE_WRITE_CLASS_FILE, "true");
        //System.setProperty(SystemPropertyUtil.WRITE_SOURCE_FILE_ABSOLUTE_PATH, "/Users/baidu/work/easymapper");
        //System.setProperty(SystemPropertyUtil.WRITE_CLASS_FILE_ABSOLUTE_PATH, "/Users/baidu/work/easymapper");
    }

    @Test
    public void testSimple() throws Exception {
        MapperFactory.getCopyByRefMapper().mapClass(Address.class, Address2.class).register();
        Person p = new Person();
        p.setFirstName("NEO");
        p.setLastName("jason");
        p.setJobTitles(Lists.newArrayList("abc", "dfegg", "iii"));
        p.setSalary(1000L);
        p.setAddress(new Address("beverly hill", 10086));
        PersonDto dto =
                MapperFactory.getCopyByRefMapper().mapClass(Person.class, PersonDto.class).register()
                        .map(p, PersonDto.class);
        System.out.println(dto);
    }

    @Test
    public void testRegisterAndMap() throws Exception {
        MapperFactory.getCopyByRefMapper().mapClass(Address.class, Address2.class).register();
        Person p = new Person();
        p.setFirstName("NEO");
        p.setLastName("jason");
        p.setJobTitles(Lists.newArrayList("abc", "dfegg", "iii"));
        p.setSalary(1000L);
        p.setAddress(new Address("beverly hill", 10086));
        PersonDto dto =
                MapperFactory.getCopyByRefMapper().mapClass(Person.class, PersonDto.class).registerAndMap(p, PersonDto
                        .class);
        System.out.println(dto);
    }

    @Test
    public void testNewObj() throws Exception {
        MapperFactory.getCopyByRefMapper().mapClass(Address.class, Address2.class).register();
        Person p = new Person();
        p.setFirstName("NEO");
        p.setLastName("jason");
        p.setJobTitles(Lists.newArrayList("abc", "dfegg", "iii"));
        p.setSalary(1000L);
        p.setAddress(new Address("beverly hill", 10086));
        PersonDto dto = new PersonDto();
        MapperFactory.getCopyByRefMapper().mapClass(Person.class, PersonDto.class).registerAndMap(p, dto);
        System.out.println(dto);
    }

    @Test
    public void testField() throws Exception {
        MapperFactory.getCopyByRefMapper().mapClass(Address.class, Address2.class).register();
        Person p = new Person();
        p.setFirstName("NEO");
        p.setLastName("jason");
        p.setJobTitles(Lists.newArrayList("abc", "dfegg", "iii"));
        p.setSalary(1000L);
        p.setAddress(new Address("beverly hill", 10086));
        PersonDto dto =
                MapperFactory.getCopyByRefMapper().mapClass(Person.class, PersonDto.class)
                        .field("salary", "salary")
                        .register()
                        .map(p, PersonDto.class);
        System.out.println(dto);
    }

    @Test
    public void testFieldTransformer() throws Exception {
        MapperFactory.getCopyByRefMapper().mapClass(Address.class, Address2.class).register();
        Person p = new Person();
        p.setFirstName("NEO");
        p.setLastName("jason");
        p.setJobTitles(Lists.newArrayList("abc", "dfegg", "iii"));
        p.setSalary(1000L);
        p.setAddress(new Address("beverly hill", 10086));
        PersonDto dto =
                MapperFactory.getCopyByRefMapper().mapClass(Person.class, PersonDto.class)
                        .field("firstName", "firstName", String.class, String.class, s -> s.toLowerCase())
                        .field("salary", "salary")
                        .register()
                        .map(p, PersonDto.class);
        System.out.println(dto);
    }

    @Test
    public void testFieldTransformer2() throws Exception {
        MapperFactory.getCopyByRefMapper().mapClass(Address.class, Address2.class).register();
        Person p = new Person();
        p.setFirstName("NEO");
        p.setLastName("jason");
        p.setJobTitles(Lists.newArrayList("abc", "dfegg", "iii"));
        p.setSalary(1000L);
        p.setAddress(new Address("beverly hill", 10086));
        PersonDto dto =
                MapperFactory.getCopyByRefMapper().mapClass(Person.class, PersonDto.class)
                        .field("firstName", "firstName", (String s) -> s.toLowerCase())
                        .field("salary", "salary")
                        .register()
                        .map(p, PersonDto.class);
        System.out.println(dto);
    }

    @Test
    public void testFieldStream() throws Exception {
        MapperFactory.getCopyByRefMapper().mapClass(Address.class, Address2.class).register();
        Person p = new Person();
        p.setFirstName("NEO");
        p.setLastName("jason");
        p.setJobTitles(Lists.newArrayList("abc", "dfegg", "iii"));
        p.setSalary(1000L);
        p.setAddress(new Address("beverly hill", 10086));
        PersonDto dto =
                MapperFactory.getCopyByRefMapper().mapClass(Person.class, PersonDto.class)
                        .field("jobTitles", "jobTitleLetterCounts",
                                (List<String> s) -> s.stream().map(String::length).toArray(Integer[]::new))
                        .register()
                        .map(p, PersonDto.class);
        System.out.println(dto);
    }

    @Test
    public void testExclude() throws Exception {
        MapperFactory.getCopyByRefMapper().mapClass(Address.class, Address2.class).register();
        Person p = new Person();
        p.setFirstName("NEO");
        p.setLastName("jason");
        p.setJobTitles(Lists.newArrayList("abc", "dfegg", "iii"));
        p.setSalary(1000L);
        p.setAddress(new Address("beverly hill", 10086));
        PersonDto dto =
                MapperFactory.getCopyByRefMapper().mapClass(Person.class, PersonDto.class)
                        .exclude("lastName")
                        .register()
                        .map(p, PersonDto.class);
        System.out.println(dto);
    }

    @Test
    public void testMapOnNull() throws Exception {
        MapperFactory.getCopyByRefMapper().mapClass(Address.class, Address2.class).register();
        Person p = new Person();
        p.setFirstName("NEO");
        p.setJobTitles(Lists.newArrayList("abc", "dfegg", "iii"));
        p.setSalary(1000L);
        p.setAddress(new Address("beverly hill", 10086));
        PersonDto dto =
                MapperFactory.getCopyByRefMapper().mapClass(Person.class, PersonDto.class)
                        .mapOnNull(true)
                        .register()
                        .map(p, PersonDto.class);
        System.out.println(dto);
    }

    @Test
    public void testCustomMapping() throws Exception {
        MapperFactory.getCopyByRefMapper().mapClass(Address.class, Address2.class).register();
        Person p = new Person();
        p.setFirstName("NEO");
        p.setLastName("jason");
        p.setJobTitles(Lists.newArrayList("abc", "dfegg", "iii"));
        p.setSalary(1000L);
        p.setAddress(new Address("beverly hill", 10086));
        PersonDto dto =
                MapperFactory.getCopyByRefMapper().mapClass(Person.class, PersonDto.class)
                        .customMapping((s, d) -> d.setLastName(s.getLastName().toUpperCase()))
                        .register()
                        .map(p, PersonDto.class);
        System.out.println(dto);
    }

    @Test(expected = MappingException.class)
    public void testNoAddressRegistered() throws Exception {
        Person p = new Person();
        p.setFirstName("NEO");
        p.setLastName("jason");
        p.setJobTitles(Lists.newArrayList("abc", "dfegg", "iii"));
        p.setSalary(1000L);
        p.setAddress(new Address("beverly hill", 10086));
        PersonDto dto =
                MapperFactory.getCopyByRefMapper().mapClass(Person.class, PersonDto.class)
                        .customMapping((s, d) -> d.setLastName(s.getLastName().toUpperCase()))
                        .register()
                        .map(p, PersonDto.class);
        System.out.println(dto);
    }

    @After
    public void clear() {
        MapperFactory.getCopyByRefMapper().clear();
    }

}
