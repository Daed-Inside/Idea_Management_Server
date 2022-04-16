package web.application.IdeaManagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import web.application.IdeaManagement.entity.IdeaComment;

import java.util.List;

public interface IdeaCommentRepository extends JpaRepository<IdeaComment, Long>, JpaSpecificationExecutor<IdeaComment> {
    Long countByIdeaId(Long id);
    List<IdeaComment> findByIdeaId(Long id);

    @Modifying
    @Query("UPDATE IdeaComment cmt SET cmt.parent = null WHERE cmt.parent = :id")
    void updateParent(@Param("id") Long id);
}
