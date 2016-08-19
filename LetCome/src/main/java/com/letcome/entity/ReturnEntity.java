package com.letcome.entity;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Created by rjt on 16/8/10.
 */


public class ReturnEntity {

    final public static String  RETURN_SUCCESS = "Y";
    final public static String  RETURN_FAILED = "N";

    public ReturnEntity(){
        result = RETURN_SUCCESS;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL) private String result;

    @JsonInclude(JsonInclude.Include.NON_NULL) private String error_code;

    @JsonInclude(JsonInclude.Include.NON_NULL) private String error_msg;

    @JsonInclude(JsonInclude.Include.NON_NULL) private Object retVal;

    public String getError_code() {
        return error_code;
    }

    public void setError_code(String error_code) {
        this.error_code = error_code;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }


    public String getError_msg() {
        return error_msg;
    }

    public void setError_msg(String error_msg) {
        this.error_msg = error_msg;
    }

    public Object getRetVal() {
        return retVal;
    }

    public void setRetVal(Object retVal) {
        this.retVal = retVal;
    }
}
