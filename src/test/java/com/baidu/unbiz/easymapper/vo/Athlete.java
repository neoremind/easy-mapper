package com.baidu.unbiz.easymapper.vo;

import com.google.common.base.MoreObjects;

/**
 * @author zhangxu
 */
public class Athlete extends Person implements Pb<String> {

    private String nation;

    @Override
    public String getPb() {
        return "best!";
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("firstName", firstName)
                .add("lastName", lastName)
                .add("jobTitles", jobTitles)
                .add("salary", salary)
                .add("nation", nation)
                .toString();
    }

    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }
}
