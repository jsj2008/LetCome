package com.letcome.service;

import com.letcome.dao.UsersDAO;
import com.letcome.entity.LoginEntity;
import com.letcome.entity.ReturnEntity;
import com.letcome.util.EncryptUtils;
import com.letcome.vo.UserVO;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * Created by rjt on 16/8/10.
 */
@Transactional
public class UserService {
    private UsersDAO userDao;

    static private String DES_KEY = "www.letcome.com";


    public UsersDAO getUserDao() {
        return userDao;
    }

    public void setUserDao(UsersDAO userDao) {
        this.userDao = userDao;
    }

    public LoginEntity addUser(String email,String pwd,String fullname){
        UserVO user = new UserVO();
        user.setEmail(email);
        user.setFullname(fullname);
        user.setPwd(pwd);
        ReturnEntity ret = userDao.insertUser(user);
        LoginEntity entity = new LoginEntity();
        entity.setUid(user.getId().toString());
        entity.setFullname(user.getFullname());
        entity.setSessionid(encodeSessionid(user.getId().toString()));
        return entity;
    }

    public LoginEntity login(String email,String pwd){
        UserVO user = new UserVO();
        user.setEmail(email);
        UserVO ret = userDao.getUser(user);
        LoginEntity entity = new LoginEntity();
        if(ret!=null && ret.getEmail().equals(email) && ret.getPwd().equals(pwd)){
            entity.setUid(ret.getId().toString());
            entity.setFullname(ret.getFullname());
            entity.setSessionid(encodeSessionid(ret.getId().toString()));
        }else {
            entity.setResult(ReturnEntity.RETURN_FAILED);
            entity.setError_code("110006");
            entity.setError_msg("用户名或密码错误");
        }
        return entity;
    }

    public ReturnEntity removeUser(String id){
        UserVO user = new UserVO();
        user.setId(Integer.valueOf(id));
        ReturnEntity retEntity = userDao.deleteUser(user);
        return retEntity;
    }

    static public String encodeSessionid(String uid){
        Date date = new Date();
        String ret = "";
        try {
            ret = EncryptUtils.Encrypt3DES(uid+";"+date.getTime(),DES_KEY);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    static public boolean isSessionidValid(String uid,String sessionid){
        boolean ret = false;
        try {
            String plainText = EncryptUtils.Decrypt3DES(sessionid,DES_KEY);
            String[] strs = plainText.split(";");
            if (uid.equals(strs[0])){
                ret = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;

    }

    public static void main(String[] args) {
        String sessionid = UserService.encodeSessionid("666");
        System.out.println("sessionid = "+sessionid);
        System.out.println("777 is valid "+UserService.isSessionidValid("777",sessionid));
        System.out.println("666 is valid "+UserService.isSessionidValid("666",sessionid));

    }

}