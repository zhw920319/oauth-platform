package com.taiji.oauthplatform.controller.sysuser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;
import java.util.Map;

@Controller
public class LoginContoller {

    @Autowired
    @Qualifier("consumerTokenServices")
    ConsumerTokenServices consumerTokenServices;

    @Autowired
    public LogoutSuccessHandler logoutSuccessHandler;

    /**
     * 获取信息
     * @param principal
     * @return
     */
    @GetMapping(value = "/me")
    @ResponseBody
    public Principal me(Principal principal) {
        System.out.println("调用me接口获取用户信息：" + principal);
        return principal;
    }

    @RequestMapping(value = "/home")
    public String loginSuccess(Map<String,Object> map) {
        map.put("username","张");
        System.out.println("登陆成功");
        return "home";
    }

    @RequestMapping(value = "/userLogin")
    public String login() {
        return "login";
    }

    @RequestMapping(value = "/logout")
    public String logout() {
        return "login";
    }

    @RequestMapping(value = "/exit")
    @ResponseBody
    public void exit(String token, HttpServletRequest request, HttpServletResponse response) {
        consumerTokenServices.revokeToken(token);
        try {
            response.sendRedirect(request.getHeader("referer"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
