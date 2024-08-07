/*
 Navicat Premium Data Transfer

 Source Server         : mysql
 Source Server Type    : MySQL
 Source Server Version : 80034 (8.0.34)
 Source Host           : localhost:3306
 Source Schema         : xunhang

 Target Server Type    : MySQL
 Target Server Version : 80034 (8.0.34)
 File Encoding         : 65001

 Date: 07/08/2024 15:30:53
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for announcement
-- ----------------------------
DROP TABLE IF EXISTS `announcement`;
CREATE TABLE `announcement`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `content` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '公告内容',
  `create_time` datetime NULL DEFAULT NULL COMMENT '发布时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '公告表，只读取前三条' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of announcement
-- ----------------------------
INSERT INTO `announcement` VALUES (1, '发布信息需要登录噢', '2023-11-04 00:00:00');
INSERT INTO `announcement` VALUES (2, '登录仅需昵称与头像', '2023-11-04 00:00:00');
INSERT INTO `announcement` VALUES (3, '有问题联系vx:18607048856', '2023-11-04 00:00:00');

-- ----------------------------
-- Table structure for comment
-- ----------------------------
DROP TABLE IF EXISTS `comment`;
CREATE TABLE `comment`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL COMMENT '评论用户id\r\n',
  `content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '评论内容',
  `create_time` datetime NOT NULL COMMENT '发布时间',
  `item_id` bigint NULL DEFAULT NULL,
  `like_count` int NULL DEFAULT 0 COMMENT '点赞数',
  `reply_user_id` bigint NULL DEFAULT NULL COMMENT '回复用户id',
  `parent_id` bigint NULL DEFAULT NULL COMMENT '父评论id',
  `is_delete` tinyint NOT NULL COMMENT '是否删除 0 否 1 是',
  `update_time` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `comment__index`(`item_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of comment
-- ----------------------------
INSERT INTO `comment` VALUES (1, 1, '我好像在哪见过', '2024-03-24 10:15:15', 20, 0, NULL, NULL, 0, NULL);
INSERT INTO `comment` VALUES (2, 1, '我在A栋看到了', '2024-03-24 10:17:18', 20, 0, NULL, NULL, 0, NULL);
INSERT INTO `comment` VALUES (3, 1, '你好', '2024-03-24 13:24:51', 27, 0, NULL, NULL, 0, NULL);
INSERT INTO `comment` VALUES (4, 3, 'commenttest', '2024-03-26 19:21:21', 20, 0, NULL, NULL, 0, NULL);
INSERT INTO `comment` VALUES (5, 4, '1', '2024-04-25 11:59:12', 20, 0, NULL, NULL, 0, NULL);
INSERT INTO `comment` VALUES (6, 2, '1', '2024-04-25 12:52:53', 20, 0, NULL, NULL, 0, NULL);

-- ----------------------------
-- Table structure for im_friend
-- ----------------------------
DROP TABLE IF EXISTS `im_friend`;
CREATE TABLE `im_friend`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `user_id` bigint NOT NULL COMMENT '用户id',
  `friend_id` bigint NOT NULL COMMENT '好友id',
  `friend_nickname` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '好友昵称',
  `friend_head_image` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT '' COMMENT '好友头像',
  `created_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_friend_id`(`friend_id` ASC) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '好友' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of im_friend
-- ----------------------------
INSERT INTO `im_friend` VALUES (3, 1, 2, '雪净痕', 'https://xunhang.oss-cn-beijing.aliyuncs.com/lovely..jpg', '2024-05-28 22:08:20');
INSERT INTO `im_friend` VALUES (4, 2, 1, '风清默', 'http://121.41.27.33:9001/xunhang/headImage/20240806/1722930676116.jpg', '2024-05-28 22:08:20');

-- ----------------------------
-- Table structure for im_private_message
-- ----------------------------
DROP TABLE IF EXISTS `im_private_message`;
CREATE TABLE `im_private_message`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `send_id` bigint NOT NULL COMMENT '发送用户id',
  `recv_id` bigint NOT NULL COMMENT '接收用户id',
  `content` text CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL COMMENT '发送内容',
  `type` tinyint(1) NOT NULL COMMENT '消息类型 0:文字 1:图片 2:文件 3:语音 10:系统提示',
  `status` tinyint(1) NOT NULL COMMENT '状态 0:未读 1:已读 2:撤回',
  `send_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '发送时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_send_recv_id`(`send_id` ASC, `recv_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '私聊消息' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of im_private_message
-- ----------------------------
INSERT INTO `im_private_message` VALUES (1, 1, 2, '你好', 0, 3, '2024-05-28 11:21:14');
INSERT INTO `im_private_message` VALUES (2, 1, 2, '你好', 0, 3, '2024-05-28 22:31:25');
INSERT INTO `im_private_message` VALUES (3, 2, 1, '你好', 0, 3, '2024-05-28 22:38:56');
INSERT INTO `im_private_message` VALUES (4, 1, 2, '你好', 0, 0, '2024-08-07 13:40:48');
INSERT INTO `im_private_message` VALUES (5, 1, 2, '你好', 0, 0, '2024-08-07 13:41:13');
INSERT INTO `im_private_message` VALUES (6, 1, 2, '你好', 0, 0, '2024-08-07 13:48:11');
INSERT INTO `im_private_message` VALUES (7, 1, 2, '你好', 0, 0, '2024-08-07 14:50:41');
INSERT INTO `im_private_message` VALUES (8, 1, 2, '你好', 0, 0, '2024-08-07 14:50:51');
INSERT INTO `im_private_message` VALUES (9, 1, 2, '你好', 0, 0, '2024-08-07 14:54:42');
INSERT INTO `im_private_message` VALUES (10, 2, 1, '1', 0, 0, '2024-08-07 14:56:53');
INSERT INTO `im_private_message` VALUES (11, 2, 1, '1\n2', 0, 0, '2024-08-07 14:57:22');
INSERT INTO `im_private_message` VALUES (12, 2, 1, '3\n\n\n\n\n\n', 0, 0, '2024-08-07 14:57:30');

-- ----------------------------
-- Table structure for item
-- ----------------------------
DROP TABLE IF EXISTS `item`;
CREATE TABLE `item`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT,
  `publisher_id` bigint NOT NULL COMMENT '发布者id',
  `description` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '描述',
  `location` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '地址',
  `date` date NULL DEFAULT NULL COMMENT '丢失日期或拾取日期',
  `category` tinyint(1) NOT NULL COMMENT '0:丢失的物品,1:拾取的物品',
  `claimer_id` bigint NULL DEFAULT 0,
  `tag` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '',
  `title` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '',
  `create_time` datetime NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT NULL,
  `is_success` tinyint(1) NULL DEFAULT 0 COMMENT '0未完成 1完成',
  `is_delete` tinyint(1) NULL DEFAULT 0 COMMENT '是否删除 0未删 1已删',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `item_fk1`(`publisher_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 67 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of item
-- ----------------------------
INSERT INTO `item` VALUES (20, 1, 'aa', 'aaa', '2023-12-20', 0, 0, '其他', '测试一张图片', '2023-12-20 20:17:08', '2023-12-20 20:17:08', 0, 0);
INSERT INTO `item` VALUES (25, 1, 'aa', NULL, NULL, 1, 0, '其他', 'aaa', '2023-12-20 21:05:17', '2023-12-20 21:05:17', 0, 0);
INSERT INTO `item` VALUES (26, 1, '高数a，绿色', 'a栋', '2023-12-21', 1, 0, '书本', '丢了本书', '2023-12-21 16:01:42', '2023-12-21 16:01:42', 0, 0);
INSERT INTO `item` VALUES (27, 1, '蓝色', 'a栋', '2023-12-21', 0, 0, '水杯', '丢了个水杯', '2023-12-21 16:03:59', '2023-12-21 16:03:59', 0, 0);
INSERT INTO `item` VALUES (28, 1, '橙色', 'a栋', '2023-12-21', 0, 0, '耳机', '丢了耳机', '2023-12-21 16:07:10', '2023-12-21 16:07:10', 0, 0);
INSERT INTO `item` VALUES (63, 1, '1', '', NULL, 1, 0, '其他', '嗄', '2024-07-09 22:43:22', '2024-07-09 22:43:22', 0, 0);
INSERT INTO `item` VALUES (66, 1, '1', '', NULL, 1, 0, '耳机', 'end test', '2024-08-07 15:11:29', '2024-08-07 15:11:29', 0, 0);

-- ----------------------------
-- Table structure for item_image
-- ----------------------------
DROP TABLE IF EXISTS `item_image`;
CREATE TABLE `item_image`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT,
  `image_url` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `item_id` bigint NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 28 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of item_image
-- ----------------------------
INSERT INTO `item_image` VALUES (1, 'https://xunhang.oss-cn-beijing.aliyuncs.com/itemImage/2023-12-20/1703074628015', 20);
INSERT INTO `item_image` VALUES (2, 'https://xunhang.oss-cn-beijing.aliyuncs.com/itemImage/2024-03-27/1711508012236', 36);
INSERT INTO `item_image` VALUES (3, 'https://xunhang.oss-cn-beijing.aliyuncs.com/itemImage/2024-03-27/1711508649611', 37);
INSERT INTO `item_image` VALUES (4, 'https://xunhang.oss-cn-beijing.aliyuncs.com/itemImage/2024-03-30/1711808453799', 38);
INSERT INTO `item_image` VALUES (5, 'https://xunhang.oss-cn-beijing.aliyuncs.com/itemImage/2024-03-30/1711809496391', 39);
INSERT INTO `item_image` VALUES (6, 'https://xunhang.oss-cn-beijing.aliyuncs.com/itemImage/2024-04-11/1712828679532', 40);
INSERT INTO `item_image` VALUES (7, 'https://xunhang.oss-cn-beijing.aliyuncs.com/itemImage/2024-04-25/1714046276460', 42);
INSERT INTO `item_image` VALUES (8, 'https://xunhang.oss-cn-beijing.aliyuncs.com/itemImage/2024-04-27/1714223712940', 46);
INSERT INTO `item_image` VALUES (9, 'https://xunhang.oss-cn-beijing.aliyuncs.com/itemImage/2024-04-27/1714224208642', 47);
INSERT INTO `item_image` VALUES (10, 'https://xunhang.oss-cn-beijing.aliyuncs.com/itemImage/2024-04-27/1714224455912', 48);
INSERT INTO `item_image` VALUES (13, 'https://xunhang.oss-cn-beijing.aliyuncs.com/itemImage/2023-12-20/1703077508884', 25);
INSERT INTO `item_image` VALUES (14, 'https://xunhang.oss-cn-beijing.aliyuncs.com/itemImage/2023-12-20/1703077514019', 25);
INSERT INTO `item_image` VALUES (15, 'https://xunhang.oss-cn-beijing.aliyuncs.com/itemImage/2023-12-20/1703077516161', 25);
INSERT INTO `item_image` VALUES (16, 'https://xunhang.oss-cn-beijing.aliyuncs.com/itemImage/2023-12-21/1703145701352', 26);
INSERT INTO `item_image` VALUES (17, 'https://xunhang.oss-cn-beijing.aliyuncs.com/itemImage/2023-12-21/1703145837676', 27);
INSERT INTO `item_image` VALUES (18, 'https://xunhang.oss-cn-beijing.aliyuncs.com/itemImage/2023-12-21/1703146028718', 28);
INSERT INTO `item_image` VALUES (19, 'https://xunhang.oss-cn-beijing.aliyuncs.com/itemImage/2023-12-21/1703146349325', 29);
INSERT INTO `item_image` VALUES (20, 'http://tmp/KXRNnxCmSDEZ1051e854dd12d3617204b31f85f5b4e9.jpg', 50);
INSERT INTO `item_image` VALUES (21, 'http://tmp/wWC2fkJ3CFwP1051e854dd12d3617204b31f85f5b4e9.jpg', 53);
INSERT INTO `item_image` VALUES (22, 'http://tmp/BG4bh2RB6JgD1051e854dd12d3617204b31f85f5b4e9.jpg', 54);
INSERT INTO `item_image` VALUES (23, 'http://tmp/7qTiwuI9MuIv1051e854dd12d3617204b31f85f5b4e9.jpg', 55);
INSERT INTO `item_image` VALUES (24, 'http://127.0.0.1:9000/xunhang/itemImage/20240709/1720536261681.jpg', 64);
INSERT INTO `item_image` VALUES (25, 'http://127.0.0.1:9000/xunhang/itemImage/20240709/1720536261681.jpg', 64);
INSERT INTO `item_image` VALUES (26, 'https://xunhang.oss-cn-beijing.aliyuncs.com/itemImage/2024-08-07/1723014686830.jpg', 66);
INSERT INTO `item_image` VALUES (27, 'https://xunhang.oss-cn-beijing.aliyuncs.com/itemImage/2024-08-07/1723014686830.jpg', 66);

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT,
  `openid` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `nickname` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `head_image` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '',
  `create_time` datetime NULL DEFAULT (now()),
  `update_time` datetime NULL DEFAULT NULL,
  `type` smallint NULL DEFAULT 1 COMMENT '1 用户 2 管理员',
  `head_image_thumb` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '',
  `last_login_time` datetime NULL DEFAULT NULL,
  `username` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `is_del` tinyint(1) NULL DEFAULT 0 COMMENT '0未删除 1已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, 'og86F5H4reYY4kDCH0M8YlwGsi0Q', '风清默', NULL, '$2a$10$KrPDFTdEf7GK/UQ/GUCKEOi5OfizXxTH3N3EVFlW75u.MkH0AXXTO', 'http://121.41.27.33:9001/xunhang/headImage/20240806/1722930675518.jpg', '2024-06-04 16:09:20', '2024-06-07 17:12:36', 1, 'http://121.41.27.33:9001/xunhang/headImage/20240806/1722930676116.jpg', NULL, '风清默', 0);
INSERT INTO `user` VALUES (2, '1', '雪净痕', NULL, '$2a$10$KrPDFTdEf7GK/UQ/GUCKEOi5OfizXxTH3N3EVFlW75u.MkH0AXXTO', 'https://xunhang.oss-cn-beijing.aliyuncs.com/lovely..jpg', '2024-03-25 11:39:39', '2024-03-25 11:39:42', 0, 'https://xunhang.oss-cn-beijing.aliyuncs.com/lovely..jpg', NULL, '雪净痕', 0);
INSERT INTO `user` VALUES (3, 'og86F5KucnqP0cTJ57KnEA7QZD6E', '测试号1', NULL, '$2a$10$KrPDFTdEf7GK/UQ/GUCKEOi5OfizXxTH3N3EVFlW75u.MkH0AXXTO', 'https://xunhang.oss-cn-beijing.aliyuncs.com/avatar/default_avatar/avatar3.jpg', '2024-03-26 18:12:19', '2024-03-26 18:12:19', 0, '', NULL, NULL, 0);
INSERT INTO `user` VALUES (5, NULL, '无敌风火轮', NULL, '$2a$10$f9jSlA0DJj/7jzUpI3dNHepE7Ds/fbP1STwbx4zOGQpouP87RfQ6K', 'https://xunhang.oss-cn-beijing.aliyuncs.com/avatar/default_avatar/avatar2.jpg', '2024-08-04 16:18:13', '2024-08-04 16:18:13', 1, 'https://xunhang.oss-cn-beijing.aliyuncs.com/avatar/default_avatar/avatar2.jpg', NULL, '123456', NULL);

SET FOREIGN_KEY_CHECKS = 1;
