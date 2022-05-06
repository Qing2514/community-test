package com.yx.controller;

import com.yx.model.Userinfo;
import com.yx.service.IUserinfoService;
import org.easymock.EasyMock;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.omg.CORBA.UserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@RunWith(Parameterized.class)
public class LoginControllerTest {

    LoginController loginController = new LoginController();

    private final String username;
    private final String password;
    private final Integer type;

    public LoginControllerTest(String username, String password, Integer type) {
        this.username = username;
        this.password = password;
        this.type = type;
    }

    @Parameterized.Parameters(name = "{index}: username = {0}, password = {1}, type = {2}")
    public static Iterable<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"admin", "123456", 1},
                // {123456, "123456", 1},
                // {"admin", 123456},
                // {123456, 123456},
                // {null, "123456"},
                // {"admin", null},
                // {null, null},
                // {"admin"},
                // {"admin", "123456","111"},
                // {"adminadminadmin", "123456"},
                // {"admin", "123456123456"},
                // {"adminadminadmin", "123456123456"},
                // {"ad", "123456"},
                // {"admin", "123"},
                // {"ad", "123"},
                // {"*****", "123456"},
                // {"admin", "******"},
                // {"*****", "******"},
        });
    }

    @Test
    public void testExecute() {

        Userinfo user = new Userinfo(username, password, type);

        HttpSession session = EasyMock.createMock(HttpSession.class);
        IUserinfoService userinfoService = EasyMock.createMock(IUserinfoService.class);
        HttpServletRequest request = EasyMock.createMock(HttpServletRequest.class);
        loginController.userinfoService = userinfoService;

        EasyMock.expect(userinfoService.queryUserByNameAndPwd(user)).andReturn(user);
        EasyMock.expect(request.getSession()).andReturn(session);
        EasyMock.replay(userinfoService);
        EasyMock.replay(request);

        Map map = loginController.loginIn(user, request);
        assertEquals(200, map.get("code"));

    }
}