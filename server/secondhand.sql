/*
 Navicat Premium Data Transfer

 Source Server         : www.xilin.com
 Source Server Type    : MySQL
 Source Server Version : 80012
 Source Host           : localhost:3306
 Source Schema         : secondhand

 Target Server Type    : MySQL
 Target Server Version : 80012
 File Encoding         : 65001

 Date: 04/07/2023 20:47:47
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for tb_goods
-- ----------------------------
DROP TABLE IF EXISTS `tb_goods`;
CREATE TABLE `tb_goods`  (
  `second_hand_id` int(11) NOT NULL AUTO_INCREMENT,
  `cart_title` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
  `cart_img` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
  `cart_description` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
  `user_id` int(11) NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `price` decimal(10, 2) NULL DEFAULT NULL,
  `status` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
  `good_num` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`second_hand_id`) USING BTREE,
  INDEX `user_id`(`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 764416047 CHARACTER SET = utf8 COLLATE = utf8_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_goods
-- ----------------------------
INSERT INTO `tb_goods` VALUES (4, 'waa', NULL, 'aaa', 3, '2023-06-01 19:03:33', '2023-06-09 19:01:34', 4.00, '正在售卖', 1999);
INSERT INTO `tb_goods` VALUES (764416006, 'xilin', NULL, 'xilin', 4, '2023-04-30 20:05:14', '2023-04-30 20:05:14', 12.00, '正在售卖', 1999);
INSERT INTO `tb_goods` VALUES (764416010, '测试3', 'D:\\3.2\\javaweb\\secondhand\\src\\main\\resources\\static\\images\\java.util.Random@58429338.jpeg', '测试3', 3, '2023-05-22 19:14:50', '2023-05-22 19:14:50', NULL, '正在售卖', 1999);
INSERT INTO `tb_goods` VALUES (764416011, 'xilin3', 'D:\\3.2\\javaweb\\secondhand\\src\\main\\resources\\static\\images\\java.util.Random@664f71bb.form-data', 'xilin3', 5, '2023-05-22 19:52:11', '2023-05-22 19:52:11', 100.00, '正在售卖', 1999);
INSERT INTO `tb_goods` VALUES (764416018, 'xilin4', 'D:\\3.2\\javaweb\\secondhand\\src\\main\\resources\\static\\images\\java.util.Random@6478f335.jpeg', 'xilin4', 5, '2023-05-22 19:53:46', '2023-05-22 19:53:46', 120.00, '正在售卖', 1999);
INSERT INTO `tb_goods` VALUES (764416020, NULL, 'localhost:8081/images/java.util.Random@13ba85a6.png', NULL, NULL, '2023-06-11 16:53:22', '2023-06-11 16:53:22', NULL, '正在售卖', 1999);
INSERT INTO `tb_goods` VALUES (764416021, NULL, 'localhost:8081/images/java.util.Random@634b3401.png', NULL, NULL, '2023-06-11 16:55:12', '2023-06-11 16:55:12', NULL, '正在售卖', 1999);
INSERT INTO `tb_goods` VALUES (764416022, NULL, 'localhost:8081/images/java.util.Random@5dc4ad87.png', NULL, NULL, '2023-06-11 16:56:23', '2023-06-11 16:56:23', NULL, '正在售卖', 1999);
INSERT INTO `tb_goods` VALUES (764416023, NULL, 'localhost:8081/images/java.util.Random@472931b7.png', NULL, NULL, '2023-06-11 16:56:25', '2023-06-11 16:56:25', NULL, '正在售卖', 1999);
INSERT INTO `tb_goods` VALUES (764416024, NULL, '', NULL, NULL, '2023-06-11 17:44:18', '2023-06-11 17:44:18', NULL, '正在售卖', 1999);
INSERT INTO `tb_goods` VALUES (764416025, 'xx', '', '', NULL, '2023-06-11 17:46:36', '2023-06-11 17:46:36', 123.00, '正在售卖', 1999);
INSERT INTO `tb_goods` VALUES (764416026, 'xax', 'null', 'adad', NULL, '2023-06-11 17:52:40', '2023-06-11 17:52:40', 123.00, '正在售卖', 1999);
INSERT INTO `tb_goods` VALUES (764416027, 'xaaa', 'localhost:8081/images/java.util.Random@155a71cf.jpeg,undefined', 'asdad', NULL, '2023-06-11 17:53:31', '2023-06-11 17:53:31', 12.00, '正在售卖', 1999);
INSERT INTO `tb_goods` VALUES (764416028, 'xaaa', 'localhost:8081/images/java.util.Random@155a71cf.jpeg,localhost:8081/images/java.util.Random@1c9da33.png,undefined', 'asdad', NULL, '2023-06-11 17:54:28', '2023-06-11 17:54:28', 12.00, '正在售卖', 1999);
INSERT INTO `tb_goods` VALUES (764416029, '西琳啊', 'localhost:8081/images/java.util.Random@42b8dd58.png,localhost:8081/images/java.util.Random@5549fe59.png', '西琳啊', NULL, '2023-06-11 17:57:49', '2023-06-11 17:57:49', 999.00, '正在售卖', 1999);
INSERT INTO `tb_goods` VALUES (764416030, '啊啊', 'localhost:8081/images/java.util.Random@42b8dd58.png,localhost:8081/images/java.util.Random@5549fe59.png', '阿大', 8, '2023-06-11 18:00:04', '2023-07-04 20:46:14', 111.00, '正在售卖', 1990);
INSERT INTO `tb_goods` VALUES (764416031, 'xilin31', '', 'xilin31', 8, '2023-06-18 19:47:55', '2023-06-18 19:47:55', 112.00, '正在售卖', 1999);
INSERT INTO `tb_goods` VALUES (764416032, 'xilin32', '', 'xilin32', 8, '2023-06-21 18:58:09', '2023-06-21 18:58:09', 112.00, '正在售卖', 1999);
INSERT INTO `tb_goods` VALUES (764416033, 'xilin32', '', 'xilin32', 8, '2023-06-21 18:58:40', '2023-06-21 18:58:40', 112.00, '正在售卖', 1999);
INSERT INTO `tb_goods` VALUES (764416034, '西琳啊啊', '', '哈哈哈', 8, '2023-06-21 19:01:15', '2023-06-21 19:01:15', 123.00, '正在售卖', NULL);
INSERT INTO `tb_goods` VALUES (764416035, '西琳啊啊', '', '哈哈哈', 8, '2023-06-21 19:01:20', '2023-06-21 19:01:20', 123.00, '正在售卖', NULL);
INSERT INTO `tb_goods` VALUES (764416036, 'asda', 'localhost:8081/images/java.util.Random@63d42616.jpeg,localhost:8081/images/java.util.Random@3d3d75ee.jpeg', '', 8, '2023-06-21 19:05:30', '2023-07-04 20:32:40', 112.00, '正在售卖', 1999);
INSERT INTO `tb_goods` VALUES (764416037, 'xilin32', '', 'xilin32', 8, '2023-06-21 19:17:32', '2023-06-21 19:17:32', 112.00, '已下架', 1999);
INSERT INTO `tb_goods` VALUES (764416038, 'xilin32', '', 'xilin32', 8, '2023-06-21 19:21:45', '2023-06-21 19:21:45', 112.00, '已下架', 1999);
INSERT INTO `tb_goods` VALUES (764416039, 'xilin38', 'localhost:8081/images/java.util.Random@72169db.png', '', 8, '2023-06-21 19:22:39', '2023-06-21 19:22:39', 112.00, '正在售卖', 1999);
INSERT INTO `tb_goods` VALUES (764416040, 'xilin39', 'localhost:8081/images/java.util.Random@384669d0.jpeg', 'ccccccccccccccccccccccccccccc', 8, '2023-06-21 19:25:51', '2023-06-28 21:51:20', 112.00, '正在售卖', 1996);
INSERT INTO `tb_goods` VALUES (764416041, 'xilin41', 'localhost:8081/images/java.util.Random@7d4cbba7.png', 'ttttttttttttttttttt', 8, '2023-06-28 20:19:54', '2023-06-28 20:19:54', 1231.00, '正在售卖', NULL);
INSERT INTO `tb_goods` VALUES (764416042, 'xilin42', 'localhost:8081/images/9670fe4d.png', 'rrrrrrrrrrr', 8, '2023-06-28 21:32:23', '2023-06-28 21:39:45', 144.00, '正在售卖', 123);
INSERT INTO `tb_goods` VALUES (764416045, 'xilin42', 'null', '1123', 8, '2023-06-29 09:39:35', '2023-06-29 09:42:03', 12.00, '正在售卖', 123);
INSERT INTO `tb_goods` VALUES (764416046, 'xilin44', 'localhost:8081/images/a0498ab743c74304b9.png', 'eeeee', 8, '2023-06-29 09:45:10', '2023-06-29 09:55:53', 123.00, '正在售卖', 1999);
INSERT INTO `tb_goods` VALUES (764416047, 'xilin44', 'localhost:8081/images/58fff0ab28944ad7ba.png', 'aaaaaaaa', 8, '2023-07-02 16:33:36', '2023-07-04 20:01:01', 12.00, '正在售卖', 1999);
INSERT INTO `tb_goods` VALUES (764416048, 'xilin32', 'localhost:8081/images/java.util.Random@42b8dd58.png,localhost:8081/images/java.util.Random@5549fe59.png', 'xilin32', 31, '2023-07-04 20:44:16', '2023-07-04 20:44:16', 112.00, '正在售卖', 1999);
INSERT INTO `tb_goods` VALUES (764416049, 'xilin32', 'localhost:8081/images/java.util.Random@42b8dd58.png,localhost:8081/images/java.util.Random@5549fe59.png', 'xilin32', 31, '2023-07-04 20:46:14', '2023-07-04 20:46:14', 112.00, '正在售卖', 1999);

-- ----------------------------
-- Table structure for tb_likes
-- ----------------------------
DROP TABLE IF EXISTS `tb_likes`;
CREATE TABLE `tb_likes`  (
  `like_id` int(11) NOT NULL AUTO_INCREMENT,
  `second_hand_id` int(11) NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT NULL,
  `user_id` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`like_id`) USING BTREE,
  INDEX `user_id`(`user_id`) USING BTREE,
  INDEX `second_hand_id`(`second_hand_id`) USING BTREE
) ENGINE = MyISAM AUTO_INCREMENT = 12 CHARACTER SET = utf8 COLLATE = utf8_unicode_ci ROW_FORMAT = Fixed;

-- ----------------------------
-- Records of tb_likes
-- ----------------------------
INSERT INTO `tb_likes` VALUES (1, 1, '2023-04-22 18:43:24', '2023-04-22 18:43:26', 1);
INSERT INTO `tb_likes` VALUES (2, 1, '2023-05-15 23:29:09', '2023-05-25 23:29:11', 2);
INSERT INTO `tb_likes` VALUES (3, 2, '2023-05-17 23:12:49', '2023-05-18 23:12:52', 1);
INSERT INTO `tb_likes` VALUES (4, 3, '2023-05-08 23:13:04', '2023-05-18 23:13:06', 1);
INSERT INTO `tb_likes` VALUES (7, 764416029, '2023-06-24 21:26:56', '2023-06-24 21:26:56', 8);
INSERT INTO `tb_likes` VALUES (8, 2, '2023-06-24 21:27:29', '2023-06-24 21:27:29', 8);
INSERT INTO `tb_likes` VALUES (9, 764416030, '2023-06-24 21:32:46', '2023-06-24 21:32:46', 8);
INSERT INTO `tb_likes` VALUES (12, 764416032, '2023-07-04 20:44:16', '2023-07-04 20:44:16', 8);
INSERT INTO `tb_likes` VALUES (13, 764416032, '2023-07-04 20:46:14', '2023-07-04 20:46:14', 8);

-- ----------------------------
-- Table structure for tb_order
-- ----------------------------
DROP TABLE IF EXISTS `tb_order`;
CREATE TABLE `tb_order`  (
  `order_id` int(11) NOT NULL AUTO_INCREMENT,
  `second_hand_id` int(11) NULL DEFAULT NULL,
  `user_id` int(11) NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT NULL,
  `state` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
  `total` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
  `seller_id` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`order_id`) USING BTREE,
  INDEX `user_id`(`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 92 CHARACTER SET = utf8 COLLATE = utf8_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_order
-- ----------------------------
INSERT INTO `tb_order` VALUES (2, 2, 2, '2023-05-09 23:38:15', '2023-05-15 23:38:18', '已完成', '10', NULL);
INSERT INTO `tb_order` VALUES (3, 1, 1, '2023-05-16 23:22:59', '2023-05-16 23:23:01', '待确认', NULL, NULL);
INSERT INTO `tb_order` VALUES (4, 764416011, 3, '2023-05-27 18:11:43', '2023-05-27 18:11:43', '已完成', '200', NULL);
INSERT INTO `tb_order` VALUES (5, 764416011, 8, '2023-05-27 18:11:43', '2023-05-27 18:11:43', '已完成', '200', NULL);
INSERT INTO `tb_order` VALUES (6, 2, 8, '2023-06-18 22:27:31', '2023-06-18 22:27:31', '关闭', NULL, NULL);
INSERT INTO `tb_order` VALUES (7, 2, 8, '2023-06-18 22:28:09', '2023-06-18 22:28:09', '关闭', NULL, NULL);
INSERT INTO `tb_order` VALUES (8, 764416012, 5, '2023-06-18 22:30:12', '2023-06-18 22:30:12', '关闭', NULL, NULL);
INSERT INTO `tb_order` VALUES (9, 764416011, 5, '2023-06-18 22:30:12', '2023-06-18 22:30:13', '关闭', NULL, NULL);
INSERT INTO `tb_order` VALUES (10, 764416012, 5, '2023-06-18 22:41:17', '2023-06-18 22:41:17', '关闭', NULL, NULL);
INSERT INTO `tb_order` VALUES (11, 764416011, 5, '2023-06-18 22:41:17', '2023-06-18 22:41:17', '关闭', NULL, NULL);
INSERT INTO `tb_order` VALUES (12, 2, 8, '2023-06-18 23:36:31', '2023-06-18 23:36:31', '关闭', NULL, NULL);
INSERT INTO `tb_order` VALUES (13, 2, 8, '2023-06-18 23:39:03', '2023-06-18 23:39:03', '关闭', NULL, NULL);
INSERT INTO `tb_order` VALUES (14, 2, 8, '2023-06-18 23:42:32', '2023-06-18 23:42:32', '关闭', NULL, NULL);
INSERT INTO `tb_order` VALUES (15, 2, 8, '2023-06-18 23:44:51', '2023-06-18 23:44:51', '关闭', NULL, NULL);
INSERT INTO `tb_order` VALUES (16, 2, 8, '2023-06-18 23:45:22', '2023-06-18 23:45:22', '关闭', NULL, NULL);
INSERT INTO `tb_order` VALUES (17, 2, 8, '2023-06-18 23:49:25', '2023-06-18 23:49:25', '关闭', NULL, NULL);
INSERT INTO `tb_order` VALUES (18, 2, 8, '2023-06-18 23:50:40', '2023-06-18 23:50:40', '关闭', NULL, NULL);
INSERT INTO `tb_order` VALUES (19, 2, 8, '2023-06-18 23:53:09', '2023-06-18 23:53:09', '关闭', NULL, NULL);
INSERT INTO `tb_order` VALUES (20, 2, 8, '2023-06-18 23:54:25', '2023-06-18 23:54:25', '关闭', NULL, NULL);
INSERT INTO `tb_order` VALUES (21, 2, 8, '2023-06-19 00:07:58', '2023-06-19 00:07:58', '关闭', NULL, NULL);
INSERT INTO `tb_order` VALUES (22, 2, 8, '2023-06-19 00:15:22', '2023-06-19 00:15:22', '关闭', NULL, NULL);
INSERT INTO `tb_order` VALUES (23, 2, 8, '2023-06-19 00:16:53', '2023-06-19 00:16:53', '关闭', NULL, NULL);
INSERT INTO `tb_order` VALUES (24, 2, 8, '2023-06-19 16:38:50', '2023-06-19 16:38:50', '关闭', NULL, NULL);
INSERT INTO `tb_order` VALUES (25, 2, 8, '2023-06-19 16:39:19', '2023-06-19 16:39:19', '关闭', NULL, NULL);
INSERT INTO `tb_order` VALUES (26, 2, 8, '2023-06-19 16:40:31', '2023-06-19 16:40:31', '关闭', NULL, NULL);
INSERT INTO `tb_order` VALUES (27, 2, 8, '2023-06-20 16:42:29', '2023-06-20 16:42:29', '关闭', NULL, NULL);
INSERT INTO `tb_order` VALUES (28, 2, 8, '2023-06-20 16:43:16', '2023-06-20 16:43:16', '关闭', NULL, NULL);
INSERT INTO `tb_order` VALUES (29, 2, 8, '2023-06-20 16:44:45', '2023-06-20 16:44:45', '关闭', NULL, NULL);
INSERT INTO `tb_order` VALUES (30, 2, 8, '2023-06-20 16:46:36', '2023-06-20 16:46:36', '关闭', NULL, NULL);
INSERT INTO `tb_order` VALUES (31, 2, 8, '2023-06-20 16:47:18', '2023-06-20 16:47:18', '关闭', NULL, NULL);
INSERT INTO `tb_order` VALUES (32, 2, 8, '2023-06-20 16:49:48', '2023-06-20 16:49:48', '关闭', NULL, NULL);
INSERT INTO `tb_order` VALUES (33, 2, 8, '2023-06-20 17:01:57', '2023-06-20 17:01:57', '关闭', NULL, NULL);
INSERT INTO `tb_order` VALUES (34, 2, 8, '2023-06-20 17:02:52', '2023-06-20 17:02:52', '关闭', NULL, NULL);
INSERT INTO `tb_order` VALUES (35, 2, 8, '2023-06-20 17:07:44', '2023-06-20 17:07:44', '关闭', NULL, NULL);
INSERT INTO `tb_order` VALUES (36, 2, 8, '2023-06-20 17:11:29', '2023-06-20 17:11:29', '关闭', NULL, NULL);
INSERT INTO `tb_order` VALUES (37, 2, 8, '2023-06-20 17:13:46', '2023-06-20 17:13:46', '关闭', NULL, NULL);
INSERT INTO `tb_order` VALUES (38, 2, 8, '2023-06-20 17:15:16', '2023-06-20 17:15:16', '关闭', NULL, NULL);
INSERT INTO `tb_order` VALUES (39, 2, 8, '2023-06-20 17:15:44', '2023-06-20 17:15:44', '关闭', NULL, NULL);
INSERT INTO `tb_order` VALUES (40, 764416030, 8, '2023-06-20 18:23:12', '2023-06-20 18:23:12', '关闭', NULL, NULL);
INSERT INTO `tb_order` VALUES (41, 2, 8, '2023-06-20 18:23:12', '2023-06-20 18:23:12', '关闭', NULL, NULL);
INSERT INTO `tb_order` VALUES (42, 764416030, 8, '2023-06-20 18:28:11', '2023-06-20 18:28:11', '关闭', NULL, NULL);
INSERT INTO `tb_order` VALUES (43, 2, 8, '2023-06-20 18:28:11', '2023-06-20 18:28:11', '关闭', NULL, NULL);
INSERT INTO `tb_order` VALUES (44, 764416030, 8, '2023-06-20 18:45:02', '2023-06-20 18:45:02', '关闭', NULL, NULL);
INSERT INTO `tb_order` VALUES (45, 2, 8, '2023-06-20 18:45:02', '2023-06-20 18:45:02', '关闭', NULL, NULL);
INSERT INTO `tb_order` VALUES (46, 764416030, 8, '2023-06-20 18:52:35', '2023-06-20 18:52:35', '关闭', NULL, NULL);
INSERT INTO `tb_order` VALUES (47, 2, 8, '2023-06-20 18:52:35', '2023-06-20 18:52:35', '关闭', NULL, NULL);
INSERT INTO `tb_order` VALUES (48, 764416030, 8, '2023-06-21 00:29:51', '2023-06-21 00:29:51', '关闭', NULL, NULL);
INSERT INTO `tb_order` VALUES (49, 2, 8, '2023-06-21 00:29:51', '2023-06-21 00:29:51', '关闭', NULL, NULL);
INSERT INTO `tb_order` VALUES (50, 764416030, 8, '2023-06-21 00:41:45', '2023-06-21 00:41:45', '关闭', NULL, NULL);
INSERT INTO `tb_order` VALUES (51, 2, 8, '2023-06-21 00:41:45', '2023-06-21 00:41:45', '关闭', NULL, NULL);
INSERT INTO `tb_order` VALUES (52, 764416030, 8, '2023-06-21 00:44:16', '2023-06-21 00:44:16', '关闭', NULL, NULL);
INSERT INTO `tb_order` VALUES (53, 2, 8, '2023-06-21 00:44:16', '2023-06-21 00:44:17', '关闭', NULL, NULL);
INSERT INTO `tb_order` VALUES (54, 764416030, 8, '2023-06-21 00:48:22', '2023-06-21 00:48:22', '关闭', NULL, NULL);
INSERT INTO `tb_order` VALUES (55, 2, 8, '2023-06-21 00:48:22', '2023-06-21 00:48:22', '关闭', NULL, NULL);
INSERT INTO `tb_order` VALUES (56, 764416030, 8, '2023-06-21 00:56:08', '2023-06-21 00:56:08', '关闭', NULL, NULL);
INSERT INTO `tb_order` VALUES (57, 2, 8, '2023-06-21 00:56:08', '2023-06-21 00:56:08', '关闭', NULL, NULL);
INSERT INTO `tb_order` VALUES (58, 764416030, 8, '2023-06-21 01:13:47', '2023-06-21 01:13:47', '关闭', NULL, NULL);
INSERT INTO `tb_order` VALUES (59, 2, 8, '2023-06-21 01:13:47', '2023-06-21 01:13:48', '关闭', NULL, NULL);
INSERT INTO `tb_order` VALUES (60, 764416030, 8, '2023-06-21 01:23:15', '2023-06-21 01:23:15', '关闭', NULL, NULL);
INSERT INTO `tb_order` VALUES (61, 2, 8, '2023-06-21 01:23:15', '2023-06-21 01:23:15', '关闭', NULL, NULL);
INSERT INTO `tb_order` VALUES (62, 764416030, 8, '2023-06-21 01:24:20', '2023-06-21 01:24:20', '关闭', NULL, NULL);
INSERT INTO `tb_order` VALUES (63, 2, 8, '2023-06-21 01:24:20', '2023-06-21 01:24:20', '关闭', NULL, NULL);
INSERT INTO `tb_order` VALUES (64, 764416030, 8, '2023-06-21 18:34:56', '2023-06-21 18:34:56', '关闭', NULL, NULL);
INSERT INTO `tb_order` VALUES (65, 764416030, 8, '2023-06-21 18:35:23', '2023-06-21 18:35:23', '关闭', NULL, NULL);
INSERT INTO `tb_order` VALUES (66, 2, 8, '2023-06-21 18:35:23', '2023-06-21 18:35:23', '关闭', NULL, NULL);
INSERT INTO `tb_order` VALUES (67, 764416030, 8, '2023-06-21 18:48:58', '2023-06-21 18:48:58', '关闭', NULL, NULL);
INSERT INTO `tb_order` VALUES (68, 2, 8, '2023-06-21 18:48:58', '2023-06-21 18:48:59', '关闭', NULL, NULL);
INSERT INTO `tb_order` VALUES (69, 764416040, 8, '2023-06-21 22:56:36', '2023-06-21 22:56:36', '关闭', NULL, NULL);
INSERT INTO `tb_order` VALUES (70, 764416040, 8, '2023-06-24 15:35:05', '2023-06-24 15:35:05', '关闭', NULL, NULL);
INSERT INTO `tb_order` VALUES (71, 2, 8, '2023-06-24 15:40:56', '2023-06-24 15:40:56', '关闭', NULL, NULL);
INSERT INTO `tb_order` VALUES (72, 2, 8, '2023-06-24 15:54:04', '2023-06-24 15:54:04', '关闭', NULL, NULL);
INSERT INTO `tb_order` VALUES (73, 2, 8, '2023-06-24 16:53:51', '2023-06-24 16:53:51', '关闭', '2.00', NULL);
INSERT INTO `tb_order` VALUES (74, 2, 8, '2023-06-24 17:03:56', '2023-06-24 17:03:56', '关闭', '8.00', NULL);
INSERT INTO `tb_order` VALUES (75, 764416040, 8, '2023-06-24 17:10:00', '2023-06-24 17:10:00', '关闭', '112.00', NULL);
INSERT INTO `tb_order` VALUES (76, 2, 8, '2023-06-24 17:33:41', '2023-06-24 17:33:41', '关闭', '8.00', NULL);
INSERT INTO `tb_order` VALUES (77, 2, 8, '2023-06-24 17:34:36', '2023-06-24 17:34:36', '关闭', '8.00', NULL);
INSERT INTO `tb_order` VALUES (78, 2, 8, '2023-06-24 17:50:52', '2023-06-24 17:50:52', '关闭', '8.00', NULL);
INSERT INTO `tb_order` VALUES (79, 2, 8, '2023-06-24 18:02:45', '2023-06-24 18:02:45', '关闭', '8.00', NULL);
INSERT INTO `tb_order` VALUES (80, 2, 8, '2023-06-24 18:58:19', '2023-06-24 18:58:19', '关闭', '8.00', NULL);
INSERT INTO `tb_order` VALUES (81, 2, 8, '2023-06-25 00:50:00', '2023-06-25 00:50:00', '关闭', '8.00', NULL);
INSERT INTO `tb_order` VALUES (82, 2, 8, '2023-06-25 19:20:06', '2023-06-25 19:20:06', '关闭', '8.00', NULL);
INSERT INTO `tb_order` VALUES (83, 764416030, 8, '2023-06-25 19:35:29', '2023-06-25 19:35:29', '关闭', '111.00', NULL);
INSERT INTO `tb_order` VALUES (84, 764416040, 8, '2023-06-25 19:37:35', '2023-06-25 19:37:35', '已完成', '112.00', 8);
INSERT INTO `tb_order` VALUES (85, 764416030, 8, '2023-06-25 20:58:06', '2023-06-25 20:58:06', '已完成', '111.00', 8);
INSERT INTO `tb_order` VALUES (86, 764416030, 8, '2023-06-25 20:59:06', '2023-06-25 20:59:06', '已完成', '111.00', 8);
INSERT INTO `tb_order` VALUES (87, 764416042, 21, '2023-06-28 21:39:45', '2023-06-28 21:39:45', '已完成', '144.00', 8);
INSERT INTO `tb_order` VALUES (88, 764416040, 21, '2023-06-28 21:41:19', '2023-06-28 21:41:19', '关闭', '112.00', 8);
INSERT INTO `tb_order` VALUES (89, 764416045, 22, '2023-06-29 09:42:03', '2023-06-29 09:42:03', '已完成', '12.00', 8);
INSERT INTO `tb_order` VALUES (90, 764416046, 8, '2023-06-29 09:45:52', '2023-06-29 09:45:52', '关闭', '123.00', 8);
INSERT INTO `tb_order` VALUES (91, 764416047, 8, '2023-07-04 20:01:01', '2023-07-04 20:01:01', '已完成', '12.00', 8);
INSERT INTO `tb_order` VALUES (92, 764416036, 8, '2023-07-04 20:01:01', '2023-07-04 20:01:01', '关闭', '112.00', 8);

-- ----------------------------
-- Table structure for tb_ratingform
-- ----------------------------
DROP TABLE IF EXISTS `tb_ratingform`;
CREATE TABLE `tb_ratingform`  (
  `evaluate_id` int(11) NOT NULL AUTO_INCREMENT,
  `order_id` int(11) NULL DEFAULT NULL,
  `state` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
  `star_rating` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT NULL,
  `content` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
  `user_id` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`evaluate_id`) USING BTREE,
  INDEX `order_id`(`order_id`) USING BTREE,
  INDEX `user_id`(`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 23 CHARACTER SET = utf8 COLLATE = utf8_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_ratingform
-- ----------------------------
INSERT INTO `tb_ratingform` VALUES (1, 1, '已评价', '4', NULL, NULL, 'shit', 1);
INSERT INTO `tb_ratingform` VALUES (2, 2, '已评价', '5', '2023-04-23 22:58:08', '2023-04-23 22:58:08', '测试内容', 1);
INSERT INTO `tb_ratingform` VALUES (3, 2, '未评价', '0', '2023-05-16 23:36:56', '2023-05-16 23:36:56', NULL, 3);
INSERT INTO `tb_ratingform` VALUES (4, 2, '审核中', '4', '2023-05-16 23:38:37', '2023-05-16 23:38:37', 'xilin4', 8);
INSERT INTO `tb_ratingform` VALUES (15, 5, '已评价', '0', '2023-05-27 21:28:23', '2023-05-27 21:28:23', NULL, 8);
INSERT INTO `tb_ratingform` VALUES (16, 78, '已评价', '4', '2023-06-24 17:59:05', '2023-06-24 17:59:05', 'asdasggggg', 8);
INSERT INTO `tb_ratingform` VALUES (17, 79, '关闭', '3', '2023-06-24 18:06:04', '2023-06-24 18:06:04', 'asdas', 8);
INSERT INTO `tb_ratingform` VALUES (18, 82, '未评价', '0', '2023-06-25 19:21:31', '2023-06-25 19:21:31', NULL, 8);
INSERT INTO `tb_ratingform` VALUES (19, 83, '未评价', '0', '2023-06-25 19:35:35', '2023-06-25 19:35:35', NULL, 8);
INSERT INTO `tb_ratingform` VALUES (20, 84, '未评价', '0', '2023-06-25 19:37:38', '2023-06-25 19:37:38', NULL, 8);
INSERT INTO `tb_ratingform` VALUES (21, 87, '未评价', '0', '2023-06-28 21:41:57', '2023-06-28 21:41:57', NULL, 21);
INSERT INTO `tb_ratingform` VALUES (22, 89, '审核中', '4', '2023-06-29 09:43:32', '2023-06-29 09:43:46', 'saasd', 22);
INSERT INTO `tb_ratingform` VALUES (23, 91, '未评价', '0', '2023-07-04 20:01:25', '2023-07-04 20:01:25', NULL, 8);

-- ----------------------------
-- Table structure for tb_user
-- ----------------------------
DROP TABLE IF EXISTS `tb_user`;
CREATE TABLE `tb_user`  (
  `user_id` int(11) NOT NULL AUTO_INCREMENT,
  `user_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
  `user_phone` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
  `money` decimal(11, 0) NULL DEFAULT NULL,
  `account` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
  `password` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
  `address` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
  `role` int(255) NULL DEFAULT NULL,
  `img` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
  PRIMARY KEY (`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 22 CHARACTER SET = utf8 COLLATE = utf8_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_user
-- ----------------------------
INSERT INTO `tb_user` VALUES (3, 'xilin', '101111', 1000, '111', '698d51a19d8a121ce581499d7b701668', 'xilin', 0, NULL);
INSERT INTO `tb_user` VALUES (4, 'xilin', '101111', 1000, '222', '698d51a19d8a121ce581499d7b701668', 'xilin', 2, NULL);
INSERT INTO `tb_user` VALUES (5, 'xilin', '101111', 1120, '333', '698d51a19d8a121ce581499d7b701668', 'xilin', 0, NULL);
INSERT INTO `tb_user` VALUES (8, '西琳啊', '18127447924', 968, '3', 'LC@AFFLGDLMFCG', 'home', 2, 'localhost:8081/images/java.util.Random@6c4d45ec.jpeg');
INSERT INTO `tb_user` VALUES (9, '西琳啊', '', 1000, '4', 'FDFMBFAMDCAMB@DCEAFFG@CD', '', 0, NULL);
INSERT INTO `tb_user` VALUES (11, 'xilin6', '18127447942', 1000, '1234', 'FDFMBFAMDCAMB@DCEAFFG@CD', 'xxx', 0, NULL);
INSERT INTO `tb_user` VALUES (12, 'xilin7', '18127447942', 1000, '12345', 'FDFMBFAMDCAMB@DCEAFFG@CD', 'xxx', 0, NULL);
INSERT INTO `tb_user` VALUES (13, 'xilin8', '18127447942', 1000, '1234567', 'FDFMBFAMDCAMB@DCEAFFG@CD', 'xxx', 0, NULL);
INSERT INTO `tb_user` VALUES (14, 'xilin9', '18127447942', 1000, '12345678', 'FDFMBFAMDCAMB@DCEAFFG@CD', 'xxx', 0, NULL);
INSERT INTO `tb_user` VALUES (20, 'x', '123', 1000, '123', 'FDFMBFAMDCAMB@DCEAFFG@CD', 'x', 0, NULL);
INSERT INTO `tb_user` VALUES (21, '啊哈哈', '18127447921', 856, '5', '@GCFG@ACCCFDBC@GELA', 'hhhhh', 0, 'localhost:8081/images/d4d71d78.jpeg');
INSERT INTO `tb_user` VALUES (22, '6', '18127447942', 988, '6', 'EBCMDMEALLDBABDLCEF', 'aaa', 1, 'localhost:8081/images/09f76a7388774b2180.jpeg');
INSERT INTO `tb_user` VALUES (23, 'xilin', '101111', 1000, '444', 'BMLAEEMLEFEALE@MMCCDEBBL', 'xilin', 0, NULL);

SET FOREIGN_KEY_CHECKS = 1;
