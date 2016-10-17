package com.letcome.dao;
import java.util.List;

import com.letcome.entity.ReturnEntity;
import com.letcome.vo.UserVO;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.loader.custom.Return;

/**
 * Created by rjt on 16/8/10.
 */
public class UsersDAO  extends BaseDAO{

    public UserVO getUser(UserVO vo){
        Session session = sessionFactory.getCurrentSession();
        String hql = "from users where email = :email";
        Query query = session.createQuery(hql);
        query.setProperties(vo);
        List<UserVO> list = query.list();
        if (list!=null && list.size()>0){
            return list.get(0);
        }else{
            return null;
        }
    }

    public UserVO getUserByOpenId(String openid){
        Session session = sessionFactory.getCurrentSession();
        String hql = "from users where openid = ?";
        Query query = session.createQuery(hql);
        query.setString(0,openid);
        List<UserVO> list = query.list();
        if (list!=null && list.size()>0){
            return list.get(0);
        }else{
            return null;
        }
    }


    public ReturnEntity insertUser(UserVO user){
//        try {
        Session session = sessionFactory.getCurrentSession();
        session.save(user);
        ReturnEntity ret = new ReturnEntity();
        ret.setRetVal(user.getId());
        return ret;
//        }catch (Exception e){
//            ReturnEntity entity = new ReturnEntity();
//            entity.setResult(ReturnEntity.RETURN_FAILED);
//            entity.setError_msg(e.getMessage());
//            return entity;
//        }

    }

    public ReturnEntity deleteUser(UserVO user){
//        try {
            Session session = sessionFactory.getCurrentSession();
            session.delete(user);
            return (new ReturnEntity());
//        }catch (Exception e){
//            ReturnEntity entity = new ReturnEntity();
//            entity.setResult(ReturnEntity.RETURN_FAILED);
//            entity.setError_msg(e.getMessage());
//            return entity;
//        }
    }
}
