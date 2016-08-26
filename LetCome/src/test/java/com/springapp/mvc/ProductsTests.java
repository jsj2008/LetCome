package com.springapp.mvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.letcome.entity.CategoryEntity;
import com.letcome.entity.ProductEntity;
import com.letcome.entity.ProductViewEntity;
import com.letcome.entity.ReturnEntity;
import com.letcome.vo.ProductVO;
import com.letcome.vo.ProductViewVO;
import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.URI;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.fileUpload;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

/**
 * Created by rjt on 16/8/24.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/mvc-dispatcher-servlet.xml","classpath:spring-bean.xml" ,"classpath:spring-hibernate.xml"} )
@Transactional
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = false)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)//按照字母升序顺序执行测试方法
public class ProductsTests {

    static String filename = "03_i4s.jpg";
    static String fieldname = "myfiles";
    static String uid = "9999";
    static Integer pid;
    static String description = "真TMD好";
    static String longitude = "31.595";
    static String latitude = "66.6666";
    static String city = "上海";
    static String status = ProductVO.STATUS_PUBLISH;
    static String price = "12.55";
    static String contact = "13912345678";
    static Integer category_id;

    private MockMvc mockMvc;

    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    protected WebApplicationContext wac;

    @Before
    public void setup() {

        this.mockMvc = webAppContextSetup(this.wac).build();
    }

    @Test
    public void test_A_Categories() throws Exception {
//        ResultActions action= mockMvc.perform(post("/user/register"));
        MvcResult result = mockMvc.perform(get("/categories")).andReturn();
        String str = result.getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        CategoryEntity l2 = mapper.readValue(str,CategoryEntity.class);
        System.out.println("size = " + l2.getRecords().size());

        Assert.assertEquals(l2.getResult(), ReturnEntity.RETURN_SUCCESS);
        assertThat(Long.valueOf(l2.getRecords().size()), greaterThan(0l));
        category_id = l2.getRecords().get(0).getId();
//        action.andExpect(content());
    }

    @Test
    public void test_B_AddProduct() throws Exception {
//        ResultActions action= mockMvc.perform(post("/user/register"));
        URL url = getClass().getResource("/"+filename);
        File f = new File(url.getFile());
        FileInputStream f1 = new FileInputStream(f);

        MockMultipartFile mockMultipartFile = new MockMultipartFile(fieldname, filename, "multipart/form-data", f1);

        MockMultipartHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.fileUpload("/image/upload");
        requestBuilder.file(mockMultipartFile);
        requestBuilder.header("let_come_uid", "9999");
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        String str = result.getResponse().getContentAsString();
        System.out.println("str = " + str);
        ObjectMapper mapper = new ObjectMapper();
        ReturnEntity l2 = mapper.readValue(str, ReturnEntity.class);
        Assert.assertEquals(l2.getResult(), ReturnEntity.RETURN_SUCCESS);
        assertThat(Integer.valueOf(l2.getRetVal().toString()), greaterThan(0));
        pid = Integer.valueOf(l2.getRetVal().toString());
    }

    @Test
    public void test_C_UpdateProduct() throws Exception{
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/product/update");
        requestBuilder.header("Content-Type", "application/json");
        requestBuilder.header("let_come_uid", "9999");
        Map<String,String> param = new HashMap<String,String>();

        param.put("id",pid.toString() );
        param.put("description", description);
        param.put("longitude",longitude);
        param.put("latitude",latitude);
        param.put("city",city);
        param.put("price", price);
        param.put("category_id", category_id.toString());
        param.put("contact_info", contact);

        ObjectMapper pMapper = new ObjectMapper();
        String pStr = pMapper.writeValueAsString(param);
        System.out.println(pStr);

        requestBuilder.content(pStr);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        String str = result.getResponse().getContentAsString();
        System.out.println("str = " + str);
        ObjectMapper mapper = new ObjectMapper();
        ReturnEntity l2 = mapper.readValue(str, ReturnEntity.class);
        Assert.assertEquals(l2.getResult(), ReturnEntity.RETURN_SUCCESS);

    }

    @Test
    public void test_D_UpdateNotionProduct() throws Exception{
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/product/update");
        requestBuilder.header("Content-Type", "application/json");
        requestBuilder.header("let_come_uid", "9999");
        Map<String,String> param = new HashMap<String,String>();

        param.put("id",pid.toString() );

        ObjectMapper pMapper = new ObjectMapper();
        String pStr = pMapper.writeValueAsString(param);
        System.out.println(pStr);

        requestBuilder.content(pStr);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        String str = result.getResponse().getContentAsString();
        System.out.println("str = " + str);
        ObjectMapper mapper = new ObjectMapper();
        ReturnEntity l2 = mapper.readValue(str, ReturnEntity.class);
        Assert.assertEquals(l2.getResult(), ReturnEntity.RETURN_SUCCESS);

    }

    @Test
    public void test_E_AddFavorite() throws Exception{
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/product/favorite");
//        requestBuilder.header("Content-Type", "application/json");
        requestBuilder.header("let_come_uid", "9999");
        requestBuilder.param("pid", String.valueOf(pid));

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        String str = result.getResponse().getContentAsString();
        System.out.println("str = " + str);
        ObjectMapper mapper = new ObjectMapper();
        ReturnEntity l2 = mapper.readValue(str, ReturnEntity.class);
        Assert.assertEquals(l2.getResult(), ReturnEntity.RETURN_SUCCESS);
        assertThat(Integer.valueOf(l2.getRetVal().toString()), greaterThan(0));
    }

    @Test
    public void test_F_GetProductDetail() throws Exception{
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/product/detail");
        requestBuilder.header("let_come_uid", uid);
        requestBuilder.param("id", "50");
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        String str = result.getResponse().getContentAsString();
        System.out.println("str = " + str);
        ObjectMapper mapper = new ObjectMapper();
        ProductEntity l2 = mapper.readValue(str, ProductEntity.class);
        assertThat(l2.getId(), greaterThan(0));
        assertThat(l2.getDescription(), equalTo(description));
        assertThat(String.valueOf(l2.getLongitude()), equalTo(longitude));
        assertThat(String.valueOf(l2.getLatitude()), equalTo(latitude));
        assertThat(l2.getCity(), equalTo(city));
        assertThat(l2.getStatus(), equalTo(status));
        assertThat(String.valueOf(l2.getPrice()), equalTo(price));
        assertThat(String.valueOf(l2.getCategory_id()), equalTo(category_id.toString()));
        assertThat(l2.getImages().size(), greaterThan(0));
        assertThat(l2.getCreated_at(), notNullValue());
        assertThat(l2.getUpdated_at(), notNullValue());
        assertThat(l2.getContact_info(), equalTo(contact));
        assertThat(l2.getIs_favorite(), equalTo("Y"));
    }

    @Test
    public void test_G_UpdateProductStatus() throws Exception{
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/product/update");
        requestBuilder.header("Content-Type", "application/json");
        requestBuilder.header("let_come_uid", "9999");
        Map<String,String> param = new HashMap<String,String>();
        status = ProductVO.STATUS_SELLING;
        param.put("id", pid.toString());
        param.put("status", status);

        ObjectMapper pMapper = new ObjectMapper();
        String pStr = pMapper.writeValueAsString(param);
        System.out.println(pStr);

        requestBuilder.content(pStr);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        String str = result.getResponse().getContentAsString();
        System.out.println("str = " + str);
        ObjectMapper mapper = new ObjectMapper();
        ReturnEntity l2 = mapper.readValue(str, ReturnEntity.class);
        Assert.assertEquals(l2.getResult(), ReturnEntity.RETURN_SUCCESS);

        test_F_GetProductDetail();
    }

    @Test
    public void test_H_GetWallFalls() throws Exception{
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/waterfalls");
//        requestBuilder.header("Content-Type", "application/json");
        requestBuilder.header("let_come_uid", "9999");
        Map<String,String> param = new HashMap<String,String>();

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        String str = result.getResponse().getContentAsString();
        System.out.println("str = " + str);
        ObjectMapper mapper = new ObjectMapper();
        ProductViewEntity l2 = mapper.readValue(str, ProductViewEntity.class);
        assertThat(l2.getRecords().size(), greaterThan(0));
    }




}
