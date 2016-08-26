package com.springapp.mvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.letcome.entity.LoginEntity;
import com.letcome.entity.ReturnEntity;
import org.junit.*;
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

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
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
public class RegisterAndLoginTests {

    static Date date;
    static String email ;
    static String fullname ;
    static String pwd;

    private MockMvc mockMvc;

    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    protected WebApplicationContext wac;

    @Before
    public void setup() {

        this.mockMvc = webAppContextSetup(this.wac).build();
    }

    @BeforeClass
    public static void initParam(){
        date = new Date();
        email = "test" + date.getTime() + "@test.com";
        pwd = "12345678";
        fullname = "牛B";
    }
    //登录测试
    @Test
    public void test_B_Login() throws Exception {
        //登录测试
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/user/login");
        requestBuilder.header("Content-Type", "application/json");

        Map<String,String> param = new HashMap<String,String>();
        param.put("email", email);
        param.put("pwd", pwd);

        ObjectMapper pMapper = new ObjectMapper();
        String pStr = pMapper.writeValueAsString(param);
        System.out.println(date.getTime()+":"+pStr);

        requestBuilder.content(pStr);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        String str = result.getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();

        LoginEntity l2 = mapper.readValue(str, LoginEntity.class);
        Assert.assertEquals(l2.getResult(), ReturnEntity.RETURN_SUCCESS);

        assertThat(Long.valueOf(l2.getUid()), greaterThan(0l));
        assertThat(l2.getFullname(),equalTo(fullname));
        assertThat(l2.getSessionid(), notNullValue());
    }
    //注册测试
    @Test
    public void test_A_Register() throws Exception {
        //注册测试
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/user/register");
        requestBuilder.header("Content-Type", "application/json");

        Map<String,String> param = new HashMap<String,String>();
        param.put("email",email );
        param.put("pwd", pwd);
        param.put("fullname",fullname);

        ObjectMapper pMapper = new ObjectMapper();
        String pStr = pMapper.writeValueAsString(param);
        System.out.println(date.getTime()+":"+pStr);

        requestBuilder.content(pStr);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        String str = result.getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();

        LoginEntity l2 = mapper.readValue(str, LoginEntity.class);
        Assert.assertEquals(l2.getResult(), ReturnEntity.RETURN_SUCCESS);

        assertThat(Long.valueOf(l2.getUid()), greaterThan(0l));
        assertThat(l2.getFullname(),equalTo(fullname));
        assertThat(l2.getSessionid(), notNullValue());

    }

}
