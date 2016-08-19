package com.letcome.controller;

import com.letcome.entity.CategoryEntity;
import com.letcome.entity.ReturnEntity;
import com.letcome.service.CategoryService;
import com.letcome.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * Created by rjt on 16/8/19.
 */
@Controller
@RequestMapping("/product")
public class ProductController {
    @Resource(name="productService")
    private ProductService service;
    @RequestMapping("/add")
    @ResponseBody
    public ReturnEntity addProduct(@RequestHeader("let_come_uid") String  uid, ModelMap model) {
        return service.addProduct(Integer.valueOf(uid));
    }
}
