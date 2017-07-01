# Easy-mapper
[![Build Status](https://travis-ci.org/neoremind/easy-mapper.svg?branch=master)](https://travis-ci.org/neoremind/easy-mapper)
[![Coverage Status](https://coveralls.io/repos/github/neoremind/easy-mapper/badge.svg?branch=master)](https://coveralls.io/github/neoremind/easy-mapper?branch=master)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.baidu.unbiz/easy-mapper/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.baidu.unbiz/easy-mapper)
[![Hex.pm](https://img.shields.io/hexpm/l/plug.svg)](http://www.apache.org/licenses/LICENSE-2.0)

Easy-mapper is a simple, light-weighted, high-performance java bean mapping framework. By leveraging Javassist, easy mapper can generate mapping byte-code at runtime and load them into JVM for later invocations to reduce some of the overhead.

Easy-mapper not only provides a relatively high-performance mapping solution, but also enables the caller to do mapping in a more flexible and extensible way. Fluent interface style API and Java8 lambda API, these modern techniques can be fully used to customize your own mapping strategy.

Here, easy-mapper uses by-reference field mapping strategy most of the time except for some immutable types like primitive, wrapper, String and BigDecimal, etc. When you don’t need to copy and clone field, by-reference mapping is capable to process your business logic and avoid the overhead of performance loss. On the other hand, easy-mapper respects immutability and do not mean to offense, just provide an alternative solution.

For performance test result, please refer to the benchmark section.

中文手册[请点这里](http://neoremind.com/2016/08/easy-mapper-%E4%B8%80%E4%B8%AA%E7%81%B5%E6%B4%BB%E5%8F%AF%E6%89%A9%E5%B1%95%E7%9A%84%E9%AB%98%E6%80%A7%E8%83%BDbean-mapping%E7%B1%BB%E5%BA%93/)。

##1. Easy-mapper in a nutshell
###1.1 Where to get easy mapper
Maven:
```
<dependency>
    <groupId>com.baidu.unbiz</groupId>
    <artifactId>easy-mapper</artifactId>
    <version>1.0.3</version>
</dependency>
```
Gradle:
```
compile 'com.baidu.unbiz:easy-mapper:1.0.3'
```

###1.2 Develop Java bean
POJO:
```
public class Person {
    private String firstName;
    private String lastName;
    private List<String> jobTitles;
    private long salary;
    // getter and setter...
}
```
DTO（Data Transfer Object）
```
public class PersonDto {
    private String firstName;
    private String lastName;
    private List<String> jobTitles;
    private long salary;
    // getter and setter...
}
```
###1.3 Start mapping
From POJO to DTO:
```
Person p = new Person();
p.setFirstName("NEO");
p.setLastName("jason");
p.setJobTitles(Lists.newArrayList("abc", "dfegg", "iii"));
p.setSalary(1000L);
PersonDto dto = MapperFactory.getCopyByRefMapper()
                .mapClass(Person.class, PersonDto.class)
                .registerAndMap(p, PersonDto.class);
System.out.println(dto);
```

##2. Dig into easy-mapper
###2.1 Register and map
There are two separate steps to do mapping and you can combine them.
```
PersonDto dto = MapperFactory.getCopyByRefMapper().mapClass(Person.class, PersonDto.class)
                .register()
                .map(p, PersonDto.class);
```

```
PersonDto dto = MapperFactory.getCopyByRefMapper().mapClass(Person.class, PersonDto.class)
                .register()
Mapper mapper = MapperFactory.getCopyByRefMapper();
PersonDto dto = mapper.map(p, PersonDto.class);
```

```
PersonDto dto = MapperFactory.getCopyByRefMapper().mapClass(Person.class, PersonDto.class)
                .register()
Mapper mapper = MapperFactory.getCopyByRefMapper().map(p, PersonDto.class);
```

###2.2 Specify field name
```
PersonDto dto = MapperFactory.getCopyByRefMapper().mapClass(Person.class, PersonDto.class)
                .field("salary", "salary")
                .register()
                .map(p, PersonDto.class);
```

###2.3 Ignore fields from source object
```
PersonDto dto = MapperFactory.getCopyByRefMapper().mapClass(Person.class, PersonDto.class)
                .exclude("lastName")
                .register()
                .map(p, PersonDto.class);
```

###2.4 Customize filed mapping
```
PersonDto6 dto = new PersonDto6();
MapperFactory.getCopyByRefMapper().mapClass(Person6.class, PersonDto6.class)
        .field("jobTitles", "jobTitles", new Transformer<List<String>, List<Integer>>() {
            @Override
            public List<Integer> transform(List<String> source) {
                return Lists.newArrayList(1, 2, 3, 4);
            }
        })
        .register()
        .map(p, dto);
```

Java8 lambda:
```
PersonDto dto = MapperFactory.getCopyByRefMapper().mapClass(Person.class, PersonDto.class)
                    .field("firstName", "firstName", (String s) -> s.toLowerCase())
                    .register()
                    .map(p, PersonDto.class);
```

Java8 streaming:
```
PersonDto dto = MapperFactory.getCopyByRefMapper().mapClass(Person.class, PersonDto.class)
                    .field("jobTitles", "jobTitleLetterCounts",
                            (List<String> s) -> s.stream().map(String::length).toArray(Integer[]::new))
                    .register()
                    .map(p, PersonDto.class);
```

Type inference:
```
MapperFactory.getCopyByRefMapper().mapClass(Person.class, PersonDto.class)
                    .field("firstName", "firstName", String.class, String.class, s -> s.toLowerCase())
                    .register()
                    .map(p, PersonDto.class);
```

###2.5 Customize object mapping
```
PersonDto6 dto = new PersonDto6();
MapperFactory.getCopyByRefMapper().mapClass(Person6.class, PersonDto6.class)
        .customMapping((a, b) -> b.setLastName(a.getLastName().toUpperCase()))
        .register()
        .map(p, dto);
```

###2.6 New object then mapping
```
PersonDto dto = new PersonDto();
MapperFactory.getCopyByRefMapper().mapClass(Person.class, PersonDto.class).registerAndMap(p, dto);
```

###2.7 Map on null
```
PersonDto dto = MapperFactory.getCopyByRefMapper().mapClass(Person.class, PersonDto.class)
                    .mapOnNull(true)
                    .register()
                    .map(p, PersonDto.class);
```

###2.8 Cascade mapping
Easy-mapper can map recursively. If `Person` has-a `Address`, `Address` mapping should be done beforehand.
```
MapperFactory.getCopyByRefMapper().mapClass(Address.class, Address2.class).register();
Person p = getPerson(); 
p.setAddress(new Address("beverly hill", 10086));
PersonDto dto = MapperFactory.getCopyByRefMapper()
                    .mapClass(Person.class, PersonDto.class)
                    .register()
                    .map(p, PersonDto.class);
```
Otherwise there will result a exception like below:
```
com.baidu.unbiz.easymapper.exception.MappingException: No class map found for (Address, Address2), make sure type or nested type is registered beforehand
```

###2.9 Output generated source code for debugging
Specify the following args:
```
-Dcom.baidu.unbiz.easymapper.enableWriteSourceFile=true 
-Dcom.baidu.unbiz.easymapper.writeSourceFileAbsolutePath="..."
-Dcom.baidu.unbiz.easymapper.enableWriteClassFile=true 
-Dcom.baidu.unbiz.easymapper.writeClassFileAbsolutePath="..."
```

##3. Mapping rules
Rules prioritizes as below:

1.	Custom transformer goes with highest priority.

2.	Field type is the same, copy by reference. For primitive and wrapper type, equal operator is used for assigning.

3.	If target field is String but source is not, assign target with source.toString().

4.	If target field type can be assigned from source field type, then copy by reference.

5.	Any other cases, use object graph or so called cascade mapping to map.

At last, if none of the mapping handlers work, there will end up with the following exception:
```
com.baidu.unbiz.easymapper.exception.MappingCodeGenerationException: No appropriate mapping strategy found for FieldMap[jobTitles(List<string>)-->jobTitles(List<integer>)]
...
com.baidu.unbiz.easymapper.exception.MappingException: Generating mapping code failed for ClassMap([A]:Person6, [B]:PersonDto6), this should not happen, probably the framework could not handle mapping correctly based on your bean.
```

##4. Dependencies
```
+- org.slf4j:slf4j-api:jar:1.7.7:compile
+- org.slf4j:slf4j-log4j12:jar:1.7.7:compile
|  \- log4j:log4j:jar:1.2.17:compile
+- org.javassist:javassist:jar:3.18.1-GA:compile
```

##5. Benchmark
Based on Oracal Hotspot JVM:
```
java version "1.8.0_51"
Java(TM) SE Runtime Environment (build 1.8.0_51-b16)
Java HotSpot(TM) 64-Bit Server VM (build 25.51-b03, mixed mode)
```
JVM args:
```
-Xmx512m -Xms512m -XX:MetaspaceSize=256m
```
Hardware configuration:
```
CPU: Intel(R) Core(TM) i5-4278U CPU @ 2.60GHz
MEM: 8G
```

Please refer to [testing source code](https://github.com/neoremind/easy-mapper/blob/master/src/test/java/com/baidu/unbiz/easymapper/performance/BenchmarkTest.java).
```
-------------------------------------
| Create object number:   10000      |
-------------------------------------
|     Framework     |    time cost   |
-------------------------------------
|      Pure get/set |      11ms      |
|       Easy mapper |      44ms      |
|  Cglib beancopier |       7ms      |
|         BeanUtils |     248ms      |
|     PropertyUtils |     129ms      |
|  Spring BeanUtils |      95ms      |
|             Dozer |     772ms      |
-------------------------------------
-------------------------------------
| Create object number:  100000      |
-------------------------------------
|     Framework     |    time cost   |
-------------------------------------
|      Pure get/set |      56ms      |
|       Easy mapper |     165ms      |
|  Cglib beancopier |      30ms      |
|         BeanUtils |     921ms      |
|     PropertyUtils |     358ms      |
|  Spring BeanUtils |     152ms      |
|             Dozer |    1224ms      |
-------------------------------------
-------------------------------------
| Create object number: 1000000      |
-------------------------------------
|     Framework     |    time cost   |
-------------------------------------
|      Pure get/set |     189ms      |
|       Easy mapper |     554ms      |
|  Cglib beancopier |      48ms      |
|         BeanUtils |    4210ms      |
|     PropertyUtils |    4386ms      |
|  Spring BeanUtils |     367ms      |
|             Dozer |    6319ms      |
-------------------------------------
```

Conclusion:

Easy-mapper is way faster than traditional framework like Apache BeanUtils, PropertyUtils and dozer but cannot beat Cglib Beancopier which manipulating byte code using ASM directly. 

For Spring BeanUtils, when invocation number exceeds certain threshold, Spring BeanUtils is faster than Easy-mapper. That is because Spring BeanUtils is so simple that it just execute Method.invoke(..), and this reflection work can be improved by JIT compiler at runtime and does not invoke native method. 

By thinking of the benefits that easy-mapper brings to you, this tradeoff can be accepted.

##6. Working together with High-order function
###6.1 With guava
```
MapperFactory.getCopyByRefMapper().mapClass(Address.class, Address2.class).register();
MapperFactory.getCopyByRefMapper().mapClass(Person.class, PersonDto.class).register();
List<Person> personList = getPersonList();
Collection<PersonDto> personDtoList = Collections2.transform(personList,
        p -> MapperFactory.getCopyByRefMapper().map(p, PersonDto.class));
System.out.println(personDtoList);
```

###6.2 With functional java
```
MapperFactory.getCopyByRefMapper().mapClass(Address.class, Address2.class).register();
MapperFactory.getCopyByRefMapper().mapClass(Person.class, PersonDto.class).register();
List<Person> personList = getPersonList();
fj.data.List<PersonDto> personDtoList = fj.data.List.fromIterator(personList.iterator()).map(
        person -> MapperFactory.getCopyByRefMapper().map(person, PersonDto.class));
personDtoList.forEach(e -> System.out.println(e));
```

###6.3 With Java8 stream
```
MapperFactory.getCopyByRefMapper().mapClass(Address.class, Address2.class).register();
MapperFactory.getCopyByRefMapper().mapClass(Person.class, PersonDto.class).register();
List<Person> personList = getPersonList();
List<PersonDto> personDtoList = personList.stream().map(p -> MapperFactory.getCopyByRefMapper().map(p,
        PersonDto.class)).collect(Collectors.toList());
```


###6.4 With Scala
```
object EasyMapperTest {
 
  def main(args: Array[String]) {
    MapperFactory.getCopyByRefMapper.mapClass(classOf[Person], classOf[PersonDto]).register
    val personList = List(
      new Person("neo1", 100),
      new Person("neo2", 200),
      new Person("neo3", 300)
    )
    val personDtoList = personList.map(p => MapperFactory.getCopyByRefMapper.map(p, classOf[PersonDto]))
    personDtoList.foreach(println)
  }
}
```

##7. Acknowledgment
The development of easy-mapper is inspired by Orika. Easy-mapper with Apache2.0 Open Source License retains all copyright, trademark, author’s information from Orika.