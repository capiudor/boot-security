package top.capiudor.security.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.capiudor.security.dao.ResourceMapper;
import top.capiudor.security.entity.Resource;
import top.capiudor.security.service.ResourceService;

import java.util.List;

@Service
public class ResourceServiceImpl implements ResourceService {

    @Autowired
    private ResourceMapper resourceMapper;

    @Override
    public List<Resource> selectResources() {
        return resourceMapper.selectAll();
    }
}
