package com.letcome.service;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.letcome.dao.UsersDAO;
import com.letcome.entity.LoginEntity;
import com.letcome.entity.ReturnEntity;
import com.letcome.util.EncryptUtils;
import com.letcome.vo.UserVO;
import net.sf.json.JSONObject;
import org.jsoup.Jsoup;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.Map;

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

    public LoginEntity addUser(String email,String pwd,String fullname,String qq,String openid){
        UserVO user = new UserVO();
        user.setEmail(email);
        user.setFullname(fullname);
        user.setPwd(pwd);
        user.setQq(qq);
        user.setOpenid(openid);
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

    public LoginEntity sso(String openid,String accesstoken){
        LoginEntity entity = new LoginEntity();
        try{
            String body = Jsoup.connect("https://graph.qq.com/user/get_user_info")
                            .data("oauth_consumer_key", "1105700540")
                            .data("access_token",accesstoken)
                            .data("openid",openid)
                            .data("form","json").ignoreContentType(true).execute().body();
            System.out.println("body:" + body);
            JSONObject json = JSONObject.fromObject(body);

            UserVO ret = userDao.getUserByOpenId(openid);
            if (ret!=null && json.getString("ret").equals("0")){
                entity.setUid(ret.getId().toString());
                entity.setFullname(ret.getFullname());
                entity.setSessionid(encodeSessionid(ret.getId().toString()));
            }else{
                entity = addUser(json.getString("nickname"),accesstoken,json.getString("nickname"),null,openid);
            }

        }catch (Exception e){
            e.printStackTrace();
                entity.setResult(ReturnEntity.RETURN_FAILED);
                entity.setError_code("110006");
                entity.setError_msg("用户名或密码错误");

        }
        return entity;

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