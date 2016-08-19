package com.letcome.handler;

/**
 * Created by rjt on 16/8/11.
 */
import com.letcome.entity.ReturnEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;




@ControllerAdvice
public class ExceptionHandler{

    @org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
    @ResponseBody
    public ReturnEntity processUnauthenticatedException(Exception e) {
        e.printStackTrace();
        ReturnEntity ret = new ReturnEntity();
        ret.setResult(ReturnEntity.RETURN_FAILED);
        ret.setError_msg(e.getMessage());
        return ret; //返回一个ReturnEntity
    }

}