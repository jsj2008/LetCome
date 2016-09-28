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
    public List<ProductViewVO > selectProductsAndImage(Integer uid,
                                                       double longitude,//经度
                                                       double latitude,//纬度
                                                       long distance,//距离,单位米
                                                       String cid,//目录id,多目录用逗号分隔
                                                       String productname,//产品名称，模糊查询
                                                       String pricerank,//价格排序，asc/desc
                                                       long starttime,//开始时间，1970年1月1日的秒数
                                                       long endtime,//结束时间，1970年1月1日的秒数
                                                       long start,long limit){





        Session session = sessionFactory.getCurrentSession();
        StringBuffer hql = new StringBuffer("SELECT p.id,p.description,p.created_at,p.updated_at,p.longitude,p.latitude,p.city,p.status,p.price,p.category_id,p.uid,p.fullname,imagename,imagepath,image_id,imageheight,imagewidth,thumbheight,thumbwidth,thumbpath,contact_info,case when f.id>0 then \"Y\" else \"N\" end as is_favorite from products_v p " +
                "left join favorites f on f.uid = ? and p.id = f.pid where 1=1");

        if (longitude!=0 && latitude!=0 && distance>0){
            hql.append(" and FUN_JW_DIST(p.longitude,p.latitude,?,?)<=?");

        }

        if (productname!=null && !productname.equals("")){
            hql.append(" and p.description like ? ");
        }

        if (cid!=null && !cid.equals("")){
            hql.append(" and p.category_id in ("+cid+") ");
        }

        if(starttime>0){
            hql.append(" and UNIX_TIMESTAMP(p.created_at) >= ? ");
        }
        if(endtime>0){
            hql.append(" and UNIX_TIMESTAMP(p.created_at) <= ? ");
        }
        hql.append(" order by ");
        if (pricerank !=null && !pricerank.equals("")) {
            hql.append(" price "+pricerank+",");
        }
        hql.append(" id desc");
        hql.append(" limit ?,?");

        int index = 0;
        Query query = session.createSQLQuery(hql.toString());
        query.setInteger(index++, uid);
        if (longitude!=0 && latitude != 0 && distance>0){
            query.setDouble(index++, longitude);
            query.setDouble(index++, latitude);
            query.setLong(index++, distance);
        }

        if (productname != null && !productname.equals("")){
            query.setString(index++, "%" + productname + "%");
        }

        if(starttime>0){
            query.setLong(index++, starttime);
        }
        if(endtime>0){
            query.setLong(index++,endtime);
        }
        hql.append("order by ");
        if (pricerank !=null && !pricerank.equals("")) {
            hql.append(" price "+pricerank+",");
        }
        query.setLong(index++, start);
        query.setLong(index++, limit);
        query.setResultTransformer(Transformers.aliasToBean(ProductViewVO.class));


        List l = query.list();
//        Map m = (Map)l.get(0);
        System.out.println(l);
        return l;
    }

    public List<ProductViewVO > selectProducts(Integer uid,String status,long start,long limit){
        Session session = sessionFactory.getCurrentSession();
        String hql = "SELECT p.id,p.description,p.created_at,p.updated_at,p.longitude,p.latitude,p.city,p.status,p.price,p.category_id,p.uid,p.fullname,imagename,imagepath,image_id,imageheight,imagewidth,thumbheight,thumbwidth,thumbpath,contact_info from products_v p\n" +
                " where p.uid = ? ";
        if(status !=null ){
            hql+= "and p.status = ? ";
        }
        hql+= " order by id desc limit ?,?";
        int index = 0;
        Query query = session.createSQLQuery(hql)
                .setInteger(index++, uid);
        if (status!=null) {
            query = query.setString(index++, status);
        }
        query.setLong(index++,start)
                .setLong(index++,limit)
                .setResultTransformer(Transformers.aliasToBean(ProductViewVO.class));


        List l = query.list();
//        Map m = (Map)l.get(0);
        System.out.println(l);
        return l;
    }

    public List<ProductViewVO > selectFavorites(Integer uid,String status,long start,long limit){
        Session session = sessionFactory.getCurrentSession();
        String hql = "SELECT p.id,p.description,p.created_at,p.updated_at,p.longitude,p.latitude,p.city,p.status,p.price,p.category_id,p.uid,p.fullname,imagename,imagepath,image_id,imageheight,imagewidth,thumbheight,thumbwidth,thumbpath,contact_info from favorites f" +
                " left join  products_v p on p.id = f.pid " +
                " where f.uid = ? and p.id>0 ";
        if(status !=null ){
            hql+= "and p.status = ? ";
        }
        hql+= " order by id desc limit ?,?";
        int index = 0;
        Query query = session.createSQLQuery(hql)
                .setInteger(index++, uid);
        if (status!=null) {
            query = query.setString(index++, status);
        }
        query.setLong(index++,start)
                .setLong(index++,limit)
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

    public ReturnEntity deleteFavorite(FavoriteVO favoriteVO){
        Session session = sessionFactory.getCurrentSession();
        String hql = "DELETE  from favorites where pid = ? and  uid = ?";
        int index = 0;
        Query query = session.createSQLQuery(hql)
                .setInteger(index++, favoriteVO.getPid())
                .setInteger(index++, favoriteVO.getUid());

        query.executeUpdate();

        ReturnEntity ret = new ReturnEntity();
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

        String hql = "from products as p  left join p.favorites as f with f.uid = ? where p.id = ?";
        Query query = session.createQuery(hql).setParameter(0,uid).setParameter(1, id);
        List<Object> list = query.list();
        Object o = list.get(0);
        Object[] obj =  (Object[])o;
        return  obj;
    }


}
