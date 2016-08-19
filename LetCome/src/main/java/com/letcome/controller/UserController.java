package com.letcome.controller;

import com.letcome.entity.LoginEntity;
import com.letcome.entity.ReturnEntity;
import com.letcome.service.UserService;
import com.letcome.vo.UserVO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;

/**
 * Created by rjt on 16/8/10.
 */
@Controller
@RequestMapping("/user")
public class UserController {

    @Resource(name="userService")
    private UserService service;

//    @ResponseBody
//    @RequestMapping(value="/register",method = RequestMethod.POST)
//    public LoginEntity register(
//            @RequestParam("email") String email,
//            @RequestParam("pwd") String pwd,
//            @RequestParam("fullname") String fullname){
//        LoginEntity retEntity = service.addUser(email, pwd, fullname);
//        return retEntity;
//    }

//    @ResponseBody
//    @RequestMapping(value="/login",method = RequestMethod.POST)
//    public LoginEntity register(
//            @RequestParam("email") String email,
//            @RequestParam("pwd") String pwd){
//        LoginEntity retEntity = service.login(email, pwd);
//        return retEntity;
//    }

    @ResponseBody
    @RequestMapping(value="/register",method = RequestMethod.POST)
    public LoginEntity register(@RequestBody UserVO user){

        LoginEntity retEntity = service.addUser(user.getEmail(), user.getPwd(), user.getFullname());
        return retEntity;
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public LoginEntity saveUser(@RequestBody UserVO user) {
        System.out.println(user);
        LoginEntity retEntity = service.login(user.getEmail(),user.getPwd());
        return retEntity;
    }

    @ResponseBody
    @RequestMapping(value="/remove",method = RequestMethod.POST)
    public ReturnEntity remove(
            @RequestParam("uid") String uid){
        ReturnEntity retEntity = service.removeUser(uid);
        return retEntity;
    }

}
