package com.letcome.service;

import com.letcome.dao.ProductDAO;
import com.letcome.entity.ImageEntity;
import com.letcome.entity.ProductEntity;
import com.letcome.entity.ProductViewEntity;
import com.letcome.entity.ReturnEntity;
import com.letcome.vo.FavoriteVO;
import com.letcome.vo.ImageVO;
import com.letcome.vo.ProductVO;
import com.letcome.vo.ProductViewVO;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by rjt on 16/8/19.
 */
@Transactional
public class ProductService {
    private ProductDAO productDao;

    public ProductDAO getProductDao() {
        return productDao;
    }

    public void setProductDao(ProductDAO productDao) {
        this.productDao = productDao;
    }

    //创建新的产品
    public ReturnEntity addProduct(Integer uid){
        ProductVO vo = new ProductVO();
        vo.setUid(uid);
        vo.setStatus(ProductVO.STATUS_SELLING);
        return  productDao.insertProduct(vo);
    }

    public ReturnEntity addFavorite(Integer uid,Integer pid){
        FavoriteVO vo =  new FavoriteVO();
        vo.setCreated_at(new Date());
        vo.setUid(uid);
        vo.setPid(pid);
        return  productDao.insertFavorite(vo);
    }

    public ProductViewEntity getMyFavorites(Integer uid,String status,long pno,long limit,String downloadPath){
        long start = (pno-1)*limit;
        ProductViewEntity entity = new ProductViewEntity();
        List<ProductViewVO> list= productDao.selectFavorites(uid, status, start, limit);
        for(int i=0;i<list.size();++i){
            ProductViewVO v = list.get(i);
            v.setImagepath(downloadPath+v.getImage_id());
            v.setThumbpath(downloadPath+v.getImage_id());
        }
        entity.setRecords(list);
        return  entity;
    }

    //更新产品
    public ReturnEntity modifyProduct(ProductVO vo){

        return  productDao.updateProduct(vo);
    }

    //获取产品和图片
    public ProductViewEntity getProductsAndImage(Integer uid,
                                                 double longitude,//经度
                                                 double latitude,//纬度
                                                 long distance,//距离,单位米
                                                 String cid,//目录id，多目录用逗号分隔
                                                 String productname,//产品名称，模糊查询
                                                 String pricerank,//价格排序，asc/desc
                                                 long starttime,//开始时间，1970年7月1日的秒数
                                                 long endtime,//结束时间，1970年7月1日的秒数
                                                 long pno,long limit,String downloadPath){
        long start = (pno-1)*limit;

        ProductViewEntity entity = new ProductViewEntity();
        List<ProductViewVO> list=
                productDao.selectProductsAndImage(uid,
                    longitude,
                    latitude,
                    distance,
                    cid,
                    productname,
                    pricerank,
                    starttime,
                    endtime,
                    start,limit);
        for(int i=0;i<list.size();++i){
            ProductViewVO v = list.get(i);
            v.setImagepath(downloadPath+v.getImage_id());
            v.setThumbpath(downloadPath+v.getImage_id());
        }
        entity.setRecords(list);
        return  entity;
    }

    //根据用户id获取产品
    public ProductViewEntity getProducts(Integer uid,String status,long pno,long limit,String downloadPath){
        long start = (pno-1)*limit;

        ProductViewEntity entity = new ProductViewEntity();
        List<ProductViewVO> list= productDao.selectProducts(uid, status, start, limit);
        for(int i=0;i<list.size();++i){
            ProductViewVO v = list.get(i);
            v.setImagepath(downloadPath+v.getImage_id());
            v.setThumbpath(downloadPath+v.getImage_id());
        }
        entity.setRecords(list);
        return  entity;
    }

    public ProductEntity getProductDetail(Integer uid,Integer id,String downloadPath){
        Object[] list = productDao.selectDeatilById(uid, id);
        ProductVO vo  = (ProductVO)list[0];

        ProductEntity e = new ProductEntity();
        e.setId(vo.getId());
        e.setUid(vo.getUid());
        e.setCategory_id(vo.getCategory_id());
        e.setCity(vo.getCity());
        e.setCreated_at(vo.getCreated_at());
        e.setDescription(vo.getDescription());
        e.setLatitude(vo.getLatitude());
        e.setLongitude(vo.getLongitude());
        e.setPrice(vo.getPrice());
        e.setStatus(vo.getStatus());
        e.setUpdated_at(vo.getUpdated_at());
        e.setContact_info(vo.getContact_info());
        e.setIs_favorite(list.length > 1 ? "Y" : "N");
        List<ImageEntity> l = new ArrayList<ImageEntity>();
        for(ImageVO image : vo.getImages()){
            ImageEntity entity = new ImageEntity();
            entity.setId(image.getId());
            entity.setUid(image.getUid());
            entity.setImagename(image.getImagename());
            entity.setImagepath(downloadPath + image.getId());
            entity.setThumbpath(downloadPath + image.getId());
            l.add(entity);
        }
        e.setImages(l);
        return e;
    }

}
