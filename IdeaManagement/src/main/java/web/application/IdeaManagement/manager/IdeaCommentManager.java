package web.application.IdeaManagement.manager;

import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import web.application.IdeaManagement.dto.PageDto;
import web.application.IdeaManagement.entity.IdeaComment;
import web.application.IdeaManagement.entity.Topic;
import web.application.IdeaManagement.model.request.CommentRequest;
import web.application.IdeaManagement.repository.IdeaCommentRepository;
import web.application.IdeaManagement.repository.TopicRepository;
import web.application.IdeaManagement.specification.CommentSpecification;
import web.application.IdeaManagement.specification.TopicSpecification;
import web.application.IdeaManagement.utils.ResponseUtils;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class IdeaCommentManager {
    @Autowired
    IdeaCommentRepository ideaCommentRepo;
    @Autowired
    ResponseUtils responseUtils;
    @Autowired
    TopicRepository topicRepository;
    @Autowired
    TopicSpecification topicSpec;
    @Autowired
    CommentSpecification commentSpec;

    public Integer createComment(CommentRequest request, String userId, String email) {
        try {
            List<Topic> topic = topicRepository.findAll(topicSpec.filterTopicByIdea(request.getIdeaId()));
            if (!topic.isEmpty()) {
                if (topic.get(0).getFinalEndDate().before(new Date())) {
                    return -2;
                }
                IdeaComment ideaComment = new IdeaComment();
                ideaComment.setParent(request.getParentId());
                ideaComment.setRootParent(request.getRootParent());
                ideaComment.setIdeaId(request.getIdeaId());
                ideaComment.setUserId(userId);
                ideaComment.setContent(request.getContent());
                ideaComment.setIsAnonymous(request.getIsAnonymous());
                ideaComment.setCreatedDate(new Date());
                ideaComment.setCreatedUser(email);
                ideaCommentRepo.save(ideaComment);
            }
            return 1;
        } catch (Exception e) {
            return 0;
        }
    }

    public PageDto getComment(Long ideaId, String search, Integer page, Integer limit, String sortBy, String sortType) {
        try {
            Integer pageNum = page - 1;
            Sort sort = responseUtils.getSort(sortBy, sortType);
            Page<IdeaComment> pageComment = ideaCommentRepo.findAll(commentSpec.filterComment(search), PageRequest.of(pageNum, limit, sort));
            return PageDto.builder()
                    .content(pageComment.getContent())
                    .numberOfElements(pageComment.getNumberOfElements())
                    .page(page)
                    .size(pageComment.getSize())
                    .totalPages(pageComment.getTotalPages())
                    .totalElements(pageComment.getTotalElements())
                    .build();
        } catch (Exception e) {
            return null;
        }
    }
}
