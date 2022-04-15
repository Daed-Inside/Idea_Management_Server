package web.application.IdeaManagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import web.application.IdeaManagement.entity.IdeaViewCount;

@Repository
public interface IdeaViewCountRepository extends JpaRepository<IdeaViewCount, Long> {
    IdeaViewCount findByIdeaIdAndCreatedUser(Long id, String createdUser);
    Long countByIdeaId(Long id);
}
