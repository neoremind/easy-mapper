package com.baidu.unbiz.easymapper.performance;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.dozer.DozerBeanMapper;
import org.junit.Ignore;
import org.junit.Test;

import com.baidu.unbiz.easymapper.Mapper;
import com.baidu.unbiz.easymapper.MapperFactory;
import com.baidu.unbiz.easymapper.vo.Person7;
import com.baidu.unbiz.easymapper.vo.PersonDto;
import com.google.common.collect.Lists;

import net.sf.cglib.beans.BeanCopier;

/**
 * 建议测试基础参数，基于JVM 8版本。
 * <pre>
 *     -Xmx512m -Xms512m -XX:MetaspaceSize=256m
 * </pre>
 * 使用如下参数可以看是否被JIT优化：
 * <pre>
 *     -XX:+PrintCompilation
 * </pre>
 * 使用如下参数可以看编译后的机器码：
 * <pre>
 *     -XX:+UnlockDiagnosticVMOptions -XX:+PrintAssembly -XX:CompileCommand=print,*Test.testEasyMapper
 * </pre>
 *
 * @author zhangxu
 */
@Ignore
public class BenchmarkTest {

    @Test
    public void testOne() {
        testMapping(2);
    }

    @Test
    public void test10k() {
        testMapping(10000);
    }

    @Test
    public void test100k() {
        testMapping(10 * 10000);
    }

    @Test
    public void test1m() {
        testMapping(100 * 10000);
    }

    public void testMapping(int invokeNum) {
        List<Profiler> profilerList = Lists.newArrayList();
        testEasyMapper(1, "Easy mapper");
        testBeanCopier(1, "Cglib bean-copier");
        testBeanUtils(1, "BeanUtils");
        testPropertyUtils(1, "PropertyUtils");
        testSpringBeanUtils(1, "Spring BeanUtils");
        testDozer(1, "Dozer");

        profilerList.add(testGetSet(invokeNum, "Pure get/set"));
        //hang(1);
        profilerList.add(testEasyMapper(invokeNum, "Easy mapper"));
        //hang(1);
        profilerList.add(testBeanCopier(invokeNum, "Cglib beancopier"));
        //hang(1);
        profilerList.add(testBeanUtils(invokeNum, "BeanUtils"));
        //hang(1);
        profilerList.add(testPropertyUtils(invokeNum, "PropertyUtils"));
        //hang(1);
        profilerList.add(testSpringBeanUtils(invokeNum, "Spring BeanUtils"));
        //hang(1);
        profilerList.add(testDozer(invokeNum, "Dozer"));

        System.out.println("-------------------------------------");
        System.out.println(String.format("| Create object number:%8d      |", invokeNum));
        System.out.println("-------------------------------------");
        System.out.println("|     Framework     |    time cost   |");
        System.out.println("-------------------------------------");
        for (Profiler profiler : profilerList) {
            System.out.println(profiler);

        }
        System.out.println("-------------------------------------");
    }

    public Profiler testGetSet(int invokeNum, String frameworkName) {
        BeanCopier b = BeanCopier.create(Person7.class, PersonDto.class, false);
        long start = System.currentTimeMillis();
        for (int i = 0; i < invokeNum; i++) {
            Person7 p = getPerson();
            PersonDto dto = new PersonDto();
            dto.setFirstName(p.getFirstName());
            dto.setLastName(p.getLastName());
            dto.setJobTitles(p.getJobTitles());
            dto.setSalary(p.getSalary());
            //System.out.println(dto);
        }
        return Profiler.apply(System.currentTimeMillis(), start)
                .setFrameworkName(frameworkName);
    }

    public Profiler testBeanCopier(int invokeNum, String frameworkName) {
        BeanCopier b = BeanCopier.create(Person7.class, PersonDto.class, false);
        long start = System.currentTimeMillis();
        for (int i = 0; i < invokeNum; i++) {
            Person7 p = getPerson();
            PersonDto dto = new PersonDto();
            b.copy(p, dto, null);
            //System.out.println(dto);
        }
        return Profiler.apply(System.currentTimeMillis(), start)
                .setFrameworkName(frameworkName);
    }

    public Profiler testEasyMapper(int invokeNum, String frameworkName) {
        MapperFactory.getCopyByRefMapper().mapClass(Person7.class, PersonDto.class).register();
        Mapper mapper = MapperFactory.getCopyByRefMapper();
        long start = System.currentTimeMillis();
        for (int i = 0; i < invokeNum; i++) {
            Person7 p = getPerson();
            PersonDto dto = new PersonDto();
            mapper.map(p, dto);
            //System.out.println(dto);
        }
        return Profiler.apply(System.currentTimeMillis(), start)
                .setFrameworkName(frameworkName);
    }

    public Profiler testBeanUtils(int invokeNum, String frameworkName) {
        long start = System.currentTimeMillis();
        for (int i = 0; i < invokeNum; i++) {
            Person7 p = getPerson();
            PersonDto dto = new PersonDto();
            try {
                BeanUtils.copyProperties(p, dto);
                //System.out.println(dto);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return Profiler.apply(System.currentTimeMillis(), start)
                .setFrameworkName(frameworkName);
    }

    public Profiler testPropertyUtils(int invokeNum, String frameworkName) {
        long start = System.currentTimeMillis();
        for (int i = 0; i < invokeNum; i++) {
            Person7 p = getPerson();
            PersonDto dto = new PersonDto();
            try {
                PropertyUtils.copyProperties(p, dto);
                //System.out.println(dto);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        return Profiler.apply(System.currentTimeMillis(), start)
                .setFrameworkName(frameworkName);
    }

    public Profiler testSpringBeanUtils(int invokeNum, String frameworkName) {
        long start = System.currentTimeMillis();
        for (int i = 0; i < invokeNum; i++) {
            Person7 p = getPerson();
            PersonDto dto = new PersonDto();
            org.springframework.beans.BeanUtils.copyProperties(p, dto);
            //System.out.println(dto);
        }
        return Profiler.apply(System.currentTimeMillis(), start)
                .setFrameworkName(frameworkName);
    }

    public Profiler testDozer(int invokeNum, String frameworkName) {
        DozerBeanMapper mapper = new DozerBeanMapper();
        long start = System.currentTimeMillis();
        for (int i = 0; i < invokeNum; i++) {
            Person7 p = getPerson();
            PersonDto dto = mapper.map(p, PersonDto.class);
            //System.out.println(dto);
        }
        return Profiler.apply(System.currentTimeMillis(), start)
                .setFrameworkName(frameworkName);
    }

    private static class Profiler {

        private String frameworkName;

        private long timeCost;

        public Profiler(long timeCost) {
            this.timeCost = timeCost;
        }

        public static Profiler apply(long end, long start) {
            return new Profiler(end - start);
        }

        public Profiler setFrameworkName(String frameworkName) {
            this.frameworkName = frameworkName;
            return this;
        }

        public String getFrameworkName() {
            return frameworkName;
        }

        public long getTimeCost() {
            return timeCost;
        }

        @Override
        public String toString() {
            return String.format("| %17s |   %5dms      |", frameworkName, timeCost);
        }
    }

    private void hang(int sec) {
        try {
            //System.gc();
            Thread.sleep(sec * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public Person7 getPerson() {
        Person7 p = new Person7();
        p.setFirstName("neo");
        p.setLastName("jason");
        p.setJobTitles(Lists.newArrayList("1", "2", "3"));
        p.setSalary(1000L);
        return p;
    }
}
