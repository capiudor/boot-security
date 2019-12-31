package top.capiudor.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import top.capiudor.security.dao.ResourcesRoleDTOMapper;
import top.capiudor.security.entity.Resource;
import top.capiudor.security.entity.ResourceRole;
import top.capiudor.security.entity.Role;
import top.capiudor.security.entity.RoleResourceDTO;
import top.capiudor.security.service.ResourceRoleService;
import top.capiudor.security.service.ResourceService;
import top.capiudor.security.service.RoleService;

import javax.sql.DataSource;
import java.util.List;

/**
 * @EnableWebSecurity 此注解已经包含 @Configuration 注解、不需要再加上
 * @EnableGlobalMethodSecurity 此注解是开启方法头上可以使用相关的sec注解
 */
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private ResourcesRoleDTOMapper resourcesRoleDTOMapper;

    @Autowired
    private DataSource dataSource;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        List<RoleResourceDTO> roleResourceDTOS = resourcesRoleDTOMapper.selectAllResourcesFKRole();
        http.authorizeRequests()
                .antMatchers("/").permitAll();
        // 遍历添加 角色与url 的关联
        for (RoleResourceDTO roleResourceDto :roleResourceDTOS) {
            http.authorizeRequests().antMatchers(roleResourceDto.getResourceUrl()).hasRole(roleResourceDto.getRoleName());
        }
        // 设置login方法
        http
            .formLogin().usernameParameter("username").passwordParameter("password")
            .loginPage("/toLogin");
        http
            .logout().logoutSuccessUrl("/");
        http
            .rememberMe().rememberMeParameter("remember");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication()
                .dataSource(dataSource)
                .withDefaultSchema();  // 继续深入

    }
}
