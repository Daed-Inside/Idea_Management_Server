package web.application.IdeaManagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import web.application.IdeaManagement.entity.Permission;

import java.util.List;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long>, JpaSpecificationExecutor<Permission> {
    @Query("SELECT p.url \n" +
            "FROM Permission p \n" +
            "JOIN RolePermission rp ON rp.permissionId = p.id \n" +
            "JOIN Role rol ON rol.id = rp.roleId \n" +
            "JOIN rol.users u\n" +
            "WHERE u.userId = :userId"
    )
    List<String> listPermission(@Param("userId") String userId);
}
