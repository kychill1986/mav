/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50133
Source Host           : localhost:3306
Source Database       : test

Target Server Type    : MYSQL
Target Server Version : 50133
File Encoding         : 65001

Date: 2013-02-19 15:04:18
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------+--
-- Table structure for `user`
-- ----------------------------+--
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` varchar(32) NOT NULL,
  `username` varchar(50) DEFAULT NULL,
  `password` varchar(50) CHARACTER SET utf8 DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;

-- ----------------------------+--
-- Records of user
-- ----------------------------+--
INSERT INTO `user` VALUES ('1', 'chill', '111111');
INSERT INTO `user` VALUES ('2', 'kawaru', '222222');
