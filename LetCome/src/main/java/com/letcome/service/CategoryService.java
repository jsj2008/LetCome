package com.letcome.service;


import com.letcome.dao.CategoryDAO;
import com.letcome.entity.CategoryEntity;
import com.letcome.vo.CategoryVO;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rjt on 16/8/19.
 */
@Transactional
public class CategoryService {
    private CategoryDAO categoryDao;

    public CategoryDAO getCategoryDao() {
        return categoryDao;
    }
    public void setCategoryDao(CategoryDAO categoryDao) {
        this.categoryDao = categoryDao;
    }

    public CategoryEntity getCategories(){
        List<CategoryVO> list = categoryDao.getCategories();
        CategoryEntity retList = new CategoryEntity();
        retList.setRecords(list);
        return  retList;

    }
}
