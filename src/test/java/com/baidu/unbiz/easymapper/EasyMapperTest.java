package com.baidu.unbiz.easymapper;

import com.baidu.unbiz.easymapper.codegen.AtoBMapping;
import com.baidu.unbiz.easymapper.exception.MappingException;
import com.baidu.unbiz.easymapper.thrift.PojoQueryRequest;
import com.baidu.unbiz.easymapper.thrift.QueryRequest;
import com.baidu.unbiz.easymapper.transformer.Transformer;
import com.baidu.unbiz.easymapper.vo.*;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

/**
 * Easy-mapper test suite
 *
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
        System.out.println(dto.myType);
        assertThat(dto.firstName, Matchers.is(p.firstName));
        assertThat(dto.lastName, Matchers.is(p.lastName));
        assertThat(dto.jobTitles, Matchers.is(p.jobTitles));
        assertThat(dto.salary, Matchers.is(p.salary));
    }

    @Test
    public void testBidirection() throws Exception {
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

        Person p2 =
                MapperFactory.getCopyByRefMapper().mapClass(PersonDto.class, Person.class).register()
                        .map(dto, Person.class);
        System.out.println(p2);
        assertThat(p2.firstName, Matchers.is(p.firstName));
        assertThat(p2.lastName, Matchers.is(p.lastName));
        assertThat(p2.jobTitles, Matchers.is(p.jobTitles));
        assertThat(p2.salary, Matchers.is(p.salary));
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
            assertThat(dto.address.getStreet(), Matchers.is(p.address.getStreet()));
            assertThat(dto.address.getNo(), Matchers.is(p.address.getNo() + ""));
        } catch (MappingException e) {
            LOGGER.error(e.getMessage(), e);
            return;
        }
        fail();
    }

    @Test
    public void testCanBeAssigned() throws Exception {
        PersonDto5_1 p = new PersonDto5_1();
        p.firstName = "neo";
        p.lastName = "jason";
        p.jobTitles = Lists.newArrayList("1", "2", "3");
        p.salary = 1000L;
        p.address = new SubAddress("beverly", 100);
        Person5 dto =
                MapperFactory.getCopyByRefMapper().mapClass(PersonDto5_1.class, Person5.class).register()
                        .map(p, Person5.class);
        System.out.println(dto);
        assertThat(dto.address.getStreet(), Matchers.is(p.address.getStreet()));
        assertThat(dto.address.getNo(), Matchers.is(p.address.getNo()));
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
    public void testExplicitFieldMapping() throws Exception {
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
    public void testExcludeFieldMapping() throws Exception {
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

    /**
     * [ERROR]	2017-12-05 20:19:37,964	[main]	easymapper.codegen.MappingCodeGenerator	(MappingCodeGenerator
     * .java:79)	-Generating mapping code with error: No appropriate mapping strategy found for FieldMap[entityList
     * (List<Entity1>)-->entityList(List<Entity2>)]
     * com.baidu.unbiz.easymapper.exception.MappingCodeGenerationException: No appropriate mapping strategy found for
     * FieldMap[entityList(List<Entity1>)-->entityList(List<Entity2>)]
     * <p>
     * 目前不支持
     */
    @Test(expected = MappingException.class)
    public void testCustomMappingAndTransformerWrapCollectionType() throws Exception {
        MapperFactory.getCopyByRefMapper().mapClass(Entity1.class, Entity2.class)
                .register();
        MapperFactory.getCopyByRefMapper().mapClass(Entity1Wrapper.class, Entity2Wrapper.class)
                .register();

        Person10 p = new Person10();
        p.firstName = "neo";
        p.lastName = "Jason2";
        p.jobTitles = Lists.newArrayList("1", "2", "3");
        p.salary = 1000L;
        p.entity = new Entity1Wrapper(Lists.newArrayList(Entity1.of("xu")));

        PersonDto10 dto = new PersonDto10();
        MapperFactory.getCopyByRefMapper().mapClass(Person10.class, PersonDto10.class)
                .register()
                .map(p, dto);
        System.out.println(dto);
        assertThat(dto.firstName, Matchers.is(p.firstName));
        assertThat(dto.lastName, Matchers.is(p.lastName.toUpperCase()));
        assertThat(dto.jobTitles.size(), Matchers.is(4));
        assertThat(dto.salary, Matchers.is(p.salary));
    }

    @Test
    public void testNameNotMatchTransformer() throws Exception {
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
    public void testNameNotMatchTransformerNegative() throws Exception {
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

    @Test
    public void testFinalStatic() throws Exception {
        Person8 p = new Person8();
        Person8.lastName = "Jason";
        p.jobTitles = Lists.newArrayList("1", "2", "3");
        PersonDto8 dto =
                MapperFactory.getCopyByRefMapper().mapClass(Person8.class, PersonDto8.class)
                        .registerAndMap(p, PersonDto8.class);
        System.out.println(dto);
        assertThat(dto.firstName, Matchers.is("hello"));
        assertThat(dto.lastName, Matchers.is(p.lastName));
        assertThat(dto.jobTitles, Matchers.is(p.jobTitles));
        assertThat(dto.salary, Matchers.is(200L));
    }

    @Test
    public void testEnum() throws Exception {
        Person9 p = new Person9();
        p.firstName = "neo";
        p.lastName = "jason";
        p.jobTitles = Lists.newArrayList("1", "2", "3");
        p.salary = 1000L;
        p.myType = MyEnumType.Mon;
        PersonDto9 dto =
                MapperFactory.getCopyByRefMapper().mapClass(Person9.class, PersonDto9.class).register()
                        .map(p, PersonDto9.class);
        System.out.println(dto);
        System.out.println(dto.myType);
        assertThat(dto.firstName, Matchers.is(p.firstName));
        assertThat(dto.lastName, Matchers.is(p.lastName));
        assertThat(dto.jobTitles, Matchers.is(p.jobTitles));
        assertThat(dto.salary, Matchers.is(p.salary));
        assertThat(dto.myType, Matchers.is(MyEnumType.Mon));
    }

    @Test
    public void testThrift2Pojo() {
        QueryRequest req = new QueryRequest();
        req.setId(123);
        req.setName("haha");
        req.setQuiet(false);
        req.setOptList(Lists.newArrayList("jack", "neo"));
        PojoQueryRequest pojoQueryRequest =
                MapperFactory.getCopyByRefMapper().mapClass(QueryRequest.class, PojoQueryRequest.class).register()
                        .map(req, PojoQueryRequest.class);
        assertThat(pojoQueryRequest.getId(), Matchers.is(req.getId()));
        assertThat(pojoQueryRequest.getName(), Matchers.is(req.getName()));
        assertThat(pojoQueryRequest.isQuiet(), Matchers.is(req.isQuiet()));
        assertThat(pojoQueryRequest.getOptList(), Matchers.is(req.getOptList()));
    }

    @Test
    public void testPojo2Thrift() {
        PojoQueryRequest pojoQueryRequest = new PojoQueryRequest();
        pojoQueryRequest.setId(123);
        pojoQueryRequest.setName("haha");
        pojoQueryRequest.setQuiet(false);
        pojoQueryRequest.setOptList(Lists.newArrayList("jack", "neo"));
        QueryRequest queryRequest =
                MapperFactory.getCopyByRefMapper().mapClass(PojoQueryRequest.class, QueryRequest.class).register()
                        .map(pojoQueryRequest, QueryRequest.class);
        assertThat(queryRequest.getId(), Matchers.is(pojoQueryRequest.getId()));
        assertThat(queryRequest.getName(), Matchers.is(pojoQueryRequest.getName()));
        assertThat(queryRequest.isQuiet(), Matchers.is(pojoQueryRequest.isQuiet()));
        assertThat(queryRequest.getOptList(), Matchers.is(pojoQueryRequest.getOptList()));
    }

    @Test
    public void testSourceToDest() {
        Source s = getSource();

        Dest d = MapperFactory.getCopyByRefMapper().mapClass(Source.class, Dest.class).register()
                .map(s, Dest.class);

        assertThat(d.getV().getV1(), Matchers.is(s.getV().getV1()));
        assertThat(d.getV().getV2().getV1(), Matchers.is(s.getV().getV2().getV1()));
        assertThat(d.getV().getV2().getV2(), Matchers.is(s.getV().getV2().getV2()));
    }

    @Test
    public void testSource1ToDest1WithMapperPredefined() {
        Source s = getSource();

        MapperFactory.getCopyByRefMapper().mapClass(InnerSource.class, InnerDest.class).customMapping((a, b) -> {
            b.setV1("hello");
            b.setV2(null);
        }).register();

        Dest dest1 = MapperFactory.getCopyByRefMapper().mapClass(Source.class, Dest.class).register()
                .map(s, Dest.class);

        assertThat(dest1.getV().getV1(), Matchers.not(s.getV().getV1()));
        assertThat(dest1.getV().getV2(), Matchers.not(s.getV().getV2()));
        assertThat(dest1.getV().getV1(), Matchers.is("hello"));
        assertThat(dest1.getV().getV2(), Matchers.nullValue());
    }

    private Source getSource() {
        InnerInnerSource iis = new InnerInnerSource();
        iis.setV1("v1");
        iis.setV2("v2");

        InnerSource is = new InnerSource();
        is.setV1("v1");
        is.setV2(iis);

        Source s = new Source();
        s.setV(is);
        return s;
    }

    @BeforeClass
    public static void init() {
//        System.setProperty(SystemPropertyUtil.ENABLE_WRITE_SOURCE_FILE, "true");
//        System.setProperty(SystemPropertyUtil.ENABLE_WRITE_CLASS_FILE, "true");
//        System.setProperty(SystemPropertyUtil.WRITE_SOURCE_FILE_ABSOLUTE_PATH, "/Users/baidu/work/easymapper");
//        System.setProperty(SystemPropertyUtil.WRITE_CLASS_FILE_ABSOLUTE_PATH, "/Users/baidu/work/easymapper");
    }

    @After
    public void clear() {
        MapperFactory.getCopyByRefMapper().clear();
    }

}
