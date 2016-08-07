package com.baidu.unbiz.easymapper;

import java.util.Collection;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Test;

import com.baidu.unbiz.easymapper.pojo.Address;
import com.baidu.unbiz.easymapper.pojo.Address2;
import com.baidu.unbiz.easymapper.pojo.Person;
import com.baidu.unbiz.easymapper.pojo.PersonDto;
import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;

import fj.F;
import fj.data.TreeMap;

/**
 * @author zhangxu
 */
public class OtherFrameworkIntegration {

    @Test
    public void testGuavaCollections2() throws Exception {
        MapperFactory.getCopyByRefMapper().mapClass(Address.class, Address2.class).register();
        MapperFactory.getCopyByRefMapper().mapClass(Person.class, PersonDto.class).register();
        List<Person> personList = getPersonList();
        Collection<PersonDto> personDtoList = Collections2.transform(personList,
                p -> MapperFactory.getCopyByRefMapper().map(p, PersonDto.class));
        System.out.println(personDtoList);
    }

    @Test
    public void testFj() throws Exception {
        MapperFactory.getCopyByRefMapper().mapClass(Address.class, Address2.class).register();
        MapperFactory.getCopyByRefMapper().mapClass(Person.class, PersonDto.class).register();
        List<Person> personList = getPersonList();
        fj.data.List<PersonDto> personDtoList = fj.data.List.fromIterator(personList.iterator()).map(
                person -> MapperFactory.getCopyByRefMapper().map(person, PersonDto.class));
        personDtoList.forEach(e -> System.out.println(e));
    }

    public List<Person> getPersonList() {
        List<Person> res = Lists.newArrayList();
        for (int i = 0; i < 10; i++) {
            Person p = new Person();
            p.setFirstName("first" + i);
            p.setLastName("lastname" + i);
            p.setJobTitles(Lists.newArrayList("Professor", "Teacher"));
            p.setSalary(100000L);
            res.add(p);
        }
        return res;
    }

}
