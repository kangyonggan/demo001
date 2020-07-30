DROP DATABASE IF EXISTS demo;
CREATE DATABASE demo DEFAULT CHARACTER SET utf8mb4;
USE demo;

-- ----------------------------
--  Table structure for user
-- ----------------------------
DROP TABLE
    IF EXISTS user;

CREATE TABLE user
(
    id           BIGINT(20) PRIMARY KEY AUTO_INCREMENT NOT NULL
        COMMENT 'ID',
    name         VARCHAR(20)                           NOT NULL DEFAULT ''
        COMMENT '姓名'
)
    COMMENT '用户表';

#====================初始数据====================#

-- ----------------------------
--  data for user
-- ----------------------------
INSERT INTO user
    (id, name)
VALUES
    (1, '佩奇'),
    (2, '乔治');
