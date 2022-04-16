package web.application.IdeaManagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import web.application.IdeaManagement.entity.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String>, JpaSpecificationExecutor<User> {
    Optional<User> findByUsername(String username);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
    User findByEmail(String email);

    @Query("SELECT u \n" +
    "FROM User u \n" +
    "JOIN u.roles rol \n" +
    "Where u.departmentId = :id AND rol.id = 2l")
    List<User> getQAMagByDept(@Param("id") Long id);

    @Query("SELECT dept.department, COUNT(u.id) \n" +
    "FROM User u \n" +
    "JOIN Department dept ON dept.id = u.departmentId \n" +
    "GROUP BY dept.id ")
    List<Object[]> countUserByDept();

}
