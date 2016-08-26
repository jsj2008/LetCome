package com.letcome.controller;

import com.letcome.entity.CategoryEntity;
import com.letcome.entity.ProductEntity;
import com.letcome.entity.ProductViewEntity;
import com.letcome.entity.ReturnEntity;
import com.letcome.service.CategoryService;
import com.letcome.service.ProductService;
import com.letcome.vo.ProductVO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

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

    @RequestMapping(value = "/update",method = RequestMethod.POST)
    @ResponseBody
    public ReturnEntity updateProduct(@RequestHeader("let_come_uid") String  uid,
                                      @RequestBody ProductVO vo,
//                                      @RequestParam("id") String id,
//                                      @RequestParam(value = "description",required = false) String description,
//                                      @RequestParam(value = "longitude",required = false,defaultValue = "0") String longitude,//经度
//                                      @RequestParam(value = "latitude",required = false,defaultValue = "0") String latitude,//纬度
//                                      @RequestParam(value = "city",required = false) String city,
//                                      @RequestParam(value = "status",required = false) String status,
//                                      @RequestParam(value = "price",required = false,defaultValue = "0") String price,
//                                      @RequestParam(value = "category_id",required = false ,defaultValue = "0") String category_id,
                                      ModelMap model) {

//        ProductVO vo = new ProductVO();
        vo.setUid(Integer.valueOf(uid));
//        vo.setId(Integer.valueOf(id));
//        vo.setDescription(description);
//        vo.setLongitude(Double.valueOf(longitude));
//        vo.setLatitude(Double.valueOf(latitude));
//        vo.setCity(city);
//        vo.setStatus(status);
//        vo.setPrice(Float.valueOf(price));
//        vo.setCategory_id(Integer.valueOf(category_id));
        return service.updateProduct(vo);
    }

    @RequestMapping(value = "/detail",method = RequestMethod.GET)
    @ResponseBody
    public ProductEntity detailProduct(@RequestHeader("let_come_uid") String  uid,@RequestParam("id") String id,HttpServletRequest request) {

        return service.getProductDetail(Integer.valueOf(uid),Integer.valueOf(id), "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/image/getimg?id=");
    }

    @RequestMapping(value = "/favorite",method = RequestMethod.GET)
    @ResponseBody
    public ReturnEntity favoriteProduct(@RequestHeader("let_come_uid") String  uid,
            @RequestParam("pid") String pid) {

        return service.updateFavorite(Integer.valueOf(uid),Integer.valueOf(pid));
    }


}
