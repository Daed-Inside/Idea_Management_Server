package web.application.IdeaManagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import web.application.IdeaManagement.entity.IdeaAttachment;

public interface IdeaAttachmentRepository extends JpaRepository<IdeaAttachment, Long>, JpaSpecificationExecutor<IdeaAttachment> {
}
