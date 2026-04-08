-- 用户表
CREATE TABLE `sys_user`
(
    `id`              bigint       NOT NULL AUTO_INCREMENT,
    `wx_mp_openid`    varchar(64)  null comment '微信小程序 openid',
    `user_name`       varchar(30)  NULL COMMENT '用户账号',
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
-- 用户表扩展字段
ALTER TABLE `sys_user`
    ADD COLUMN `birthday`           date  NULL COMMENT '出生日期' AFTER `avatar`,
    ADD COLUMN `height`             float NULL COMMENT '身高(cm)' AFTER `birthday`,
    ADD COLUMN `weight`             float NULL COMMENT '体重(kg)' AFTER `height`,
    ADD COLUMN `first_measure_date` date  NULL COMMENT '首次检测日期' AFTER `weight`,
    ADD COLUMN `total_measures`     int   NULL DEFAULT 0 COMMENT '累计检测次数' AFTER `first_measure_date`;

alter table sys_user
    add unique index unikey (user_name, wx_mp_openid) comment '用户名或微信唯一索引';
-- 设备信息表
CREATE TABLE `t_device`
(
    `id`               bigint       NOT NULL AUTO_INCREMENT COMMENT '主键 ID',
    `device_sn`        varchar(64)  NOT NULL COMMENT '设备 SN 码',
    `mac`              varchar(32)  NULL DEFAULT NULL COMMENT 'MAC 地址',
    `firmware_version` varchar(32)  NULL DEFAULT NULL COMMENT '固件版本',
    `hardware_version` varchar(32)  NULL DEFAULT NULL COMMENT '硬件版本',
    `batch_no`         varchar(64)  NULL DEFAULT NULL COMMENT '生产批次',
    `activated_at`     datetime     NULL DEFAULT NULL COMMENT '激活时间（首次绑定）',
    `last_used_at`     datetime     NULL DEFAULT NULL COMMENT '最后使用时间',
    `total_uses`       int          NULL DEFAULT 0 COMMENT '累计使用次数',
    `status`           char(1)      NULL DEFAULT '0' COMMENT '设备状态（0 正常 1 禁用）',
    `create_by`        varchar(64)  NULL DEFAULT '' COMMENT '创建者',
    `create_time`      datetime     NULL DEFAULT NULL COMMENT '创建时间',
    `update_by`        varchar(64)  NULL DEFAULT '' COMMENT '更新者',
    `update_time`      datetime     NULL DEFAULT NULL COMMENT '更新时间',
    `remark`           varchar(500) NULL DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `uk_device_sn` (`device_sn`) USING BTREE COMMENT '设备 SN 唯一索引',
    INDEX `idx_last_used_at` (`last_used_at`) USING BTREE COMMENT '最后使用时间索引'
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
    COMMENT = '设备信息表';


-- 用户设备绑定关系表
CREATE TABLE `t_user_device`
(
    `id`          bigint   NOT NULL AUTO_INCREMENT COMMENT '主键 ID',
    `user_id`     bigint   NOT NULL COMMENT '用户 ID',
    `device_id`   bigint   NOT NULL COMMENT '设备 ID',
    `bind_time`   datetime NULL DEFAULT NULL COMMENT '绑定时间',
    `unbind_time` datetime NULL DEFAULT NULL COMMENT '解绑时间',
    `is_active`   tinyint  NULL DEFAULT 1 COMMENT '是否当前绑定（1 是 0 否）',
    `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
    `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `idx_user_id` (`user_id`) USING BTREE COMMENT '用户 ID 索引',
    INDEX `idx_device_id` (`device_id`) USING BTREE COMMENT '设备 ID 索引',
    INDEX `idx_is_active` (`is_active`) USING BTREE COMMENT '活跃绑定索引'
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
    COMMENT = '用户设备绑定关系表';


-- 检测记录表
CREATE TABLE `t_record`
(
    `id`               bigint       NOT NULL AUTO_INCREMENT COMMENT '主键 ID',
    `record_id`        varchar(64)  NOT NULL COMMENT '客户端生成的唯一记录 ID(UUID)',
    `user_id`          bigint       NOT NULL COMMENT '用户 ID',
    `device_sn`        varchar(64)  NOT NULL COMMENT '设备 SN',
    `timestamp`        datetime     NOT NULL COMMENT '检测时间',
    `scene_type`       tinyint      NULL DEFAULT 0 COMMENT '场景标签（0 未选 1 空腹 2 餐后 3 运动后）',
    `concentration`    float        NULL DEFAULT NULL COMMENT '浓度值 (ppb)',
    `level`            tinyint      NULL DEFAULT NULL COMMENT '等级（1-5）',
    `level_label`      varchar(32)  NULL DEFAULT NULL COMMENT '等级文案（如"低风险"）',
    `suggestion`       text         NULL DEFAULT NULL COMMENT '建议文案',
    `r_base`           float        NULL DEFAULT NULL COMMENT '基线电阻',
    `r_gas`            float        NULL DEFAULT NULL COMMENT '响应电阻',
    `adc_value`        int          NULL DEFAULT NULL COMMENT 'ADC 采样值',
    `temperature`      float        NULL DEFAULT NULL COMMENT '环境温度 (℃)',
    `humidity`         float        NULL DEFAULT NULL COMMENT '环境湿度 (%RH)',
    `heater_temp`      float        NULL DEFAULT NULL COMMENT '加热片温度 (℃)',
    `algo_version`     varchar(32)  NULL DEFAULT NULL COMMENT '算法版本',
    `firmware_version` varchar(32)  NULL DEFAULT NULL COMMENT '固件版本',
    `gas_type`         varchar(32)  NULL DEFAULT NULL COMMENT '气体类型（如 H2）',
    `extra_data`       json         NULL DEFAULT NULL COMMENT '扩展字段',
    `del_flag`         char(1)      NULL DEFAULT '0' COMMENT '删除标志（0 代表存在 1 代表删除）',
    `create_by`        varchar(64)  NULL DEFAULT '' COMMENT '创建者',
    `create_time`      datetime     NULL DEFAULT NULL COMMENT '记录创建时间',
    `update_by`        varchar(64)  NULL DEFAULT '' COMMENT '更新者',
    `update_time`      datetime     NULL DEFAULT NULL COMMENT '更新时间',
    `remark`           varchar(500) NULL DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `uk_record_id` (`record_id`) USING BTREE COMMENT '记录 ID 唯一索引',
    INDEX `idx_user_id` (`user_id`) USING BTREE COMMENT '用户 ID 索引',
    INDEX `idx_device_sn` (`device_sn`) USING BTREE COMMENT '设备 SN 索引',
    INDEX `idx_timestamp` (`timestamp`) USING BTREE COMMENT '检测时间索引',
    INDEX `idx_scene_type` (`scene_type`) USING BTREE COMMENT '场景类型索引'
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
    COMMENT = '检测记录表';


-- 用户反馈表
CREATE TABLE `t_feedback`
(
    `id`          bigint       NOT NULL AUTO_INCREMENT COMMENT '主键 ID',
    `user_id`     bigint       NOT NULL COMMENT '用户 ID',
    `content`     text         NOT NULL COMMENT '反馈内容',
    `images`      json         NULL DEFAULT NULL COMMENT '图片 URL 列表',
    `reply`       text         NULL DEFAULT NULL COMMENT '管理员回复',
    `reply_time`  datetime     NULL DEFAULT NULL COMMENT '回复时间',
    `reply_by`    varchar(64)  NULL DEFAULT NULL COMMENT '回复人',
    `status`      tinyint      NULL DEFAULT 0 COMMENT '状态（0 待回复 1 已回复）',
    `del_flag`    char(1)      NULL DEFAULT '0' COMMENT '删除标志（0 代表存在 1 代表删除）',
    `create_by`   varchar(64)  NULL DEFAULT '' COMMENT '创建者',
    `create_time` datetime     NULL DEFAULT NULL COMMENT '提交时间',
    `update_by`   varchar(64)  NULL DEFAULT '' COMMENT '更新者',
    `update_time` datetime     NULL DEFAULT NULL COMMENT '更新时间',
    `remark`      varchar(500) NULL DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `idx_user_id` (`user_id`) USING BTREE COMMENT '用户 ID 索引',
    INDEX `idx_status` (`status`) USING BTREE COMMENT '状态索引',
    INDEX `idx_create_time` (`create_time`) USING BTREE COMMENT '提交时间索引'
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
    COMMENT = '用户反馈表';


-- 系统配置表
CREATE TABLE `sys_config`
(
    `id`           bigint       NOT NULL AUTO_INCREMENT COMMENT '主键 ID',
    `config_key`   varchar(64)  NOT NULL COMMENT '配置键',
    `config_value` text         NULL DEFAULT NULL COMMENT '配置值（可为 JSON）',
    `description`  varchar(255) NULL DEFAULT NULL COMMENT '描述',
    `config_type`  char(1)      NULL DEFAULT '0' COMMENT '配置类型（0 普通 1 敏感）',
    `del_flag`     char(1)      NULL DEFAULT '0' COMMENT '删除标志（0 代表存在 1 代表删除）',
    `create_by`    varchar(64)  NULL DEFAULT '' COMMENT '创建者',
    `create_time`  datetime     NULL DEFAULT NULL COMMENT '创建时间',
    `update_by`    varchar(64)  NULL DEFAULT '' COMMENT '更新者',
    `update_time`  datetime     NULL DEFAULT NULL COMMENT '更新时间',
    `remark`       varchar(500) NULL DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `uk_config_key` (`config_key`) USING BTREE COMMENT '配置键唯一索引'
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
    COMMENT = '系统配置表';

-- MinIO 文件上传配置表
CREATE TABLE `sys_minio_file`
(
    `id`            bigint       NOT NULL AUTO_INCREMENT COMMENT '主键 ID',
    `original_name` varchar(255) NULL DEFAULT NULL COMMENT '图片原始文件名称',
    `suffix`        varchar(64)  NULL DEFAULT NULL COMMENT '后缀名称',
    `bucket`        varchar(64)  NULL DEFAULT NULL COMMENT '桶名称',
    `object`        varchar(500) NULL DEFAULT NULL COMMENT '存储地址',
    `object_size`   bigint       NULL DEFAULT NULL COMMENT '文件大小',
    `create_by`     varchar(64)  NULL DEFAULT '' COMMENT '创建者',
    `create_time`   datetime     NULL DEFAULT NULL COMMENT '创建时间',
    `update_by`     varchar(64)  NULL DEFAULT '' COMMENT '更新者',
    `update_time`   datetime     NULL DEFAULT NULL COMMENT '更新时间',
    `remark`        varchar(500) NULL DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `idx_bucket` (`bucket`) USING BTREE COMMENT '桶名称索引',
    INDEX `idx_create_time` (`create_time`) USING BTREE COMMENT '创建时间索引'
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
    COMMENT = 'MinIO 文件上传配置表';

alter table sys_user
    add unique index uk_phone (phonenumber, del_flag) comment '';