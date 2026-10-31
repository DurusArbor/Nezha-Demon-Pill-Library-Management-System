/*
Navicat MySQL Data Transfer

Source Server         : localhost_3306
Source Server Version : 50717
Source Host           : localhost:3306
Source Database       : ry-vue

Target Server Type    : MYSQL
Target Server Version : 50717
File Encoding         : 65001

Date: 2026-10-19 18:12:52
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for lib_borrow
-- ----------------------------
DROP TABLE IF EXISTS `lib_borrow`;
CREATE TABLE `lib_borrow` (
  `borrow_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '借阅记录ID',
  `user_id` bigint(20) NOT NULL COMMENT '读者ID(关联lib_user)',
  `book_id` bigint(20) NOT NULL COMMENT '图书ID(关联lib_book)',
  `borrow_time` datetime NOT NULL COMMENT '借出时间',
  `return_time` datetime DEFAULT NULL COMMENT '归还时间，NULL代表未归还',
  `deadline_time` datetime NOT NULL COMMENT '应还截止日期',
  `borrow_status` char(1) DEFAULT '0' COMMENT '状态 0借阅中 1已归还 2逾期',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`borrow_id`),
  KEY `fk_borrow_user` (`user_id`),
  KEY `fk_borrow_book` (`book_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COMMENT='图书借阅记录表';

-- ----------------------------
-- Records of lib_borrow
-- ----------------------------
