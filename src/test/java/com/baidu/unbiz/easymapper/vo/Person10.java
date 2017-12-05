package com.baidu.unbiz.easymapper.vo;

import com.google.common.base.MoreObjects;

import java.util.List;

/**
 * @author zhangxu
 */
public class Person10 {

    public String firstName;
    public String lastName;
    public List<String> jobTitles;
    public long salary;
    public Entity1Wrapper entity;

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("firstName", firstName)
                .add("lastName", lastName)
                .add("jobTitles", jobTitles)
                .add("salary", salary)
                .add("entity", entity)
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

    public Entity1Wrapper getEntity() {
        return entity;
    }

    public void setEntity(Entity1Wrapper entity) {
        this.entity = entity;
    }
}
