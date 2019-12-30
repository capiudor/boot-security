# Spring Boot 整合 Security框架

## 1.要做的一个效果

- 除了登录、登录校验、主页之外都应该 认证后才能访问
- 从数据库获取角色、用户名及密码
- 不同角色显示不同的内容
- 可以记住密码

## 2. 需要用的东西

> jdk: 1.8 
>
> maven: 3.6.3
>
> IDEA: 2019.3.1
>
> boot: 2.2.2.RELEASE
>
> thymeleaf: 与boot兼容的3.0.11.RELEASE
>
> MySQL：5.7

![版本信息](src/main/resouces/static/imgs/搜狗截图20191224173218.png)

## 3. 构建数据库

这里使用的 Navicat Premium工具进行构建

![数据库建模](src/main/resouces/static/imgs/搜狗截图20191230172322.png)

建表语句如下

```sql
/*
Navicat MySQL Data Transfer

Source Server         : localhost_3306
Source Server Version : 50728
Source Host           : localhost:3306
Source Database       : security-boot

Target Server Type    : MYSQL
Target Server Version : 50728
File Encoding         : 65001

Date: 2019-12-30 17:22:12
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for resource
-- ----------------------------
DROP TABLE IF EXISTS `resource`;
CREATE TABLE `resource` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `res_name` varchar(255) NOT NULL,
  `res_url` varchar(255) NOT NULL,
  `res_jurisdiction` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of resource
-- ----------------------------

-- ----------------------------
-- Table structure for resource_role
-- ----------------------------
DROP TABLE IF EXISTS `resource_role`;
CREATE TABLE `resource_role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `role_id` int(11) NOT NULL,
  `res_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_role_ll_id` (`role_id`),
  KEY `fk_res_role_res_id` (`res_id`),
  CONSTRAINT `fk_res_role_res_id` FOREIGN KEY (`res_id`) REFERENCES `resource` (`id`),
  CONSTRAINT `fk_role_ll_id` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of resource_role
-- ----------------------------

-- ----------------------------
-- Table structure for role
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `role_name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of role
-- ----------------------------

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `useranme` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `headImg` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user
-- ----------------------------

-- ----------------------------
-- Table structure for user_role
-- ----------------------------
DROP TABLE IF EXISTS `user_role`;
CREATE TABLE `user_role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `role_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_user_id` (`user_id`),
  KEY `fk_role_id` (`role_id`),
  CONSTRAINT `fk_role_id` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`),
  CONSTRAINT `fk_user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user_role
-- ----------------------------
```

## 4.快速构建框架

这里我们使用IDEA工具自带的 spring Initializer 快速构建：

选择 spring Web 、Thymeleaf、security、MYSQL Driver、Mybatis Framework。

![spring Initializer 截图](src/main/resouces/static/imgs/搜狗截图20191230170408.png)







