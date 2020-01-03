package top.capiudor.security.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import top.capiudor.security.dao.RoleMapper;
import top.capiudor.security.dao.UserMapper;
import top.capiudor.security.entity.Role;
import top.capiudor.security.entity.User;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userMapper.selectByUsername(username);
        if (null == user){
            throw new UsernameNotFoundException("用户不存在");
        }
        // 将数据库形式的Roles解析为UserDetails的权限集
        user.setAuthorities(generateAuthorities(user.getId()));
        return user;
    }

    /**
     * 获取用户的角色信息
     * @param userId
     * @return
     */
    private List<GrantedAuthority> generateAuthorities(Integer userId){
        List<GrantedAuthority> authorities = new ArrayList<>();
        List<Role> roles = roleMapper.selectRolesByUserId(userId);
        if (null != roles){
            for (Role role : roles){
                authorities.add(new SimpleGrantedAuthority(role.getRoleName()));
            }
        }
        return authorities;
    }


}
