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
