package com.letcome.controller;

import com.letcome.entity.LoginEntity;
import com.letcome.entity.MessageEntity;
import com.letcome.entity.ReturnEntity;
import com.letcome.entity.UserEntity;
import com.letcome.service.MessageService;
import com.letcome.vo.MessageVO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * Created by rjt on 16/8/30.
 */

@Controller
@RequestMapping("/message")
public class MessageController {
    @Resource(name="messageService")
    private MessageService service;

    @ResponseBody
    @RequestMapping(value="/add", method = RequestMethod.POST)
    public ReturnEntity addMessage(@RequestHeader("let_come_uid") String  uid,@RequestBody MessageVO vo){
        ReturnEntity retEntity = service.addMessage(Integer.valueOf(uid),vo.getToid(),vo.getContent());
        return retEntity;
    }


    @ResponseBody
    @RequestMapping(value="/get", method = RequestMethod.GET)
    public MessageEntity findMessage(@RequestHeader("let_come_uid") String  uid,@RequestParam("toid") String toid){
        MessageEntity retEntity = service.getMessage(Integer.valueOf(uid), Integer.valueOf(toid));
        return retEntity;
    }

    @ResponseBody
    @RequestMapping(value="/user", method = RequestMethod.GET)
    public UserEntity findUser(@RequestHeader("let_come_uid") String  uid){
        UserEntity retEntity = service.getUser(Integer.valueOf(uid));
        return retEntity;
    }
}
