package com.taiji.oauthplatform.config.ssoauth;

import com.google.common.collect.Lists;
import com.taiji.oauthplatform.dao.common.TbPermissionRepository;
import com.taiji.oauthplatform.dao.common.TbUserRepository;
import com.taiji.oauthplatform.model.common.TbPermission;
import com.taiji.oauthplatform.model.common.TbUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 自定义用户登陆 --- 用户信息服务 （查询用户信息）
 * 即创建验证用户,设置用户名和密码并设置角色权限
 */
@Component("SsoUserDetailsService")
public class CostomUserDetailsService implements UserDetailsService {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    private TbUserRepository userRepository;

    @Autowired
    private TbPermissionRepository permissionRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //查询数据库获取用户
        TbUser tbUser = userRepository.findTbUserByUsername(username);

        if(tbUser == null){
            //如果用户查不到，返回null.由provider抛出异常
            throw new UsernameNotFoundException("用户名："+ username + "不存在！");
        }
        List<GrantedAuthority> grantedAuthorities =  Lists.newArrayList();
        //获取权限
        List<TbPermission> permissions = permissionRepository.findAllById(tbUser.getId());
        // 声明用户授权

        permissions.forEach(permission -> {
            if (permission != null && permission.getEnname() != null) {
                GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(permission.getEnname());
                grantedAuthorities.add(grantedAuthority);
            }
        });
        //验证成功返回用户数据 权限控制
//        return new User(username,
//                passwordEncoder().encode(tbUser.getPassword()),
//                AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_ADMIN"));
        return new User(tbUser.getUsername(),
                passwordEncoder().encode(tbUser.getPassword()),
                grantedAuthorities);
    }
}