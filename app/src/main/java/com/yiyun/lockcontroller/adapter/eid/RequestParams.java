package com.yiyun.lockcontroller.adapter.eid;

import java.io.Serializable;

public class RequestParams implements Serializable {

    public String envTag = "";
    public String url = "";

    public RequestParams(String envTag, String url) {

        this.envTag = envTag;
        this.url = url;

    }

}
