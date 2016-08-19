package com.letcome.dao;

import com.letcome.vo.CategoryVO;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;

/**
 * Created by rjt on 16/8/19.
 */
public class CategoryDAO extends BaseDAO{
    public List<CategoryVO> getCategories(){
        Session session = sessionFactory.getCurrentSession();
        String hql = "from categories ";
        Query query = session.createQuery(hql);
        return query.list();
    }
}
