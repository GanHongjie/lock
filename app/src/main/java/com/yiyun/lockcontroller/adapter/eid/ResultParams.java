package com.yiyun.lockcontroller.adapter.eid;

import java.io.Serializable;


public class ResultParams implements Serializable {

    private static final long serialVersionUID = 7943978240383120670L;

    public String envTag = "";
    public boolean isOK = false;
    public String more = "";

    public ResultParams(String envTag) {
        this.envTag = envTag;
        isOK = false;
        more = "";

    }

    public ResultParams build(boolean isOK, String more) {
        this.isOK = isOK;
        this.more = more;
        return this;

    }

}
