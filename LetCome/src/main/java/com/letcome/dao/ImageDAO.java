package com.letcome.dao;

import com.letcome.entity.ReturnEntity;
import com.letcome.vo.ImageVO;
import com.letcome.vo.UserVO;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.io.InputStream;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by rjt on 16/8/12.
 */
public class ImageDAO extends BaseDAO{

    public ReturnEntity insertImage(ImageVO image){
//        try {
        Session session = sessionFactory.getCurrentSession();
        session.save(image);
        ReturnEntity ret = new ReturnEntity();
        ret.setRetVal(image.getId());
        return ret;
//        }catch (Exception e){
//            ReturnEntity entity = new ReturnEntity();
//            entity.setResult(ReturnEntity.RETURN_FAILED);
//            entity.setError_msg(e.getMessage());
//            return entity;
//        }

    }

    public ImageVO getImage(ImageVO vo) {
        Session session = sessionFactory.getCurrentSession();
        String hql = "from images where id = :id";
        Query query = session.createQuery(hql);
        query.setProperties(vo);
        List<ImageVO> list = query.list();
        if (list != null && list.size() > 0) {
            return list.get(0);
        } else {
            return null;
        }
    }

    public List<ImageVO> getImages(long start,long end) {
        Session session = sessionFactory.getCurrentSession();
        String hql = "from images";
        Query query = session.createQuery(hql);
        return query.list();
    }
}
