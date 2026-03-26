-- 用户表
CREATE TABLE `sys_user`
(
    `id`              bigint       NOT NULL AUTO_INCREMENT,
    `wx_mp_openid`    varchar(64)  null comment '微信小程序 openid',
    `user_name`       varchar(30)  NOT NULL COMMENT '用户账号',
    `nick_name`       varchar(30)  NOT NULL COMMENT '用户昵称',
    `user_type`       varchar(2)   NULL DEFAULT '0' COMMENT '用户类型（1 管理员，0 普通用户）',
    `email`           varchar(50)  NULL DEFAULT '' COMMENT '用户邮箱',
    `phonenumber`     varchar(11)  NULL DEFAULT '' COMMENT '手机号码',
    `sex`             char(1)      NULL DEFAULT '0' COMMENT '用户性别（0男 1女 2未知）',
    `avatar`          varchar(100) NULL DEFAULT '' COMMENT '头像地址',
    `password`        varchar(100) NULL DEFAULT '' COMMENT '密码',
    `status`          char(1)      NULL DEFAULT '0' COMMENT '账号状态（0正常 1停用）',
    `del_flag`        char(1)      NULL DEFAULT '0' COMMENT '删除标志（0代表存在 1代表删除）',
    `login_ip`        varchar(128) NULL DEFAULT '' COMMENT '最后登录IP',
    `login_date`      datetime     NULL DEFAULT NULL COMMENT '最后登录时间',
    `pwd_update_date` datetime     NULL DEFAULT NULL COMMENT '密码最后更新时间',
    `create_by`       varchar(64)  NULL DEFAULT '' COMMENT '创建者',
    `create_time`     datetime     NULL DEFAULT NULL COMMENT '创建时间',
    `update_by`       varchar(64)  NULL DEFAULT '' COMMENT '更新者',
    `update_time`     datetime     NULL DEFAULT NULL COMMENT '更新时间',
    `remark`          varchar(500) NULL DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1 COMMENT = '用户信息表';

