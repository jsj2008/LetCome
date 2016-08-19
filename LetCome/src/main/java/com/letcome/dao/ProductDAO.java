package com.letcome.dao;

import com.letcome.entity.ReturnEntity;
import com.letcome.vo.ProductVO;
import org.hibernate.Session;

/**
 * Created by rjt on 16/8/19.
 */
public class ProductDAO extends BaseDAO{
    public ReturnEntity insertProduct(ProductVO product){
        Session session = sessionFactory.getCurrentSession();
        session.save(product);
        ReturnEntity ret = new ReturnEntity();
        ret.setRetVal(product.getId());
        return ret;
    }
}
