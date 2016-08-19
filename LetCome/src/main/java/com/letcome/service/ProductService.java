package com.letcome.service;

import com.letcome.dao.ProductDAO;
import com.letcome.dao.UsersDAO;
import com.letcome.entity.ReturnEntity;
import com.letcome.vo.ProductVO;
import org.springframework.transaction.annotation.Transactional;

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
        return  productDao.insertProduct(vo);
    }
}
