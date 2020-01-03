package top.capiudor.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import top.capiudor.security.dao.ResourcesRoleDTOMapper;
import top.capiudor.security.dao.RoleMapper;
import top.capiudor.security.entity.Role;
import top.capiudor.security.entity.RoleResourceDTO;

import java.util.List;

@SpringBootTest
class BootSecurityApplicationTests {

    @Autowired
    private ResourcesRoleDTOMapper roleMapper;

    @Test
    void contextLoads() {
        String password = "123456";
        String encode = new BCryptPasswordEncoder().encode(password);
        System.out.println(encode);
    }

}
