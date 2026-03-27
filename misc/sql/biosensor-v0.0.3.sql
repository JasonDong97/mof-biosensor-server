-- 用户表扩展字段
ALTER TABLE `sys_user` 
    ADD COLUMN `birthday` date NULL COMMENT '出生日期' AFTER `avatar`,
    ADD COLUMN `height` float NULL COMMENT '身高(cm)' AFTER `birthday`,
    ADD COLUMN `weight` float NULL COMMENT '体重(kg)' AFTER `height`,
    ADD COLUMN `first_measure_date` date NULL COMMENT '首次检测日期' AFTER `weight`,
    ADD COLUMN `total_measures` int NULL DEFAULT 0 COMMENT '累计检测次数' AFTER `first_measure_date`;