package com.baidu.unbiz.easymapper.vo;

import java.util.List;

import com.google.common.base.MoreObjects;

/**
 * @author zhangxu
 */
public class PersonDto2 {

    public String firstName;
    public String lastName;
    public List<String> jobTitles;
    public Long salary;
    public Integer height;
    public Double d;
    public short s;

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("firstName", firstName)
                .add("lastName", lastName)
                .add("jobTitles", jobTitles)
                .add("salary", salary)
                .add("height", height)
                .add("d", d)
                .add("s", s)
                .toString();
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public List<String> getJobTitles() {
        return jobTitles;
    }

    public void setJobTitles(List<String> jobTitles) {
        this.jobTitles = jobTitles;
    }

    public long getSalary() {
        return salary;
    }

    public void setSalary(long salary) {
        this.salary = salary;
    }

    public void setSalary(Long salary) {
        this.salary = salary;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Double getD() {
        return d;
    }

    public void setD(Double d) {
        this.d = d;
    }

    public short getS() {
        return s;
    }

    public void setS(short s) {
        this.s = s;
    }
}
