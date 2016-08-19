package com.letcome.entity;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Created by rjt on 16/8/11.
 */
public class LoginEntity extends ReturnEntity{
    @JsonInclude(JsonInclude.Include.NON_NULL) private String uid;

    @JsonInclude(JsonInclude.Include.NON_NULL) private String fullname;

    @JsonInclude(JsonInclude.Include.NON_NULL) private String sessionid;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getSessionid() {
        return sessionid;
    }

    public void setSessionid(String sessionid) {
        this.sessionid = sessionid;
    }
}
