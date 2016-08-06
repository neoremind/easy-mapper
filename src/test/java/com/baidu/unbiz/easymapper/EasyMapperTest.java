package com.baidu.unbiz.easymapper;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.util.List;
import java.util.Map;

import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baidu.unbiz.easymapper.codegen.AtoBMapping;
import com.baidu.unbiz.easymapper.exception.MappingException;
import com.baidu.unbiz.easymapper.transformer.Transformer;
import com.baidu.unbiz.easymapper.vo.Address;
import com.baidu.unbiz.easymapper.vo.Address2;
import com.baidu.unbiz.easymapper.vo.Athlete;
import com.baidu.unbiz.easymapper.vo.Family;
import com.baidu.unbiz.easymapper.vo.Person;
import com.baidu.unbiz.easymapper.vo.Person2;
import com.baidu.unbiz.easymapper.vo.Person3;
import com.baidu.unbiz.easymapper.vo.Person4;
import com.baidu.unbiz.easymapper.vo.Person5;
import com.baidu.unbiz.easymapper.vo.Person6;
import com.baidu.unbiz.easymapper.vo.Person7;
import com.baidu.unbiz.easymapper.vo.PersonDto;
import com.baidu.unbiz.easymapper.vo.PersonDto2;
import com.baidu.unbiz.easymapper.vo.PersonDto3;
import com.baidu.unbiz.easymapper.vo.PersonDto4;
import com.baidu.unbiz.easymapper.vo.PersonDto5;
import com.baidu.unbiz.easymapper.vo.PersonDto6;
import com.baidu.unbiz.easymapper.vo.PersonDto7;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * @author zhangxu
 */
public class EasyMapperTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(EasyMapperTest.class);

    @Test
    public void testIdentity() throws Exception {
        Person p = new Person();
        p.firstName = "neo";
        p.lastName = "jason";
        p.jobTitles = Lists.newArrayList("1", "2", "3");
        p.salary = 1000L;
        PersonDto dto =
                MapperFactory.getCopyByRefMapper().mapClass(Person.class, PersonDto.class).register()
                        .map(p, PersonDto.class);
        System.out.println(dto);
        assertThat(dto.firstName, Matchers.is(p.firstName));
        assertThat(dto.lastName, Matchers.is(p.lastName));
        assertThat(dto.jobTitles, Matchers.is(p.jobTitles));
        assertThat(dto.salary, Matchers.is(p.salary));
    }

    @Test
    public void testPrimitiveAndWrapper() throws Exception {
        Person2 p = new Person2();
        p.firstName = "neo";
        p.lastName = "jason";
        p.jobTitles = Lists.newArrayList("1", "2", "3");
        p.salary = 1000L;
        p.height = 180;
        p.d = 3.14d;
        p.s = 64;
        PersonDto2 dto =
                MapperFactory.getCopyByRefMapper().mapClass(Person2.class, PersonDto2.class).register()
                        .map(p, PersonDto2.class);
        System.out.println(dto);
        assertThat(dto.firstName, Matchers.is(p.firstName));
        assertThat(dto.lastName, Matchers.is(p.lastName));
        assertThat(dto.jobTitles, Matchers.is(p.jobTitles));
        assertThat(dto.salary, Matchers.is(p.salary));
        assertThat(dto.s, Matchers.is(p.s));
        assertThat(dto.d, Matchers.is(p.d));
    }

    @Test
    public void testInherit() throws Exception {
        Person p = new Person();
        p.firstName = "neo";
        p.lastName = "jason";
        p.jobTitles = Lists.newArrayList("1", "2", "3");
        p.salary = 1000L;
        Athlete athlete =
                MapperFactory.getCopyByRefMapper().mapClass(Person.class, Athlete.class).register()
                        .map(p, Athlete.class);
        System.out.println(athlete);
        assertThat(athlete.firstName, Matchers.is(p.firstName));
        assertThat(athlete.lastName, Matchers.is(p.lastName));
        assertThat(athlete.jobTitles, Matchers.is(p.jobTitles));
        assertThat(athlete.salary, Matchers.is(p.salary));
    }

    @Test
    public void testTypeSameCopyRef() throws Exception {
        Person3 p = new Person3();
        p.firstName = "neo";
        p.lastName = "jason";
        p.jobTitles = Lists.newArrayList("1", "2", "3");
        p.salary = 1000L;
        p.family = new Family<String>() {
            @Override
            public List<String> getMembers() {
                return Lists.newArrayList("father", "mother");
            }
        };
        PersonDto3 dto =
                MapperFactory.getCopyByRefMapper().mapClass(Person3.class, PersonDto3.class).register()
                        .map(p, PersonDto3.class);
        System.out.println(dto);
        assertThat(dto.family == p.family, Matchers.is(true));
    }

    @Test
    public void testGenericType() throws Exception {
        Person4<String> p = new Person4<String>();
        p.firstName = "neo";
        p.lastName = "jason";
        p.jobTitles = Lists.newArrayList("1", "2", "3");
        p.salary = 1000L;
        p.family = new Family<String>() {
            @Override
            public List<String> getMembers() {
                return Lists.newArrayList("father", "mother");
            }
        };
        PersonDto4<String> dto =
                MapperFactory.getCopyByRefMapper().mapClass(Person4.class, PersonDto4.class).register()
                        .map(p, PersonDto4.class);
        System.out.println(dto);
        assertThat(dto.family == p.family, Matchers.is(true));
    }

    @Test
    public void testTypeNotMatchAndAnyTypeToString() throws Exception {
        MapperFactory.getCopyByRefMapper().mapClass(Address.class, Address2.class).register();
        Person5 p = new Person5();
        p.firstName = "neo";
        p.lastName = "jason";
        p.jobTitles = Lists.newArrayList("1", "2", "3");
        p.salary = 1000L;
        p.address = new Address("beverly", 100);
        PersonDto5 dto =
                MapperFactory.getCopyByRefMapper().mapClass(Person5.class, PersonDto5.class).register()
                        .map(p, PersonDto5.class);
        System.out.println(dto);
        assertThat(dto.address.getStreet(), Matchers.is(p.address.getStreet()));
        assertThat(dto.address.getNo(), Matchers.is(p.address.getNo() + ""));
    }

    @Test
    public void testTypeNotMatchNegative() throws Exception {
        try {
            Person5 p = new Person5();
            p.firstName = "neo";
            p.lastName = "jason";
            p.jobTitles = Lists.newArrayList("1", "2", "3");
            p.salary = 1000L;
            p.address = new Address("beverly", 100);
            PersonDto5 dto =
                    MapperFactory.getCopyByRefMapper().mapClass(Person5.class, PersonDto5.class).register()
                            .map(p, PersonDto5.class);
            System.out.println(dto);
        } catch (MappingException e) {
            LOGGER.error(e.getMessage(), e);
            return;
        }
        fail();
    }

    @Test
    public void testArgumentTypeNotMatch() throws Exception {
        try {
            MapperFactory.getCopyByRefMapper().mapClass(Address.class, Address2.class).register();
            Person6 p = new Person6();
            p.firstName = "neo";
            p.lastName = "jason";
            p.jobTitles = Lists.newArrayList("1", "2", "3");
            p.salary = 1000L;
            PersonDto6 dto =
                    MapperFactory.getCopyByRefMapper().mapClass(Person6.class, PersonDto6.class).register()
                            .map(p, PersonDto6.class);
            System.out.println(dto);
        } catch (MappingException e) {
            LOGGER.error(e.getMessage(), e);
            return;
        }
        fail();
    }

    @Test
    public void testField() throws Exception {
        Person7 p = new Person7();
        p.setFirstName("neo");
        p.setJobTitles(Lists.newArrayList("1", "2", "3"));
        p.setSalary(1000L);
        PersonDto7 dto =
                MapperFactory.getCopyByRefMapper().mapClass(Person7.class, PersonDto7.class)
                        .field("firstName", "firstName7")
                        .field("jobTitles", "jobTitles7")
                        .field("salary", "salary7")
                        .register()
                        .map(p, PersonDto7.class);
        System.out.println(dto);
        assertThat(dto.firstName7, Matchers.is(p.getFirstName()));
        assertThat(dto.lastName7, Matchers.nullValue());
        assertThat(dto.jobTitles7, Matchers.is(p.getJobTitles()));
        assertThat(dto.salary7, Matchers.is(p.getSalary()));
    }

    @Test
    public void testExclude() throws Exception {
        Person7 p = new Person7();
        p.setFirstName("neo");
        p.setLastName("Jason");
        p.setJobTitles(Lists.newArrayList("1", "2", "3"));
        p.setSalary(1000L);
        PersonDto7 dto =
                MapperFactory.getCopyByRefMapper().mapClass(Person7.class, PersonDto7.class)
                        .field("firstName", "firstName7")
                        .field("lastName", "lastName7")
                        .field("jobTitles", "jobTitles7")
                        .field("salary", "salary7")
                        .exclude("lastName")
                        .register()
                        .map(p, PersonDto7.class);
        System.out.println(dto);
        assertThat(dto.firstName7, Matchers.is(p.getFirstName()));
        assertThat(dto.lastName7, Matchers.nullValue());
        assertThat(dto.jobTitles7, Matchers.is(p.getJobTitles()));
        assertThat(dto.salary7, Matchers.is(p.getSalary()));
    }

    @Test
    public void testMapOnNullDefault() throws Exception {
        Person p = new Person();
        p.firstName = "neo";
        p.salary = 1000L;

        PersonDto dto = new PersonDto();
        dto.firstName = "neo2";
        dto.lastName = "Jason2";
        dto.jobTitles = Lists.newArrayList("1", "2", "3");
        dto.salary = 1000L;
        MapperFactory.getCopyByRefMapper().mapClass(Person.class, PersonDto.class).register()
                .map(p, dto);
        System.out.println(dto);
        assertThat(dto.firstName, Matchers.is(p.firstName));
        assertThat(dto.lastName, Matchers.notNullValue());
        assertThat(dto.jobTitles, Matchers.notNullValue());
        assertThat(dto.salary, Matchers.is(p.salary));
    }

    @Test
    public void testMapOnNullTrue() throws Exception {
        Person p = new Person();
        p.firstName = "neo";
        p.salary = 1000L;

        PersonDto dto = new PersonDto();
        dto.firstName = "neo2";
        dto.lastName = "Jason2";
        dto.jobTitles = Lists.newArrayList("1", "2", "3");
        dto.salary = 1000L;
        MapperFactory.getCopyByRefMapper().mapClass(Person.class, PersonDto.class)
                .mapOnNull(true)
                .register()
                .map(p, dto);
        System.out.println(dto);
        assertThat(dto.firstName, Matchers.is(p.firstName));
        assertThat(dto.lastName, Matchers.is(p.lastName));
        assertThat(dto.jobTitles, Matchers.is(p.jobTitles));
        assertThat(dto.salary, Matchers.is(p.salary));
    }

    @Test
    public void testCustomMappingAndTransformer() throws Exception {
        Person6 p = new Person6();
        p.firstName = "neo";
        p.lastName = "Jason2";
        p.jobTitles = Lists.newArrayList("1", "2", "3");
        p.salary = 1000L;

        PersonDto6 dto = new PersonDto6();
        MapperFactory.getCopyByRefMapper().mapClass(Person6.class, PersonDto6.class)
                .field("jobTitles", "jobTitles", new Transformer<List<String>, List<Integer>>() {
                    @Override
                    public List<Integer> transform(List<String> source) {
                        return Lists.newArrayList(1, 2, 3, 4);
                    }
                })
                .customMapping(new AtoBMapping<Person6, PersonDto6>() {
                    @Override
                    public void map(Person6 person, PersonDto6 personDto) {
                        personDto.setLastName(person.lastName.toUpperCase());
                    }
                })
                .register()
                .map(p, dto);
        System.out.println(dto);
        assertThat(dto.firstName, Matchers.is(p.firstName));
        assertThat(dto.lastName, Matchers.is(p.lastName.toUpperCase()));
        assertThat(dto.jobTitles.size(), Matchers.is(4));
        assertThat(dto.salary, Matchers.is(p.salary));
    }

    @Test
    public void testTransformer1() throws Exception {
        Person7 p = new Person7();
        p.setFirstName("neo");
        p.setLastName("Jason");
        p.setJobTitles(Lists.newArrayList("1", "2", "3"));
        p.setSalary(1000L);

        PersonDto7 dto = new PersonDto7();
        MapperFactory.getCopyByRefMapper().mapClass(Person7.class, PersonDto7.class)
                .field("jobTitles", "jobTitles7", List.class, List.class,
                        new Transformer<List, List>() {
                            @Override
                            public List transform(List source) {
                                return Lists.newArrayList(1, 2, 3, 4);
                            }
                        })
                .register()
                .map(p, dto);
        System.out.println(dto);
        assertThat(dto.getFirstName7(), Matchers.nullValue());
        assertThat(dto.getJobTitles7().size(), Matchers.is(4));
    }

    @Test
    public void testTransformer2() throws Exception {
        try {
            Person7 p = new Person7();
            p.setFirstName("neo");
            p.setLastName("Jason");
            p.setJobTitles(Lists.newArrayList("1", "2", "3"));
            p.setSalary(1000L);

            PersonDto7 dto = new PersonDto7();
            MapperFactory.getCopyByRefMapper().mapClass(Person7.class, PersonDto7.class)
                    .field("jobTitles", "jobTitles7", new Transformer<List, Map>() {
                        @Override
                        public Map transform(List source) {
                            return Maps.newHashMap();
                        }
                    })
                    .register()
                    .map(p, dto);
            System.out.println(dto);
            assertThat(dto.getFirstName7(), Matchers.nullValue());
            assertThat(dto.getJobTitles7().size(), Matchers.is(4));
        } catch (MappingException e) {
            LOGGER.error(e.getMessage(), e);
            return;
        }
        fail();
    }

    @Test
    public void testRegisterAndMap() throws Exception {
        Person p = new Person();
        p.firstName = "neo";
        p.lastName = "jason";
        p.jobTitles = Lists.newArrayList("1", "2", "3");
        p.salary = 1000L;
        PersonDto dto =
                MapperFactory.getCopyByRefMapper().mapClass(Person.class, PersonDto.class).registerAndMap(p, PersonDto
                        .class);
        System.out.println(dto);
        assertThat(dto.firstName, Matchers.is(p.firstName));
        assertThat(dto.lastName, Matchers.is(p.lastName));
        assertThat(dto.jobTitles, Matchers.is(p.jobTitles));
        assertThat(dto.salary, Matchers.is(p.salary));
    }

    @After
    public void clear() {
        MapperFactory.getCopyByRefMapper().clear();
    }

}
