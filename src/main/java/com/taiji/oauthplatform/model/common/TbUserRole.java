package com.taiji.oauthplatform.model.common;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "tb_user_role", schema = "common", catalog = "")
@Data
@ToString
public class TbUserRole implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "JDBC")
    private Long id;

    /**
     * 用户名
     */
    @Column(name = "user_id")
    private Long userId;

    /**
     * 密码，加密存储
     */
    @Column(name = "role_id")
    private String roleId;

}
