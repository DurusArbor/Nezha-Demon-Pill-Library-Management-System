/*
Navicat MySQL Data Transfer

Source Server         : localhost_3306
Source Server Version : 50717
Source Host           : localhost:3306
Source Database       : ry-vue

Target Server Type    : MYSQL
Target Server Version : 50717
File Encoding         : 65001

Date: 2026-10-19 18:13:00
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for lib_user
-- ----------------------------
DROP TABLE IF EXISTS `lib_user`;
CREATE TABLE `lib_user` (
  `user_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '读者ID',
  `user_name` varchar(50) NOT NULL COMMENT '读者姓名',
  `user_phone` varchar(20) DEFAULT NULL COMMENT '联系电话',
  `user_email` varchar(100) DEFAULT NULL COMMENT '邮箱',
  `user_status` char(1) DEFAULT '0' COMMENT '状态 0正常 1禁用',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COMMENT='图书读者表';

-- ----------------------------
-- Records of lib_user
-- ----------------------------
INSERT INTO `lib_user` VALUES ('2', '131232', null, null, '0', '', '2026-10-19 17:48:44', '', null, null);
