package com.yx.controller;

import com.yx.model.Userinfo;
import com.yx.service.IUserinfoService;
import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.easymock.EasyMock.*;
import static org.easymock.EasyMock.expect;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.*;

@RunWith(value = Parameterized.class)
@SpringBootTest
public class LoginTest {
    @Autowired
    private LoginController loginController;
    @Autowired
    private IUserinfoService userinfoService;
    private HttpServletRequest request;
    private HttpSession session;
    private Userinfo userinfo;

    private Object username,password;
    public LoginTest(Object username,Object password){
        this.username=username;
        this.password=password;
    }

    @Before
    public void init(){
        request = createStrictMock(HttpServletRequest.class);
        session=createStrictMock(HttpSession.class);
        userinfoService=createStrictMock(IUserinfoService.class);
        loginController=new LoginController();
        loginController.userinfoService=userinfoService;
        userinfo=new Userinfo();
    }

    @Parameterized.Parameters(name="{index}:username={0},password={1}")
    public static Iterable<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"admin","123456"},
                {"123456"}, {123,"123456"},
                {"admin","123"}, {"admin",123},
                {null,"123456"}, {"admin",null}, {null,null}

        });
    }

    @Test
    public void loginInFailedTest1() {
        expect(request.getSession()).andReturn(null);
        replay(request);
        Map map=new HashMap();
        map.put("code",404);
        map.put("msg","登录超时了");
        Assert.assertEquals(map,loginController.loginIn(userinfo,request));
    }

    @Test
    public void loginInFailedTest2() {
        expect(userinfoService.queryUserByNameAndPwd(userinfo)).andReturn(null);
        expect(request.getSession()).andReturn(session);
        replay(userinfoService);
        replay(request);
        Map map=new HashMap();
        map.put("code",404);
        map.put("msg","用户名或者密码错误");
        Assert.assertEquals(map,loginController.loginIn(userinfo,request));
    }

    @Test
    public void loginInFailedTest3() {
        try{
            userinfo.setUsername((String) username);
            userinfo.setPassword((String) password);
            expect(userinfoService.queryUserByNameAndPwd(userinfo)).andReturn(userinfo);
            expect(request.getSession()).andReturn(session);
            replay(userinfoService);
            replay(request);

            Map map=new HashMap();
            map.put("code",200);
            map.put("user",userinfo);
            map.put("username",userinfo.getUsername());
            Assert.assertEquals(map,loginController.loginIn(userinfo,request));
        } catch(Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }

    }

    @Test
    public void loginInSuccessTest() {
        userinfo.setUsername("admin");
        userinfo.setPassword("123456");
        expect(userinfoService.queryUserByNameAndPwd(userinfo)).andReturn(userinfo);
        expect(request.getSession()).andReturn(session);
        replay(userinfoService);
        replay(request);

        Map map=new HashMap();
        map.put("code",200);
        map.put("user",userinfo);
        map.put("username",userinfo.getUsername());
        Assert.assertEquals(map,loginController.loginIn(userinfo,request));
    }

    @Test
    public void loginOut() {
    }
}