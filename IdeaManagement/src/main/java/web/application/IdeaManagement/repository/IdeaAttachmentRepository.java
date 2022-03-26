package web.application.IdeaManagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import web.application.IdeaManagement.entity.IdeaAttachment;

import java.util.List;

@Repository
public interface IdeaAttachmentRepository extends JpaRepository<IdeaAttachment, Long>, JpaSpecificationExecutor<IdeaAttachment> {
    List<IdeaAttachment> findByIdeaId(Long id);
}
