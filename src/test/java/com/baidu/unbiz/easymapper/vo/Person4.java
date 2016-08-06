package com.baidu.unbiz.easymapper.vo;

import java.util.List;

import com.google.common.base.MoreObjects;
import com.google.common.collect.Lists;

/**
 * @author zhangxu
 */
public class Person4<T> {

    public String firstName;
    public String lastName;
    public List<String> jobTitles;
    public long salary;
    public Family<T> family;

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("firstName", firstName)
                .add("lastName", lastName)
                .add("jobTitles", jobTitles)
                .add("salary", salary)
                .add("family", family)
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

    public Family<T> getFamily() {
        return family;
    }

    public void setFamily(Family<T> family) {
        this.family = family;
    }
}
