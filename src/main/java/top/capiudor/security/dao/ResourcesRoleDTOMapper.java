package top.capiudor.security.dao;

import org.apache.ibatis.annotations.Select;
import top.capiudor.security.entity.RoleResourceDTO;

import java.util.List;

public interface ResourcesRoleDTOMapper {

    List<RoleResourceDTO> selectAllResourcesFKRole();
}
