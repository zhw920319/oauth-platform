package com.taiji.oauthplatform.controller.sysuser;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;
import java.util.Map;

@Controller
public class LoginContoller {

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

    @RequestMapping(value = "/login")
    public String login() {
        return "login";
    }

    @RequestMapping(value = "/logout")
    public String logout() {
        return "login";
    }

}
