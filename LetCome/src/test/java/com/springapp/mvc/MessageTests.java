package com.springapp.mvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.letcome.entity.CategoryEntity;
import com.letcome.entity.MessageEntity;
import com.letcome.entity.ReturnEntity;
import com.letcome.entity.UserEntity;
import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

/**
 * Created by rjt on 16/8/30.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/mvc-dispatcher-servlet.xml","classpath:spring-bean.xml" ,"classpath:spring-hibernate.xml"} )
@Transactional
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = false)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)//按照字母升序顺序执行测试方法
public class MessageTests {

    private String uid = "10022";
    private String toid = "9998";
    private String c1 = "asdfasdf撒旦法的说法第三方123123123";
    private String c2 = "fghfghfghttyt顺丰速递发123123123";

    private MockMvc mockMvc;

    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    protected WebApplicationContext wac;

    @Before
    public void setup() {
        this.mockMvc = webAppContextSetup(this.wac).build();
    }

    @Test
    public void test_A_addMessage() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/message/add");
        requestBuilder.header("Content-Type", "application/json");
        requestBuilder.header("let_come_uid", uid);
        Map<String,String> param = new HashMap<String,String>();

        param.put("toid", toid);
        param.put("content",c1);

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
//        action.andExpect(content());
    }

    @Test
         public void test_B_addMessage() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/message/add");
        requestBuilder.header("Content-Type", "application/json");
        requestBuilder.header("let_come_uid", toid);
        Map<String,String> param = new HashMap<String,String>();

        param.put("toid", uid);
        param.put("content",c2);

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
//        action.andExpect(content());
    }

    @Test
    public void test_C_findMessage() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/message/get");
        requestBuilder.header("let_come_uid", uid);
        requestBuilder.param("toid", toid);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        String str = result.getResponse().getContentAsString();
        System.out.println("str = " + str);
        ObjectMapper mapper = new ObjectMapper();
        MessageEntity l2 = mapper.readValue(str, MessageEntity.class);
        Assert.assertEquals(l2.getResult(), ReturnEntity.RETURN_SUCCESS);
        assertThat(l2.getRecords().size(), greaterThan(1));
//        action.andExpect(content());
    }

    @Test
    public void test_D_findUsers() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/message/user");
        requestBuilder.header("let_come_uid", uid);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        String str = result.getResponse().getContentAsString();
        System.out.println("str = " + str);
        ObjectMapper mapper = new ObjectMapper();
        UserEntity l2 = mapper.readValue(str, UserEntity.class);
        Assert.assertEquals(l2.getResult(), ReturnEntity.RETURN_SUCCESS);
        assertThat(l2.getRecords().size(),greaterThan(1));
//        action.andExpect(content());
    }

}
