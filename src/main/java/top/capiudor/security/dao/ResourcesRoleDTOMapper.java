package top.capiudor.security.dao;

import org.apache.ibatis.annotations.Select;
import top.capiudor.security.entity.RoleResourceDTO;

import java.util.List;

public interface ResourcesRoleDTOMapper {

    @Select("SELECT " +
            "res_name,res_url,role_name " +
            "from resource res JOIN resource_role rr on rr.res_id = res.id " +
            "JOIN role r on r.id = rr.role_id " +
            "GROUP BY res.res_url")
    List<RoleResourceDTO> selectAllResourcesFKRole();
}
