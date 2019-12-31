package top.capiudor.security.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.capiudor.security.dao.RoleMapper;
import top.capiudor.security.entity.Role;
import top.capiudor.security.service.RoleService;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleMapper roleMapper;

    @Override
    public List<Role> selectAllRoles() {
        return roleMapper.selectAll();
    }
}
