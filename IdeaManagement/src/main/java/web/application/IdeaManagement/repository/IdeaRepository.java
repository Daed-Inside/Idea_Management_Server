package web.application.IdeaManagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import web.application.IdeaManagement.entity.Idea;
import web.application.IdeaManagement.model.response.IdeaDetailResponse;

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
}
