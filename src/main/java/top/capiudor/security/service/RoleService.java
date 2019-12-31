package top.capiudor.security.service;

import top.capiudor.security.entity.Role;

import java.util.List;

public interface RoleService {
    List<Role> selectAllRoles();
}
