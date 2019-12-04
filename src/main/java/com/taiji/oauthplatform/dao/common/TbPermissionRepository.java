package com.taiji.oauthplatform.dao.common;

import com.taiji.oauthplatform.model.common.TbPermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface TbPermissionRepository extends JpaRepository<TbPermission,Integer> {
    //设置nativeQuery=true 即可以使用原生的SQL进行查询
//    @Query(value = "SELECT * FROM (SELECT permission_id FROM  ( SELECT role_id FROM tb_user_role WHERE user_id = ?1 ) a LEFT JOIN tb_role_permission b ON a.role_id = b.role_id ) c LEFT JOIN tb_permission d ON c.permission_id = d.id", nativeQuery = true)
    @Query(value =  "SELECT p.* FROM tb_user AS u LEFT JOIN tb_user_role AS ur ON u.id = ur.user_id LEFT JOIN tb_role AS r ON r.id = ur.role_id LEFT JOIN tb_role_permission AS rp ON r.id = rp.role_id LEFT JOIN tb_permission AS p ON p.id = rp.permission_id WHERE u.id = ?1", nativeQuery = true)
    public List<TbPermission> findAllById(Long id);
}
