package top.capiudor.security.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.capiudor.security.dao.ResourceRoleMapper;
import top.capiudor.security.entity.ResourceRole;
import top.capiudor.security.service.ResourceRoleService;

import java.util.List;

@Service
public class ResourceRoleServiceImpl implements ResourceRoleService {

    @Autowired
    private ResourceRoleMapper resourceRoleMapper;

    @Override
    public List<ResourceRole> selectAllResourceRole() {
        return resourceRoleMapper.selectAll();
    }
}
