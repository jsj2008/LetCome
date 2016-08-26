package com.letcome.dao;

import com.letcome.entity.ReturnEntity;
import com.letcome.vo.FavoriteVO;
import com.letcome.vo.ProductVO;
import com.letcome.vo.ProductViewVO;
import com.letcome.vo.UserVO;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * Created by rjt on 16/8/19.
 */
public class ProductDAO extends BaseDAO{

    public List<ProductViewVO > selectProductsAndImage(Integer uid,long start,long end){
        Session session = sessionFactory.getCurrentSession();
        String hql = "SELECT p.id,p.description,p.created_at,p.updated_at,p.longitude,p.latitude,p.city,p.status,p.price,p.category_id,p.uid,imagename,imagepath,image_id,contact_info,case when f.id>0 then \"Y\" else \"N\" end as is_favorite from products_v p\n" +
                "left join favorites f on f.uid = ? and p.id = f.pid";
        Query query = session.createSQLQuery(hql)
                        .setInteger(0,uid)
                        .setResultTransformer(Transformers.aliasToBean(ProductViewVO.class));


        List l = query.list();
//        Map m = (Map)l.get(0);
        System.out.println(l);
        return l;
    }

    public ReturnEntity insertProduct(ProductVO productVO){
        Session session = sessionFactory.getCurrentSession();
        productVO.setCreated_at(new Date());
        productVO.setUpdated_at(productVO.getCreated_at());
        session.save(productVO);
        ReturnEntity ret = new ReturnEntity();
        ret.setRetVal(productVO.getId());
        return ret;
    }

    public ReturnEntity insertFavorite(FavoriteVO favoriteVO){
        Session session = sessionFactory.getCurrentSession();
        favoriteVO.setCreated_at(new Date());
        session.save(favoriteVO);
        ReturnEntity ret = new ReturnEntity();
        ret.setRetVal(favoriteVO.getId());
        return ret;
    }

    public ReturnEntity updateProduct(ProductVO productVO){
        ReturnEntity ret = new ReturnEntity();
        Session session = sessionFactory.getCurrentSession();
        String hql = "from products where id = :id and uid = :uid";
        Query query = session.createQuery(hql);
        query.setProperties(productVO);
        List l = query.list();
        if (l!=null && l.size()>0){
            ProductVO vo = (ProductVO)l.get(0);
            if(productVO.getCategory_id()!=null && productVO.getCategory_id()>0){
                vo.setCategory_id(productVO.getCategory_id());
            }
            if(productVO.getCity()!=null){
                vo.setCity(productVO.getCity());
            }
            if(productVO.getDescription()!=null){
                vo.setDescription(productVO.getDescription());
            }
            if(productVO.getLatitude()>0){
                vo.setLatitude(productVO.getLatitude());
            }
            if(productVO.getLongitude()>0){
                vo.setLongitude(productVO.getLongitude());
            }
            if(productVO.getPrice()>0){
                vo.setPrice(productVO.getPrice());
            }
            if(productVO.getStatus()!=null){
                vo.setStatus(productVO.getStatus());
            }

            if(productVO.getContact_info()!=null){
                vo.setContact_info(productVO.getContact_info());
            }
            vo.setUpdated_at(new Date());
            session.update(vo);
        }else {
            ret.setResult(ReturnEntity.RETURN_FAILED);
            ret.setError_msg("没有找到符合条件的产品");
        }


        ret.setRetVal(productVO.getId());
        return ret;
    }

    public Object[] selectDeatilById(Integer uid,Integer id){
        Session session = sessionFactory.getCurrentSession();

        String hql = "from products  as p  left join p.favorites as f where p.id = ? and f.uid = ? and p.id = f.pid";
        Query query = session.createQuery(hql).setParameter(0,id).setParameter(1,uid);
        List<Object> list = query.list();
        Object o = list.get(0);
        Object[] obj =  (Object[])o;
        return  obj;
    }


}
