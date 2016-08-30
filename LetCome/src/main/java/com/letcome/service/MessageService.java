package com.letcome.service;

import com.letcome.dao.MessageDAO;
import com.letcome.entity.CategoryEntity;
import com.letcome.entity.MessageEntity;
import com.letcome.entity.ReturnEntity;
import com.letcome.entity.UserEntity;
import com.letcome.vo.MessageVO;
import com.letcome.vo.UserVO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by rjt on 16/8/30.
 */
@Transactional
public class MessageService {
    private MessageDAO messageDao;

    public MessageDAO getMessageDao() {
        return messageDao;
    }

    public void setMessageDao(MessageDAO messageDao) {
        this.messageDao = messageDao;
    }

    public ReturnEntity addMessage(Integer fromid,Integer toid,String content){
        MessageVO vo = new MessageVO();
        vo.setContent(content);
        vo.setFromid(fromid);
        vo.setToid(toid);
        return messageDao.insertMessage(vo);
    }

    public MessageEntity getMessage(Integer fromid,Integer toid){
        MessageVO vo = new MessageVO();
        vo.setFromid(fromid);
        vo.setToid(toid);
        List<MessageVO> list = messageDao.selectMessageByUID(vo);
        MessageEntity retList = new MessageEntity();
        retList.setRecords(list);
        return retList;
    }

    public UserEntity getUser(Integer uid){
        List<UserVO> list = messageDao.selectUserByUID(uid);
        UserEntity retList = new UserEntity();
        retList.setRecords(list);
        return retList;
    }
}
