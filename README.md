# Spring Boot + MyBatis 整合 Security框架

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

![](src\main\resources\static\imgs\搜狗截图20191224173218.png)

## 3. 构建数据库

这里使用的 Navicat Premium工具进行构建

![](src\main\resources\static\imgs\搜狗截图20191230172322.png)

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

Date: 2020-01-03 16:14:54
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
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of resource
-- ----------------------------
INSERT INTO `resource` VALUES ('1', '高级查看', '/level1/**', 'select');
INSERT INTO `resource` VALUES ('2', '进阶查看', '/level2/**', 'select');
INSERT INTO `resource` VALUES ('3', '基础查看', '/level3/**', 'select');

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
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of resource_role
-- ----------------------------
INSERT INTO `resource_role` VALUES ('1', '1', '1');
INSERT INTO `resource_role` VALUES ('2', '2', '2');
INSERT INTO `resource_role` VALUES ('3', '3', '3');

-- ----------------------------
-- Table structure for role
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `role_name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of role
-- ----------------------------
INSERT INTO `role` VALUES ('1', 'super');
INSERT INTO `role` VALUES ('2', 'admin');
INSERT INTO `role` VALUES ('3', 'user');

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
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES ('1', 'capiudor', '$2a$10$Fr2oK2yjqV6hxMMxqOi53Oev7yyTSKXVNYvaCPYg0vdMuxLtzR64G', 'https://b-ssl.duitang.com/uploads/item/201607/26/20160726185736_yPmrE.thumb.224_0.jpeg');
INSERT INTO `user` VALUES ('2', 'capiudor2', '$2a$10$Fr2oK2yjqV6hxMMxqOi53Oev7yyTSKXVNYvaCPYg0vdMuxLtzR64G', 'http://img0.imgtn.bdimg.com/it/u=3256100974,305075936&fm=26&gp=0.jpg');
INSERT INTO `user` VALUES ('3', 'capiudor3', '$2a$10$Fr2oK2yjqV6hxMMxqOi53Oev7yyTSKXVNYvaCPYg0vdMuxLtzR64G', 'http://pic4.zhimg.com/50/v2-848b1a190d937e270e8d062d00865493_hd.jpg');

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
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user_role
-- ----------------------------
INSERT INTO `user_role` VALUES ('1', '1', '1');
INSERT INTO `user_role` VALUES ('2', '2', '2');
INSERT INTO `user_role` VALUES ('3', '3', '3');
INSERT INTO `user_role` VALUES ('4', '1', '2');
INSERT INTO `user_role` VALUES ('5', '1', '3');
INSERT INTO `user_role` VALUES ('6', '2', '3');
```

## 4. 快速构建框架

这里我们使用IDEA工具自带的 spring Initializer 快速构建：

选择 spring Web 、Thymeleaf、security、MYSQL Driver、Mybatis Framework。



![](src\main\resources\static\imgs\搜狗截图20191230170408.png)

## 5. 快速整合 Mybatis 及 Mybatis逆向工程

在这里我们使用 [MyBatis Generator](http://mybatis.org/generator/index.html) 进行逆向工程生成

###  5.1. MyBatis Generator 的核心 Configuration.xml

`src/main/resources/mybatis-generate-cfg.xml` 

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE generatorConfiguration PUBLIC "-//mybatis.org//DTD MyBatis Generator Configuration 1.0//EN"
        "http://mybatis.org/dtd/mybatis-generator-config_1_0.dtd" >
<generatorConfiguration>
<!--  <properties resource="mybatis.properties" />
     -->
<classPathEntry location="H:\repo\maven\mysql\mysql-connector-java\8.0.16\mysql-connector-java-8.0.16.jar" />
<context id="msqlTables" targetRuntime="MyBatis3">
    <plugin type="org.mybatis.generator.plugins.SerializablePlugin"></plugin>
    <jdbcConnection connectionURL="jdbc:mysql://localhost:3306/security-boot?serverTimezone=GMT%2B8"
                    driverClass="com.mysql.cj.jdbc.Driver" password="root" userId="root" >

        <property name="nullCatalogMeansCurrent" value="true"/>
    </jdbcConnection>
    <javaTypeResolver>
        <property name="forceBigDecimals" value="false" />
    </javaTypeResolver>
    <javaModelGenerator targetPackage="top.capiudor.security.entity" targetProject="F:\git-space\idea\boot-security\src\main\java">
        <property name="enableSubPackages" value="true"/>
        <!-- 从数据库返回的值被清理前后的空格  -->
        <property name="trimStrings" value="true" />
    </javaModelGenerator>
    <sqlMapGenerator targetPackage="top.capiudor.security.dao" targetProject="F:\git-space\idea\boot-security\src\main\java">
        <property name="enableSubPackages" value="true"/>
    </sqlMapGenerator>
    <javaClientGenerator type="XMLMAPPER" targetPackage="top.capiudor.security.dao" targetProject="F:\git-space\idea\boot-security\src\main\java">
        <property name="enableSubPackages" value="true"/>
    </javaClientGenerator>

    <!--数据库表-->
    <table tableName="user" domainObjectName="User"
           enableCountByExample="false" enableUpdateByExample="false" enableDeleteByExample="false"
           enableSelectByExample="false" selectByExampleQueryId="false" >
        <property name="useActualColumnNames" value="false"/>
    </table>
    <table tableName="role" domainObjectName="Role"
           enableCountByExample="false" enableUpdateByExample="false" enableDeleteByExample="false"
           enableSelectByExample="false" selectByExampleQueryId="false" >
        <property name="useActualColumnNames" value="false"/>
    </table>
    <table tableName="resource" domainObjectName="Resource"
           enableCountByExample="false" enableUpdateByExample="false" enableDeleteByExample="false"
           enableSelectByExample="false" selectByExampleQueryId="false" >
        <property name="useActualColumnNames" value="false"/>
    </table>
    <table tableName="resource_role" domainObjectName="ResourceRole"
           enableCountByExample="false" enableUpdateByExample="false" enableDeleteByExample="false"
           enableSelectByExample="false" selectByExampleQueryId="false" >
        <property name="useActualColumnNames" value="false"/>
    </table>
    <table tableName="user_role" domainObjectName="UserRole"
           enableCountByExample="false" enableUpdateByExample="false" enableDeleteByExample="false"
           enableSelectByExample="false" selectByExampleQueryId="false" >
        <property name="useActualColumnNames" value="false"/>
    </table>
</context>
</generatorConfiguration>
```

### 5.2.  引入Mybatis Generator 的 依赖

`pom.xml`

```xml
<!-- 添加到dependencies 中的核心依赖 -->
<dependency>
    <groupId>org.mybatis.generator</groupId>
    <artifactId>mybatis-generator-core</artifactId>
    <version>1.3.2</version>
</dependency>
```

### 5.3. 使用 Maven 运行

- 引入Maven插件
- maven 命令

#### 5.3.1. maven 插件

`pom.xml`  在`<plugins></plugins>` 标签中添加一下内容

```xml
<plugin>
    <groupId>org.mybatis.generator</groupId>
    <artifactId>mybatis-generator-maven-plugin</artifactId>
    <version>1.3.2</version>
    <configuration>
        <configurationFile>src/main/resources/mybatis-generate-cfg.xml</configurationFile>
        <verbose>true</verbose>
        <overwrite>true</overwrite>
    </configuration>
</plugin>
```

#### 5.3.2. Execute 

##### 5.3.2.1. By the command line

The goal can be executed from the command line with the command:

- `mvn mybatis-generator:generate`

  > 官方说用这条命令可以

```java
> mvn mybatis-generator:generate
```

#####  5.3.2.1. By IDEA plugin

在 `Command line` 中键入 `mybatis-generator:generate -e`

如下图所示：

![](src\main\resources\static\imgs\搜狗截图20200103163706.png)

### 5.4. 配置 MyBatis 关联映射

`src/main/resources/application.yml` 

```yaml
spring:
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3306/security-boot?serverTimezone=UTC&useUnicode=true&characterEncoding=utf8&useSSL=false
    username: root
    password: root
mybatis:
  mapper-locations: top/capiudor/security/dao/*.xml
  type-aliases-package: top.capiudor.security.entity
```



## 6. 整合Security

 ### 6.1. 引入依赖

`pom.xml` 

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.security</groupId>
    <artifactId>spring-security-test</artifactId>
    <scope>test</scope>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-thymeleaf</artifactId>
</dependency>
 <dependency>
     <groupId>org.thymeleaf.extras</groupId>
     <artifactId>thymeleaf-extras-springsecurity5</artifactId>
     <version>3.0.4.RELEASE</version>
</dependency>
```

> `spring-boot-starter-security` 和 `spring-security-test` 是所需最小的 Security  支持和测试支持。
>
> `spring-boot-starter-thymeleaf` 和`thymeleaf-extras-springsecurity5` 是集成Thymeleaf 模板引擎使用

### 6.2. 配置Security 支持

#### 6.2.1. 扩展 WebSecurityConfigurerAdapter 

Security官方提供了这样的[案例](https://spring.io/guides/gs/securing-web/)：

Here’s a security configuration that ensures that only authenticated users can see the secret greeting:

```java
package hello;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
      .authorizeRequests()
        .antMatchers("/", "/home").permitAll()
        .anyRequest().authenticated()
        .and()
      .formLogin()
        .loginPage("/login")
        .permitAll()
        .and()
      .logout()
        .permitAll();
  }

  @Bean
  @Override
  public UserDetailsService userDetailsService() {
    UserDetails user =
       User.withDefaultPasswordEncoder()
        .username("user")
        .password("password")
        .roles("USER")
        .build();

    return new InMemoryUserDetailsManager(user);
  }
}
```

可以通过这个来进行安全配置

因为它所提供的是 内存的用户`InMemoryUserDetailsManager` ，不是我们想要的，所以做出如下修改：

`src/main/java/top/capiudor/security/config/WebSecurityConfig.java`

```java
package top.capiudor.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import top.capiudor.security.dao.ResourcesRoleDTOMapper;
import top.capiudor.security.entity.RoleResourceDTO;
import java.util.List;

/**
 * @EnableWebSecurity 此注解已经包含 @Configuration 注解、不需要再加上
 * @EnableGlobalMethodSecurity 此注解是开启方法头上可以使用相关的sec注解
 */
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * 注入 ResourcesRoleDTOMapper，查询 URL 和 对应的角色
     */
    @Autowired
    private ResourcesRoleDTOMapper resourcesRoleDTOMapper;

    @Autowired
    @Qualifier("userDetailsServiceImpl")
    private UserDetailsService userDetailsService;


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        List<RoleResourceDTO> roleResourceDTOS = resourcesRoleDTOMapper.selectAllResourcesFKRole();
        http.authorizeRequests()
                .antMatchers("/").permitAll();
        // 遍历添加 角色与url 的关联
        for (RoleResourceDTO roleResourceDto :roleResourceDTOS) {
            /**
             * 这里使用 的 是  hasAuthority 方法  ，hasRole 默认前缀是  加上了 ROLE_
             * @See org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer#hasAnyRole(java.lang.String...)
             */
            http.authorizeRequests().antMatchers(roleResourceDto.getResourceUrl()).hasAuthority(roleResourceDto.getRoleName());
        }
        // 设置login方法
        http
                /**
                 * 这里可以对 login page（自定义登录界面）、
                 * defaultSuccessUrl（默认成功路径） 、
                 * failureHandler()对失败进行处理 、
                 * successHandler()对成功进行处理 、
                 * usernameParameter 、自定义Login页面的 用户名 输入框input name值
                 * passwordParameter 、自定义Login页面的 密码 输入框input name值
                 */
            .formLogin().usernameParameter("username").passwordParameter("password")
            .loginPage("/login")
            .defaultSuccessUrl("/");
        http
                /**
                 * 对 登出成功的路径进行设置
                 */
            .logout().logoutSuccessUrl("/");
        http
                /**
                 * 开启记住我功能
                 *  rememberMeParameter 设置 <code><input type="checkbox" name="remember"></code>  security 可以根据此进行识别是否记住我
                 */
            .rememberMe().rememberMeParameter("remember");
    }

    /**
     * 这里是Spring security 5.x 的新特性，必须使用 BCryptPasswordEncoder对密码进行加密处理
     * 如下所示：我们可以自己在注册的时候加密处理
     * <code>
     *      String password = "123456";
     *      String encode = new BCryptPasswordEncoder().encode(password);
     * </code>
     *
     * @param auth
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder());
    }
}
```

#### 6.2.2. Thymeleaf Security 方言的使用

在需要使用方言的HTML中引入  

```html
xmlns:sec="http://www.thymeleaf.org/extras/spring-security5"
```

这里面提供了一些很有用的api：

- ```
  sec:authorize="isAuthenticated()"  是否经过认证
  ```

- ```
  sec:authentication="name"      获取到登录的用户名
  sec:authentication="principal.authorities"   获取到用户所包含的所有角色
  ```

- ```
  sec:authorize="hasAuthority('super')"  是否存在角色为 'super'，这里可以使用  hasRole，但是不推荐
  ```

  > 这些API 中 返回 Boolean的决定着是否显示这个html标签
  >
  > 返回用户名角色等，可以在某个元素中显示，比如:
  >
  > `<span sec:authentication="principal.authorities"></span>`
  >
  > 它会在这个 `span` 元素中显示你所有的角色，如果没有则为空，但是元素依然存在
  >
  > 更多的API 请参考 Thymeleaf 官方文档  https://www.thymeleaf.org/doc/articles/springsecurity.html

## 7.HTML 引入和 HTML映射至 Boot

### 7.1. Login.html

```html
<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <title>登录页面</title>
</head>
<body>
<form method="post" th:action="@{/login}" action="/login">
    <label for="username">账号：</label>
    <input id="username" name="username" type="text"> <br>
    <label for="password">密码：</label>
    <input id="password" name="password" type="password"> <br>
    <input type="checkbox" name="remember"> 记住我 <br>
    <input type="submit" value="登录">
</form>
</body>
</html>
```

### 7.2. Welcome.html

```html
<!DOCTYPE html>
<html lang="en"
      xmlns:th="http://www.thymeleaf.org"
      xmlns:sec="http://www.thymeleaf.org/extras/spring-security5"
>
<head>
    <meta charset="UTF-8">
    <title>welcome</title>
</head>
<body>

<h1 align="center">欢迎光临阴曹地府管理系统</h1>
<div sec:authorize="!isAuthenticated()">
    <h2 align="center">您好，如果想进入阴曹地府 <a th:href="@{/toLogin}">请登录</a></h2>
</div>
<div sec:authorize="isAuthenticated()">
    <h2><span sec:authentication="name"></span>，您好,您的角色有：
        <span sec:authentication="principal.authorities"></span></h2>
    <form th:action="@{/logout}" method="post">
        <input type="submit" value="注销"/>
    </form>
</div>

<hr>

<div sec:authorize="hasAuthority('super')">
    <h3>最高神职</h3>
    <ul>
        <li><a th:href="@{/level1/1}">天齐仁圣大帝</a></li>
        <li><a th:href="@{/level1/2}">北阴酆都大帝</a></li>
        <li><a th:href="@{/level1/3}">五方鬼帝</a></li>
    </ul>

</div>

<div sec:authorize="hasAuthority('admin')">
    <h3>罗酆六天</h3>
    <ul>
        <li><a th:href="@{/level2/1}">宗灵七非天宫、敢司连宛屡天宫</a></li>
        <li><a th:href="@{/level2/2}">纣绝阴天宫、泰煞谅事宗天宫</a></li>
        <li><a th:href="@{/level2/3}">明晨耐犯武城天宫、恬昭罪气天宫</a></li>
    </ul>

</div>

<div sec:authorize="hasAuthority('user')">
    <h3>阴曹地府</h3>
    <ul>
        <li><a th:href="@{/level3/1}">秦广王、楚江王、宋帝王、仵官王</a></li>
        <li><a th:href="@{/level3/2}">阎罗王、平等王、泰山王</a></li>
        <li><a th:href="@{/level3/3}">都市王、卞城王、转轮王</a></li>
    </ul>
</div>


</body>
</html>
```

### 7.3. page

> 详情查看 `src/main/resources/templates/pages/**`



## 8. 小结

这个周花了3天的时间进行阅读 spring security 的文档，有些东西还是得实践才行，如果我不实践，可能不知道为什么 数据库的角色 和 我在Config中设置的角色不一致...

编写不易，希望这篇文章可以帮助到大家。如果大家能够提出我的问题或者值得优化的地方那便是对我最大的帮助。

























