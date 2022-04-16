package web.application.IdeaManagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import web.application.IdeaManagement.entity.Idea;
import web.application.IdeaManagement.model.response.DashboardResponse;
import web.application.IdeaManagement.model.response.ExcelExportResponse;
import web.application.IdeaManagement.model.response.IdeaDetailResponse;

import java.util.List;

@Repository
public interface IdeaRepository extends JpaRepository<Idea, Long>, JpaSpecificationExecutor<Idea> {
    Boolean existsByCategoryId(Long categoryId);

    @Query("SELECT NEW web.application.IdeaManagement.model.response.IdeaDetailResponse(" +
            "c.category, t.topic, i.ideaTitle, i.ideaContent, i.isAnonymous, i.createdUser, i.createdDate) \n" +
            "FROM Idea i \n" +
            "JOIN Topic t ON i.topicId = t.id \n" +
            "JOIN Category c ON i.categoryId = c.id \n" +
            "WHERE i.id = :id")
    IdeaDetailResponse getIdeaDetail(@Param("id") Long id);

    @Query("SELECT NEW web.application.IdeaManagement.model.response.DashboardResponse(" +
            "count(distinct idea.id), " +
            "sum(CASE WHEN idea.isAnonymous = true then 1 else 0 END) " +
            ") \n" +
            "FROM Idea idea \n" +
            "JOIN Topic topic ON idea.topicId = topic.id \n" +
            "JOIN Department dept ON dept.id = topic.departmentId \n" +
            "JOIN AcademicYear acay On topic.academicId = acay.id \n" +
            "WHERE ((:year is not null and :year = acay.year) or (:year is null)) \n" +
            "AND ((:semester is not null and :semester = acay.semester) or (:semester is null)) \n" +
            "AND ((:department is not null and :department = topic.departmentId) or (:department is null)) \n" +
            "AND ((:topic is not null and :topic = topic.id) or (:topic is null))")
    DashboardResponse getDashboard(@Param("year") String year, @Param("semester") String semester,
                                   @Param("department") Long department, @Param("topic") Long topic);

    @Query("SELECT dept.department, COUNT(distinct idea.id), COUNT(comt.id) \n" +
            "FROM Idea idea \n" +
            "JOIN Topic topic ON idea.topicId = topic.id \n" +
            "JOIN Department dept ON dept.id = topic.departmentId \n" +
            "JOIN AcademicYear acay On topic.academicId = acay.id \n" +
            "LEFT JOIN IdeaComment comt ON comt.ideaId = idea.id \n" +
            "WHERE ((:year is not null and :year = acay.year) or (:year is null)) \n" +
            "AND ((:semester is not null and :semester = acay.semester) or (:semester is null)) \n" +
            "AND ((:department is not null and :department = topic.departmentId) or (:department is null)) \n" +
            "AND ((:topic is not null and :topic = topic.id) or (:topic is null)) \n" +
            "GROUP BY dept.id")
    List<Object[]> FirstBarChart(@Param("year") String year, @Param("semester") String semester,
                                 @Param("department") Long department, @Param("topic") Long topic);

    @Query("SELECT dept.department, COUNT(idea.id) \n" +
            "FROM Idea idea \n" +
            "JOIN Topic topic ON idea.topicId = topic.id \n" +
            "JOIN Department dept ON dept.id = topic.departmentId \n" +
            "JOIN AcademicYear acay On topic.academicId = acay.id \n" +
            "LEFT JOIN IdeaComment comt ON comt.ideaId = idea.id \n" +
            "WHERE ((:year is not null and :year = acay.year) or (:year is null)) \n" +
            "AND ((:semester is not null and :semester = acay.semester) or (:semester is null)) \n" +
            "AND ((:department is not null and :department = topic.departmentId) or (:department is null)) \n" +
            "AND ((:topic is not null and :topic = topic.id) or (:topic is null)) \n" +
            "GROUP BY dept.id")
    List<Object[]> PieChart(@Param("year") String year, @Param("semester") String semester,
                                 @Param("department") Long department, @Param("topic") Long topic);

    @Query("SELECT NEW web.application.IdeaManagement.model.response.ExcelExportResponse(" +
            "acay.semester, dept.department, topic.topic, cate.category, topic.finalEndDate, idea.ideaTitle, \n" +
            "CONCAT(u.firstname, ' ', u.lastname), u.email, attach.downloadUrl" +
            ") \n" +
            "FROM Idea idea \n" +
            "JOIN Category cate ON cate.id = idea.categoryId \n" +
            "JOIN User u ON u.userId = idea.userId \n" +
            "JOIN Topic topic ON idea.topicId = topic.id \n" +
            "JOIN Department dept ON dept.id = topic.departmentId \n" +
            "JOIN AcademicYear acay On topic.academicId = acay.id \n" +
            "LEFT JOIN IdeaAttachment attach ON attach.ideaId = idea.id \n" +
            "WHERE ((:year is not null and :year = acay.year) or (:year is null)) \n" +
            "AND ((:semester is not null and :semester = acay.semester) or (:semester is null)) \n" +
            "AND ((:department is not null and :department = topic.departmentId) or (:department is null)) \n" +
            "AND ((:topic is not null and :topic = topic.id) or (:topic is null))")
    List<ExcelExportResponse> getExcelData(@Param("year") String year, @Param("semester") String semester,
                                           @Param("department") Long department, @Param("topic") Long topic);

}
