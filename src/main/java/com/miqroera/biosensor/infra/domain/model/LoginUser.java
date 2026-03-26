package com.miqroera.biosensor.infra.domain.model;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 登录用户身份权限
 *
 * @author Lion Li
 */

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoginUser implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户 ID
     */
    @Schema(description = "用户 ID", example = "1")
    private Long id;

    /**
     * 用户账号
     */
    @Schema(description = "用户账号", example = "admin")
    private String userName;

    /**
     * 用户昵称
     */
    @Schema(description = "用户昵称", example = "管理员")
    private String nickName;

    /**
     * 用户类型（0 普通用户， 1 管理员, 2 审计员）
     */
    @Schema(description = "用户类型（0 普通用户，1 管理员，2 审计员，3 运营人员）", example = "1")
    private Integer userType;

    /**
     * 用户邮箱
     */
    @Schema(description = "用户邮箱", example = "admin@example.com")
    private String email;

    /**
     * 手机号码
     */
    @Schema(description = "手机号码", example = "13800138000")
    private String phonenumber;

    /**
     * 用户性别（0 男 1 女 2 未知）
     */
    @Schema(description = "用户性别（0 男 1 女 2 未知）", example = "0")
    private String sex;

    /**
     * 头像地址
     */
    @Schema(description = "头像地址", example = "https://example.com/avatar.jpg")
    private String avatar;

    /**
     * 账号状态（0 正常 1 停用）
     */
    @Schema(description = "账号状态（0 正常 1 停用）", example = "0")
    private String status;

    /**
     * 最后登录 IP
     */
    @Schema(description = "最后登录 IP", example = "192.168.1.1")
    private String loginIp;

    /**
     * 最后登录时间
     */
    @Schema(description = "最后登录时间", example = "2024-01-01 12:00:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date loginDate;

    /**
     * 密码最后更新时间
     */
    @Schema(description = "密码最后更新时间", example = "2024-01-01 12:00:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date pwdUpdateDate;

    /**
     * 备注
     */
    @Schema(description = "备注", example = "这是一个测试备注")
    private String remark;

    /**
     * 创建时间
     */
    @Schema(description = "创建时间", example = "2024-01-01 12:00:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @Schema(description = "是否为管理员", example = "true")
    private Boolean isAdmin = false;

    @Schema(description = "角色权限列表")
    private Map<String, List<String>> rolePermissions;

    public Boolean getIsAdmin() {
        return isAdmin != null && isAdmin;
    }

    public boolean isSuperAdmin() {
        return Long.valueOf(1).equals(id);
    }

    public String getRoleKey(String permission) {
        Map<String, List<String>> map = getRolePermissions();
        if (map != null) {
            for (Map.Entry<String, List<String>> entry : map.entrySet()) {
                List<String> permissions = entry.getValue();
                if (permissions != null && permissions.contains(permission)) {
                    return entry.getKey();
                }
            }
        }
        return null;
    }
}
