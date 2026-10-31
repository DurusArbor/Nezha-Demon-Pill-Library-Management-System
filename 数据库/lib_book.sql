/*
Navicat MySQL Data Transfer

Source Server         : localhost_3306
Source Server Version : 50717
Source Host           : localhost:3306
Source Database       : ry-vue

Target Server Type    : MYSQL
Target Server Version : 50717
File Encoding         : 65001

Date: 2026-10-19 18:12:42
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for lib_book
-- ----------------------------
DROP TABLE IF EXISTS `lib_book`;
CREATE TABLE `lib_book` (
  `book_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '图书ID',
  `book_name` varchar(200) NOT NULL COMMENT '图书名称',
  `book_author` varchar(100) DEFAULT NULL COMMENT '作者',
  `book_isbn` varchar(30) DEFAULT NULL COMMENT 'ISBN编号',
  `book_publisher` varchar(100) DEFAULT NULL COMMENT '出版社',
  `publish_date` date DEFAULT NULL COMMENT '出版日期',
  `book_count` int(11) NOT NULL DEFAULT '0' COMMENT '馆藏总数量',
  `borrow_count` int(11) NOT NULL DEFAULT '0' COMMENT '已借出数量',
  `book_status` char(1) DEFAULT '0' COMMENT '状态：0正常，1下架',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`book_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COMMENT='图书信息表';

-- ----------------------------
-- Records of lib_book
-- ----------------------------
INSERT INTO `lib_book` VALUES ('1', '晓楠', '晓楠', '123', null, null, '12', '10', '0', '', '2026-10-19 16:06:24', '', null, null);
INSERT INTO `lib_book` VALUES ('2', '生气', null, null, null, null, '1111', '1111', '0', '', '2026-10-19 17:49:07', '', '2026-10-19 17:51:39', null);
