package com.springapp.mvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.letcome.entity.CategoryEntity;
import com.letcome.entity.ReturnEntity;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/mvc-dispatcher-servlet.xml","classpath:spring-bean.xml" ,"classpath:spring-hibernate.xml"} )
@Transactional
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = false)
public class AppTests {
    private MockMvc mockMvc;

    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    protected WebApplicationContext wac;

    @Before
    public void setup() {

        this.mockMvc = webAppContextSetup(this.wac).build();
    }

    @Test
    public void simple() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("hello"));
    }
    @Test
    public void testCategories() throws Exception {
//        ResultActions action= mockMvc.perform(post("/user/register"));
        MvcResult result = mockMvc.perform(get("/categories")).andReturn();
        String str = result.getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        long start2 = System.currentTimeMillis();
        CategoryEntity l2 = mapper.readValue(str,CategoryEntity.class);
        System.out.println("size = "+l2.getRecords().size());
        if (!l2.getResult().equals(ReturnEntity.RETURN_SUCCESS) || l2.getRecords().size()<=0){
            throw new Exception("目录查询异常");
        }
//        action.andExpect(content());
    }

    @Test
    public void testAddProduct() throws Exception {
//        ResultActions action= mockMvc.perform(post("/user/register"));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/product/add");
        requestBuilder.header("let_come_uid",9999);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        String str = result.getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        long start2 = System.currentTimeMillis();
        ReturnEntity l2 = mapper.readValue(str,ReturnEntity.class);
        if (!l2.getResult().equals(ReturnEntity.RETURN_SUCCESS) || Integer.valueOf(l2.getRetVal().toString()) <=0){
            throw new Exception("创建产品异常");
        }
//        action.andExpect(content());
    }

}
