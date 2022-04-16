package web.application.IdeaManagement.manager;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import web.application.IdeaManagement.entity.IdeaComment;
import web.application.IdeaManagement.entity.IdeaReaction;
import web.application.IdeaManagement.model.response.IdeaStatusCountResponse;
import web.application.IdeaManagement.repository.IdeaCommentRepository;
import web.application.IdeaManagement.repository.IdeaReactionRepository;
import web.application.IdeaManagement.repository.IdeaViewCountRepository;
import web.application.IdeaManagement.utils.ResponseUtils;

import java.util.Date;

@Service
@Transactional
public class IdeaReactionManager {
    @Autowired
    ResponseUtils responseUtils;
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    IdeaReactionRepository ideaReactionRepo;
    @Autowired
    IdeaViewCountRepository ideaViewCountRepo;
    @Autowired
    IdeaCommentRepository ideaCommentRepo;

    public Integer ReactIdea(Long ideaId, String email, Integer reaction) {
        try {
            IdeaReaction existReaction = ideaReactionRepo.findByIdeaIdAndCreatedUser(ideaId, email);
            if (existReaction != null && reaction == existReaction.getEvaluation()) {
                existReaction.setEvaluation(0);
                ideaReactionRepo.save(existReaction);
            } else if (existReaction != null && reaction != existReaction.getEvaluation()) {
                existReaction.setEvaluation(reaction);
                ideaReactionRepo.save(existReaction);
            }
            else {
                IdeaReaction newReaction = new IdeaReaction();
                newReaction.setIdeaId(ideaId);
                newReaction.setCreatedDate(new Date());
                newReaction.setCreatedUser(email);
                newReaction.setEvaluation(reaction);
                ideaReactionRepo.save(newReaction);
            }
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public IdeaStatusCountResponse getIdeaStatus(Long ideaId, String email) {
        try {
            Long likeCount = ideaReactionRepo.countByIdeaIdAndEvaluation(ideaId, 1);
            Long dislikeCount = ideaReactionRepo.countByIdeaIdAndEvaluation(ideaId, -1);
            IdeaReaction existReaction = ideaReactionRepo.findByIdeaIdAndCreatedUser(ideaId, email);
            Long viewCount = ideaViewCountRepo.countByIdeaId(ideaId);
            Long commentCount = ideaCommentRepo.countByIdeaId(ideaId);
            IdeaStatusCountResponse newRes = new IdeaStatusCountResponse();
            newRes.setLike(likeCount);
            newRes.setDislike(dislikeCount);
            newRes.setCurrentStatus(existReaction != null ? existReaction.getEvaluation() : 0);
            newRes.setViewCount(viewCount);
            newRes.setCommentCount(commentCount);
            return newRes;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
