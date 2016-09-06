package com.letcome.dao;

import com.letcome.entity.ReturnEntity;
import com.letcome.vo.MessageVO;
import com.letcome.vo.UserVO;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;

import java.util.Date;
import java.util.List;

/**
 * Created by rjt on 16/8/30.
 */
public class MessageDAO  extends BaseDAO{
    public ReturnEntity insertMessage(MessageVO vo){
        Session session = sessionFactory.getCurrentSession();
        vo.setCreated_at(new Date());
        session.save(vo);
        ReturnEntity ret = new ReturnEntity();
        ret.setRetVal(vo.getId());
        return ret;
    }

    public List<MessageVO> selectMessageByUID(MessageVO vo){
        Session session = sessionFactory.getCurrentSession();
        String hql = "select m.id,m.fromid,m.toid,m.content,m.created_at,fu.fullname as fromname,tu.fullname as toname  from messages m  " +
                "left join users fu on fu.id = m.fromid " +
                "left join users tu on tu.id = m.toid " +
                "where fromid = :fromid and toid = :toid or fromid = :toid and toid = :fromid order by id desc";
        Query query = session.createSQLQuery(hql)
                .setParameter("fromid",vo.getFromid())
                .setParameter("toid",vo.getToid())
                .setResultTransformer(Transformers.aliasToBean(MessageVO.class));
        List<MessageVO> l = query.list();
        return l;
    }

    public List<UserVO> selectUserByUID(Integer uid){
        Session session = sessionFactory.getCurrentSession();
        String hql = "SELECT u.id,u.email,u.fullname,t.mid,t.fromid,t.toid,t.content,t.created_at from users u " +
                " INNER JOIN " +
                " ( " +
                " select max(id) mid,fromid,toid,content,created_at,(fromid*toid +fromid+toid) as hashid from " +
                " messages m " +
                " WHERE fromid = :uid or toid = :uid " +
                " GROUP BY hashid " +
                " ORDER BY id desc " +
                " ) t " +
                " on t.fromid = u.id or t.toid = u.id " +
                " where u.id != :uid " ;
        Query query = session.createSQLQuery(hql)
                .setParameter("uid",uid)
                .setResultTransformer(Transformers.aliasToBean(UserVO.class));
        List<UserVO> l = query.list();
        return l;
    }
}
