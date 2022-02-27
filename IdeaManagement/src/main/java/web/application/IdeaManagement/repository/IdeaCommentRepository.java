package web.application.IdeaManagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import web.application.IdeaManagement.entity.IdeaComment;

public interface IdeaCommentRepository extends JpaRepository<IdeaComment, Long>, JpaSpecificationExecutor<IdeaComment> {
}
