package com.letcome.controller;

import com.letcome.entity.CategoryEntity;
import com.letcome.service.CategoryService;
import com.letcome.service.ImageService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by rjt on 16/8/19.
 */
@Controller

public class CategoryController {
    @Resource(name="categoryService")
    private CategoryService service;
    @RequestMapping("/categories")
    @ResponseBody
    public CategoryEntity getCategories(ModelMap model) {
        return service.getCategories();
    }
}
