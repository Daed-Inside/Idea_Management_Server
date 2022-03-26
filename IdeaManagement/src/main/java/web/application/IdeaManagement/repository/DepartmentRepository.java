package web.application.IdeaManagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import web.application.IdeaManagement.entity.AcademicYear;
import web.application.IdeaManagement.entity.Department;

import java.util.List;

public interface DepartmentRepository extends JpaRepository<Department, Long>, JpaSpecificationExecutor<Department> {
}
