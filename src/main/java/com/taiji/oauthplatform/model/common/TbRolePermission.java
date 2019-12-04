package com.taiji.oauthplatform.model.common;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "tb_role_permission", schema = "common", catalog = "")
@Data
@ToString
public class TbRolePermission implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "JDBC")
    private Long id;

    @Column(name = "role_id")
    private String roleId;

    /**
     * 用户名
     */
    @Column(name = "permission_id")
    private Long permissionId;
}
