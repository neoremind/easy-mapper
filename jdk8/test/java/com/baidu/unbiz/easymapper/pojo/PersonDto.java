package com.baidu.unbiz.easymapper.pojo;

import java.util.List;

import com.google.common.base.MoreObjects;

/**
 * @author zhangxu
 */
public class PersonDto {

    public String firstName;
    public String lastName;
    private List<String> jobTitles;
    private Integer[] jobTitleLetterCounts;
    public long salary;
    private Address2 address;

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("firstName", firstName)
                .add("lastName", lastName)
                .add("jobTitles", jobTitles)
                .add("jobTitleLetterCounts", jobTitleLetterCounts)
                .add("salary", salary)
                .add("address", address)
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

    public Address2 getAddress() {
        return address;
    }

    public void setAddress(Address2 address) {
        this.address = address;
    }

    public Integer[] getJobTitleLetterCounts() {
        return jobTitleLetterCounts;
    }

    public void setJobTitleLetterCounts(Integer[] jobTitleLetterCounts) {
        this.jobTitleLetterCounts = jobTitleLetterCounts;
    }
}
