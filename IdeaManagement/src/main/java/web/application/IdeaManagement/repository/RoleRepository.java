package web.application.IdeaManagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import web.application.IdeaManagement.entity.Role;

import java.util.List;

public interface RoleRepository extends JpaRepository<Role, Long>, JpaSpecificationExecutor<Role> {
    Role findByName(String role);
    Role findRoleById(Long id);
//    Role findByName(String name);
    Boolean existsByName(String role);
    List<Role> getRoleByNameIn(List<String> names);
    void deleteByNameIn(List<String> names);
}
