package com.taiji.oauthplatform.controller.sysuser;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 该接口类中的唯一接口，用于ClientA和ClientB在登录成功后获取用户信息用
 * 该接口地址可以任意修改，只要与ClientA/B中配置的用户信息地址一致即可
 */
@Controller
@RequestMapping(value = "/test")
public class TestController {

    @RequestMapping(value = "/test")
    @ResponseBody
    @PreAuthorize("hasAuthority('System')")
    public String testData() {
        return "测试数据";
    }

    @RequestMapping(value = "/test1")
    @ResponseBody
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String testData1() {
        return "测试权限数据";
    }
}
