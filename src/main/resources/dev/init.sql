DROP DATABASE IF EXISTS BLACK_SERVER;
CREATE DATABASE BLACK_SERVER;
USE BLACK_SERVER;

CREATE TABLE T_IMAGE (
  `id`    BIGINT PRIMARY KEY AUTO_INCREMENT,
  `data`  LONGBLOB,
  `flags` INT(32),
  `uid`   INT(32),
  `gid`   BIGINT(64),
  `hash`  VARCHAR(128),
  `used`  BOOLEAN
);

CREATE TABLE T_COLLEGE (
  `id`       INT PRIMARY KEY AUTO_INCREMENT,
  `name`     VARCHAR(30),
  `name_ext` VARCHAR(30),
  `logo_id`  BIGINT,
  `location` VARCHAR(200),
  FOREIGN KEY (`logo_id`) REFERENCES T_IMAGE (id)
);

CREATE TABLE T_ACADEMY (
  `id`         INT PRIMARY KEY AUTO_INCREMENT,
  `name`       VARCHAR(30),
  `college_id` INT,
  `logo_id`    BIGINT,
  FOREIGN KEY (`logo_id`) REFERENCES T_IMAGE (id)
);

CREATE TABLE T_GAME (
  `id`      INT PRIMARY KEY AUTO_INCREMENT,
  `name`    VARCHAR(30),
  `logo_id` BIGINT,
  FOREIGN KEY (`logo_id`) REFERENCES T_IMAGE (id)
);

CREATE TABLE T_PAGE (
  `id` INT PRIMARY KEY AUTO_INCREMENT
);

CREATE TABLE T_GROUP (
  `id`      BIGINT PRIMARY KEY AUTO_INCREMENT,
  `name`    VARCHAR(50),
  `intro`   VARCHAR(200),
  `logo_id` BIGINT,
  `page_id` INT,
  FOREIGN KEY (`page_id`) REFERENCES T_PAGE (id),
  FOREIGN KEY (`logo_id`) REFERENCES T_IMAGE (id)
);

CREATE TABLE T_USER (
  `id`                  INT PRIMARY KEY AUTO_INCREMENT,
  `phone`               VARCHAR(20),
  `email`               VARCHAR(100),
  `realname`            VARCHAR(20),
  `idcard`              VARCHAR(18),
  `enabled`             BOOLEAN,
  `gender`              ENUM('MALE', 'FEMALE', 'SECRET'),
  `college_id`          INT,
  `academy_id`          INT,
  `avatar_id`           BIGINT,
  `birthday`            DATE,
  `reg_time`            TIMESTAMP,
  `reg_ip`              VARCHAR(39),
  `reg_longitude`       DOUBLE PRECISION(9, 6),
  `reg_latitude`        DOUBLE PRECISION(9, 6),
  `username`            VARCHAR(20),
  `signature`           VARCHAR(255),
  `hometown`            VARCHAR(40),
  `highschool`          VARCHAR(40),
  `grade`               VARCHAR(20),
  `background_image_id` BIGINT,
  FOREIGN KEY (`college_id`) REFERENCES T_COLLEGE (id),
  FOREIGN KEY (`academy_id`) REFERENCES T_ACADEMY (id),
  FOREIGN KEY (`background_image_id`) REFERENCES T_IMAGE (id)
);

CREATE TABLE T_ACTIVITY (
  `id`                    INT PRIMARY KEY AUTO_INCREMENT,
  `cover_image_id`        BIGINT,
  `start_time`            DATETIME,
  `end_time`              DATETIME,
  `registration_deadline` DATETIME,
  `location`              VARCHAR(100),
  `promoter_id`           INT,
  `title`                 TEXT,
  `content`               TEXT,
  `game_id`               INT,
  `type`                  ENUM('MATCH', 'BLACK'),
  `status`                ENUM('READY', 'RUNNING', 'STOPPED'),
  `group_id`              BIGINT,
  FOREIGN KEY (`promoter_id`) REFERENCES T_USER (id),
  FOREIGN KEY (`group_id`) REFERENCES T_GROUP (id),
  FOREIGN KEY (`cover_image_id`) REFERENCES T_IMAGE (id),
  FOREIGN KEY (`game_id`) REFERENCES T_GAME (id)
);

CREATE TABLE T_ACTIVITY_IMAGE (
  `activity_id` INT,
  `image_id`    BIGINT,
  FOREIGN KEY (`activity_id`) REFERENCES T_ACTIVITY (id),
  FOREIGN KEY (`image_id`) REFERENCES T_IMAGE (id)
);

CREATE TABLE T_FRIENDSHIP (
  `id`           INT PRIMARY KEY AUTO_INCREMENT,
  `user_id`      INT,
  `friend_id`    INT,
  `friend_alias` VARCHAR(30),
  FOREIGN KEY (`user_id`) REFERENCES T_USER (id),
  FOREIGN KEY (`friend_id`) REFERENCES T_USER (id)
);

CREATE TABLE T_SUBSCRIPTION (
  `broadcaster_id` INT,
  `subscriber_id`  INT,
  FOREIGN KEY (`broadcaster_id`) REFERENCES T_USER (id),
  FOREIGN KEY (`subscriber_id`) REFERENCES T_USER (id)
);

CREATE TABLE T_MEMBERSHIP (
  `id`          INT PRIMARY KEY AUTO_INCREMENT,
  `group_id`    BIGINT,
  `member_id`   INT,
  `type`        ENUM('MEMBER', 'OP', 'SPEAKER'),
  `nickname`    VARCHAR(30),
  `group_alias` VARCHAR(50),
  FOREIGN KEY (`group_id`) REFERENCES T_GROUP (id),
  FOREIGN KEY (`member_id`) REFERENCES T_USER (id)
);

CREATE TABLE T_MESSAGE (
  `id`             BIGINT PRIMARY KEY AUTO_INCREMENT,
  `content`        VARCHAR(1000),
  `sender_id`      INT,
  `sender_time`    TIMESTAMP,
  `visibility`     INT,
  `visibility_ext` VARCHAR(200),
  `extenstion`     VARCHAR(200),
  FOREIGN KEY (`sender_id`) REFERENCES T_USER (id)
);

CREATE TABLE T_NETBAR (
  `id`       INT PRIMARY KEY AUTO_INCREMENT,
  `name`     VARCHAR(30),
  `logo_id`  BIGINT,
  `location` VARCHAR(200),
  FOREIGN KEY (`logo_id`) REFERENCES T_IMAGE (id)
);

# 插入数据
#学校信息
INSERT INTO `black_server`.`t_college`
(`id`, `name`, `name_ext`, `location`)
VALUES
  ('1', '上海大学', '宝山校区', '上海市宝山区');

INSERT INTO `black_server`.`t_academy`
(`id`, `name`, `college_id`)
VALUES
  ('1', '计算机工程与科学学院', '1');

#用户信息
INSERT INTO `black_server`.`t_user`
(`id`, `phone`, `email`, `username`, `realname`, `idcard`, `enabled`, `gender`, `college_id`, `academy_id`, `birthday`, `reg_time`, `reg_ip`, `signature`, `hometown`, `highschool`, `grade`)
VALUES
  ('1', '123456789', 'test@test.com', '王尼玛', '王尼玛', '123456789', '0', 'MALE', '1', '1', '2000-01-01',
   '2001-01-02:03:45:01', '127.0.0.1', '今天没吃药感觉自己萌萌哒', '上海', '暴走高中', '研究生一年级');
INSERT INTO `black_server`.`t_user`
(`id`, `phone`, `email`, `username`, `realname`, `idcard`, `enabled`, `gender`, `college_id`, `academy_id`, `birthday`, `reg_time`, `reg_ip`, `signature`, `hometown`, `highschool`, `grade`)
VALUES
  ('2', '10000000', 'test@test.com', '东仙队长', '东仙队长', '123456789', '0', 'MALE', '1', '1', '2000-01-01',
   '2001-01-02:03:45:01', '127.0.0.1', '我要金坷垃，非洲农业不发达，必须要有金坷垃', '上海', '暴走高中', '研究生一年级');
INSERT INTO `black_server`.`t_user`
(`id`, `phone`, `email`, `username`, `realname`, `idcard`, `enabled`, `gender`, `college_id`, `academy_id`, `birthday`, `reg_time`, `reg_ip`, `signature`, `hometown`, `highschool`, `grade`)
VALUES
  ('3', '10000001', 'test@test.com', '德国Boy', '德国Boy', '123456789', '0', 'MALE', '1', '1', '2000-01-01',
   '2001-01-02:03:45:01', '127.0.0.1', '我练功发自真心', '上海', '暴走高中', '研究生一年级');
INSERT INTO `black_server`.`t_user`
(`id`, `phone`, `email`, `username`, `realname`, `idcard`, `enabled`, `gender`, `college_id`, `academy_id`, `birthday`, `reg_time`, `reg_ip`, `signature`, `hometown`, `highschool`, `grade`)
VALUES
  ('4', '10000002', 'test@test.com', '成龙', '成龙', '123456789', '0', 'MALE', '1', '1', '2000-01-01',
   '2001-01-02:03:45:01', '127.0.0.1', 'duang duang duang', '上海', '暴走高中', '研究生一年级');
INSERT INTO `black_server`.`t_user`
(`id`, `phone`, `email`, `username`, `realname`, `idcard`, `enabled`, `gender`, `college_id`, `academy_id`, `birthday`, `reg_time`, `reg_ip`, `signature`, `hometown`, `highschool`, `grade`)
VALUES
  ('5', '10000003', 'test@test.com', '大力哥', '大力哥', '123456789', '0', 'MALE', '1', '1', '2000-01-01',
   '2001-01-02:03:45:01', '127.0.0.1', '一天不喝，浑身难受', '上海', '暴走高中', '研究生一年级');
INSERT INTO `black_server`.`t_user`
(`id`, `phone`, `email`, `username`, `realname`, `idcard`, `enabled`, `gender`, `college_id`, `academy_id`, `birthday`, `reg_time`, `reg_ip`, `signature`, `hometown`, `highschool`, `grade`)
VALUES
  ('6', '10000004', 'test@test.com', '尔康', '尔康', '123456789', '0', 'MALE', '1', '1', '2000-01-01',
   '2001-01-02:03:45:01', '127.0.0.1', '紫薇，等一下', '上海', '暴走高中', '研究生一年级');
INSERT INTO `black_server`.`t_user`
(`id`, `phone`, `email`, `username`, `realname`, `idcard`, `enabled`, `gender`, `college_id`, `academy_id`, `birthday`, `reg_time`, `reg_ip`, `signature`, `hometown`, `highschool`, `grade`)
VALUES
  ('7', '10000005', 'test@test.com', '葛炮', '葛炮', '123456789', '0', 'MALE', '1', '1', '2000-01-01',
   '2001-01-02:03:45:01', '127.0.0.1', '看！人群中突然钻出一个光头', '上海', '暴走高中', '研究生一年级');
INSERT INTO `black_server`.`t_user`
(`id`, `phone`, `email`, `username`, `realname`, `idcard`, `enabled`, `gender`, `college_id`, `academy_id`, `birthday`, `reg_time`, `reg_ip`, `signature`, `hometown`, `highschool`, `grade`)
VALUES
  ('8', '10000006', 'test@test.com', '元首', '元首', '123456789', '0', 'MALE', '1', '1', '2000-01-01',
   '2001-01-02:03:45:01', '127.0.0.1', '渣渣', '上海', '暴走高中', '研究生一年级');
INSERT INTO `black_server`.`t_user`
(`id`, `phone`, `email`, `username`, `realname`, `idcard`, `enabled`, `gender`, `college_id`, `academy_id`, `birthday`, `reg_time`, `reg_ip`, `signature`, `hometown`, `highschool`, `grade`)
VALUES
  ('9', '10000007', 'test@test.com', '金馆长', '金馆长', '123456789', '0', 'MALE', '1', '1', '2000-01-01',
   '2001-01-02:03:45:01', '127.0.0.1', '哈哈哈', '上海', '暴走高中', '研究生一年级');
INSERT INTO `black_server`.`t_user`
(`id`, `phone`, `email`, `username`, `realname`, `idcard`, `enabled`, `gender`, `college_id`, `academy_id`, `birthday`, `reg_time`, `reg_ip`, `signature`, `hometown`, `highschool`, `grade`)
VALUES
  ('10', '10000008', 'test@test.com', '小鬼子', '小鬼子', '123456789', '0', 'MALE', '1', '1', '2000-01-01',
   '2001-01-02:03:45:01', '127.0.0.1', '我要金坷垃，日本资源太缺乏，必须要有金坷垃', '上海', '暴走高中', '研究生一年级');


#游戏信息
INSERT INTO `black_server`.`t_game` (`id`, `name`) VALUES ('1', 'Counter Strike');
INSERT INTO `black_server`.`t_game` (`id`, `name`) VALUES ('2', 'Dota 2');
INSERT INTO `black_server`.`t_game` (`id`, `name`) VALUES ('3', 'Hearthstone');
INSERT INTO `black_server`.`t_game` (`id`, `name`) VALUES ('4', 'League of Legends');
INSERT INTO `black_server`.`t_game` (`id`, `name`) VALUES ('5', 'Minecraft');
INSERT INTO `black_server`.`t_game` (`id`, `name`) VALUES ('6', 'StarCraft II');
INSERT INTO `black_server`.`t_game` (`id`, `name`) VALUES ('7', 'Warcraft III');

#群组页面信息
INSERT INTO `black_server`.`t_page` (`id`) VALUES ('1');

#群组信息
INSERT INTO `black_server`.`t_group` (`id`, `name`, `intro`, `page_id`) VALUES ('1', '起来嗨', '睡你麻痹起来嗨', '1');

#活动信息
INSERT INTO `black_server`.`t_activity` (`id`, `title`, `content`, `start_time`, `end_time`, `registration_deadline`, `promoter_id`, `location`, `type`, `status`, `group_id`)
VALUES ('1', '起来嗨', '睡你麻痹起来嗨', '2001-02-03 01:02:03', '2099-11-11 11:11:11', '2099-01-01 11:11:11', '1', '埃及', 'MATCH',
        'RUNNING', '1');
INSERT INTO `black_server`.`t_activity` (`id`, `title`, `content`, `start_time`, `location`, `type`, `status`)
VALUES ('2', '起来嗨', '睡你麻痹起来嗨', '2001-02-03 01:03:04', '埃及', 'MATCH', 'RUNNING');
INSERT INTO `black_server`.`t_activity` (`id`, `title`, `content`, `start_time`, `location`, `type`, `status`)
VALUES ('3', '起来嗨', '睡你麻痹起来嗨', '2001-02-03 01:04:05', '埃及', 'MATCH', 'RUNNING');
INSERT INTO `black_server`.`t_activity` (`id`, `title`, `content`, `start_time`, `location`, `type`, `status`)
VALUES ('4', '起来嗨', '睡你麻痹起来嗨', '2001-02-03 01:05:06', '埃及', 'MATCH', 'RUNNING');
INSERT INTO `black_server`.`t_activity` (`id`, `title`, `content`, `start_time`, `location`, `promoter_id`, `type`, `status`)
VALUES ('5', '起来嗨', '睡你麻痹起来嗨', '2001-02-03 01:06:07', '埃及', '1', 'MATCH', 'RUNNING');
INSERT INTO `black_server`.`t_activity` (`id`, `title`, `content`, `start_time`, `location`, `type`, `status`)
VALUES ('6', '起来嗨', '睡你麻痹起来嗨', '2001-02-03 01:07:08', '埃及', 'MATCH', 'RUNNING');
INSERT INTO `black_server`.`t_activity` (`id`, `title`, `content`, `start_time`, `location`, `type`, `status`)
VALUES ('7', '起来嗨', '睡你麻痹起来嗨', '2001-02-03 01:08:09', '埃及', 'MATCH', 'RUNNING');
INSERT INTO `black_server`.`t_activity` (`id`, `title`, `content`, `start_time`, `location`, `type`, `status`)
VALUES ('8', '起来嗨', '睡你麻痹起来嗨', '2001-02-03 01:09:10', '埃及', 'MATCH', 'RUNNING');
INSERT INTO `black_server`.`t_activity` (`id`, `title`, `content`, `start_time`, `location`, `type`, `status`)
VALUES ('9', '起来嗨', '睡你麻痹起来嗨', '2001-02-03 01:10:11', '埃及', 'MATCH', 'RUNNING');
INSERT INTO `black_server`.`t_activity` (`id`, `title`, `content`, `start_time`, `location`, `type`, `status`)
VALUES ('10', '起来嗨', '睡你麻痹起来嗨', '2001-02-03 01:11:12', '埃及', 'MATCH', 'RUNNING');
