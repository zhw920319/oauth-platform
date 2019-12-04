package com.taiji.oauthplatform.config.ssoauth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 自定义用户登陆逻辑配置
 */
@Configuration
@Order(1)//@Order标记定义了组件的加载顺序，值越小拥有越高的优先级，可为负数。
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    @Qualifier("SsoUserDetailsService")
    private UserDetailsService userDetailsService;

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    /**
     * 密码编码器
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }
//    @Bean
//    public DaoAuthenticationProvider authenticationProvider() {
//        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
//        authenticationProvider.setUserDetailsService(userDetailsService);
//        authenticationProvider.setPasswordEncoder(passwordEncoder());
//        authenticationProvider.setHideUserNotFoundExceptions(false);
//        return authenticationProvider;
//    }
    /**
     * 安全拦截机制
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 改成表单登陆的方式 所有请求都需要认证
//        http.formLogin().and().authorizeRequests().anyRequest().authenticated();
        //配置不需要登陆验证
//        http.authorizeRequests().anyRequest().permitAll().and().logout().permitAll();
                http
                    .csrf().disable()
//                        .antMatcher("/**")
//                    .requestMatchers()
//                    .antMatchers("/login", "/oauth/**","/**")//"/**"登陆后可以访问方法
//                    .and()
                    .authorizeRequests()
                    .antMatchers( "/oauth/**","swagger-ui.html")
                    .authenticated()
                    .and()
                    .formLogin()//允许表单登陆
                    .loginPage("/login")//自定义登陆页面
                    .defaultSuccessUrl("/home")
                    .permitAll()//允许任何人访问登录url
                        .and()
                    .logout().permitAll();
//                    .successForwardUrl("/user/loginSuccess");//自定义登陆成功页面地址
    }

    /**
     * 用户信息服务 （查询用户信息）
     * @param auth
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // 用自己的登陆逻辑以及加密器
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
        //内存中配置
//        auth.inMemoryAuthentication()
//                .withUser("john")
//                .password(passwordEncoder().encode("123"))
//                .roles("USER");
    }
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/favor.ioc");
    }

}
