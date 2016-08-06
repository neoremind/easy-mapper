package com.baidu.unbiz.easymapper.vo;

import java.util.List;

import com.google.common.base.MoreObjects;

/**
 * @author zhangxu
 */
public class PersonDto7 {

    public String firstName7;
    public String lastName7;
    public List<String> jobTitles7;
    public long salary7;

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("firstName7", firstName7)
                .add("lastName7", lastName7)
                .add("jobTitles7", jobTitles7)
                .add("salary7", salary7)
                .toString();
    }

    public String getFirstName7() {
        return firstName7;
    }

    public void setFirstName7(String firstName7) {
        this.firstName7 = firstName7;
    }

    public String getLastName7() {
        return lastName7;
    }

    public void setLastName7(String lastName7) {
        this.lastName7 = lastName7;
    }

    public List<String> getJobTitles7() {
        return jobTitles7;
    }

    public void setJobTitles7(List<String> jobTitles7) {
        this.jobTitles7 = jobTitles7;
    }

    public long getSalary7() {
        return salary7;
    }

    public void setSalary7(long salary7) {
        this.salary7 = salary7;
    }
}
