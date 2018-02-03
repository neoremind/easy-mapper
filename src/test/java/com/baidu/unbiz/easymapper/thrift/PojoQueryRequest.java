package com.baidu.unbiz.easymapper.thrift;

import java.util.List;

/**
 * POJO of {@link QueryRequest}
 *
 * @author xu.zhang
 */
public class PojoQueryRequest {

    private long id;
    private String name;
    private List<String> optList;
    private boolean quiet;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getOptList() {
        return optList;
    }

    public void setOptList(List<String> optList) {
        this.optList = optList;
    }

    public boolean isQuiet() {
        return quiet;
    }

    public void setQuiet(boolean quiet) {
        this.quiet = quiet;
    }
}
