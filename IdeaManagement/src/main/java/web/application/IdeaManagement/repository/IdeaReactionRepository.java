package web.application.IdeaManagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import web.application.IdeaManagement.entity.IdeaReaction;

@Repository
public interface IdeaReactionRepository extends JpaRepository<IdeaReaction, Long> {
    IdeaReaction findByIdeaIdAndCreatedUser(Long id, String createdUser);

    Long countByIdeaIdAndEvaluation(Long id, Integer evaluation);
}
