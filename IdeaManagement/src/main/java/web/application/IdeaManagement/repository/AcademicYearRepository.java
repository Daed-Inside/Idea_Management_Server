package web.application.IdeaManagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import web.application.IdeaManagement.entity.AcademicYear;

import java.util.List;

public interface AcademicYearRepository extends JpaRepository<AcademicYear, Long>, JpaSpecificationExecutor<AcademicYear> {
    List<AcademicYear> findAllByYear(String year);
}
