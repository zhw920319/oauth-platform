package com.taiji.oauthplatform.config.ssoauth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;

import javax.sql.DataSource;
import java.util.concurrent.TimeUnit;

/**
 * 认证授权服务器配置
 * 一是 定义了客户端应用的通行证即客户端详情；即校验客户端是否合法
 * 二是 令牌管理服务
 * 三是 令牌访问端点
 * 四是 令牌存储策略
 * 五是 令牌访问端点安全策略
 *
 */
@Configuration
@EnableAuthorizationServer
public class AuthServerConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private ClientDetailsService clientDetailsService;

//    @Autowired
//    private AuthorizationCodeServices authorizationCodeServices;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    public BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserDetailsService userDetailsService;

    //引入数据库 把客户端详情存储在数据库中
    @Autowired
    @Qualifier("commonDataSource")
    private DataSource commonDataSource;

    @Bean
    public ClientDetailsService jdbcClientDetails() {
        // 基于 JDBC 实现，需要事先在数据库配置客户端信息
        return new JdbcClientDetailsService(commonDataSource);
    }
    //token存储
    @Bean
    public TokenStore tokenStore() {
        // 基于 JDBC 实现，令牌保存到数据
//        return new JdbcTokenStore(dataSource());
        return new JdbcTokenStore(commonDataSource);
    }

    /**
     * 配置客户端详情服务 ClientID 客户端标识，client secret 客户端密钥 --- 后期通过数据库获取配置
     * @param clients
     * @throws Exception
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        // 读取客户端配置
        clients.withClientDetails(jdbcClientDetails());
//
////        内存方式
//        clients.inMemory()
//                .withClient("client1") //客户端ID
//                .secret(passwordEncoder.encode("secret1"))//端户端密钥
////                .resourceIds("res1")//资源列表 对应资源服务的ID
//                .authorizedGrantTypes("authorization_code", "refresh_token")//该client允许的授权类型（oauth2.0的5种类型）
//                .scopes("all")//允许的授权范围 可以获取的资源权限
//                .autoApprove(true)//false 授权码方式  跳转到授权页面
//                .redirectUris("http://localhost:8086/securedPage")//回调地址 返回授权码
//                .and()
//                .withClient("client2") //客户端ID
//                .secret(passwordEncoder.encode("secret2"))//端户端密钥
////                .resourceIds("res1")//资源列表 对应资源服务的ID
//                .authorizedGrantTypes("authorization_code", "refresh_token")//该client允许的授权类型（oauth2.0的5种类型）
//                .scopes("all")//允许的授权范围
//                .autoApprove(true)//false 授权码方式  跳转到授权页面
//                .redirectUris("https://www.jd.com");//回调地址
    }

    /**
     * 用来配置令牌的访问端点，和令牌服务（token生成）
     * @param endpoints
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
//        endpoints.accessTokenConverter(jwtAccessTokenConverter());
//        endpoints.tokenServices(tokenServices());
        //令牌访问端点
        endpoints
                .authenticationManager(authenticationManager)//认证管理器
                .authorizationCodeServices(endpoints.getAuthorizationCodeServices())//授权码模式需要
                .userDetailsService(userDetailsService)
                .tokenServices(tokenService())//令牌管理服务
//                .accessTokenConverter(jwtAccessTokenConverter())
                .allowedTokenEndpointRequestMethods(HttpMethod.POST,HttpMethod.GET);//允许访问方式
    }

    /**
     * 令牌管理服务
     * @return
     */
    public AuthorizationServerTokenServices tokenService() {
        DefaultTokenServices tokenServices = new DefaultTokenServices();
        //令牌服务
        tokenServices.setTokenStore(tokenStore());
        tokenServices.setSupportRefreshToken(true);//是否产生刷新令牌
        tokenServices.setClientDetailsService(clientDetailsService);//客户端信息服务
//        tokenServices.setTokenEnhancer(endpoints.getTokenEnhancer());
        tokenServices.setAccessTokenValiditySeconds((int) TimeUnit.DAYS.toSeconds(1)); // 一天有效期
        tokenServices.setRefreshTokenValiditySeconds(259200);//刷新令牌默认有效期3天
        return tokenServices;
    }

    /**
     * 认证服务器的安全配置
     *  用来配置令牌端点的安全约束，那些可以访问
     *  令牌访问端点安全策略即安全约束
     * @param security
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        // 要访问认证服务器tokenKey的时候需要经过身份认证
        security
                .tokenKeyAccess("permitAll()")///oauth/token 是公开
                .checkTokenAccess("permitAll()")///oauth/check_token 是公开
                .allowFormAuthenticationForClients();//表单认证（申请令牌）
    }

    /**
     * 令牌存储策略
     * 告诉spring security 的生成方式
     * @return
     */
//    @Bean
//    public TokenStore jwtTokenStore() {
//        return new JwtTokenStore(jwtAccessTokenConverter());
//    }

    /**
     * AccessToken转换器-定义token生成方式
     * @return
     */
//    @Bean
//    ///认证服务1-对称加密方式
//    public JwtAccessTokenConverter jwtAccessTokenConverter() {
//        JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
//        // 保证JWT安全的唯一方式
//        jwtAccessTokenConverter.setSigningKey("secret");
//        return jwtAccessTokenConverter;
//    }
    //认证服务2-非对称加密方式（公钥密钥）
}