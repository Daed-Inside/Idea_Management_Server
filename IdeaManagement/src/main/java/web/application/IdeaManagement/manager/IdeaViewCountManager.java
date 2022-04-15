package web.application.IdeaManagement.manager;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import web.application.IdeaManagement.entity.IdeaViewCount;
import web.application.IdeaManagement.repository.IdeaReactionRepository;
import web.application.IdeaManagement.repository.IdeaViewCountRepository;
import web.application.IdeaManagement.utils.ResponseUtils;

import java.util.Date;

@Service
@Transactional
public class IdeaViewCountManager {
    @Autowired
    ResponseUtils responseUtils;
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    IdeaViewCountRepository ideaViewCountRepo;

    public Integer countView(Long ideaId, String email) {
        try {
            IdeaViewCount existCount = ideaViewCountRepo.findByIdeaIdAndCreatedUser(ideaId, email);
            if (existCount != null) {
                existCount.setLatestViewDate(new Date());
                ideaViewCountRepo.save(existCount);
            } else {
                IdeaViewCount newCount = new IdeaViewCount();
                newCount.setCreatedDate(new Date());
                newCount.setCreatedUser(email);
                newCount.setLatestViewDate(new Date());
                newCount.setIdeaId(ideaId);
                ideaViewCountRepo.save(newCount);
            }
            return 1;
        } catch (Exception e) {
            return 0;
        }
    }

}
