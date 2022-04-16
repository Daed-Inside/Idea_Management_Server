package web.application.IdeaManagement.manager;

import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import web.application.IdeaManagement.dto.PageDto;
import web.application.IdeaManagement.entity.IdeaComment;
import web.application.IdeaManagement.entity.Role;
import web.application.IdeaManagement.entity.Topic;
import web.application.IdeaManagement.entity.User;
import web.application.IdeaManagement.model.request.CommentRequest;
import web.application.IdeaManagement.model.response.IdeaCommentResponse;
import web.application.IdeaManagement.repository.IdeaCommentRepository;
import web.application.IdeaManagement.repository.TopicRepository;
import web.application.IdeaManagement.repository.UserRepository;
import web.application.IdeaManagement.specification.CommentSpecification;
import web.application.IdeaManagement.specification.TopicSpecification;
import web.application.IdeaManagement.utils.ResponseUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class IdeaCommentManager {
    @Autowired
    IdeaCommentRepository ideaCommentRepo;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ResponseUtils responseUtils;
    @Autowired
    TopicRepository topicRepository;
    @Autowired
    TopicSpecification topicSpec;
    @Autowired
    CommentSpecification commentSpec;
    @Autowired
    ModelMapper modelMapper;

    public IdeaComment createComment(CommentRequest request, String userId, String email) {
        try {
            List<Topic> topic = topicRepository.findAll(topicSpec.filterTopicByIdea(request.getIdeaId()));
            IdeaComment newCmt = new IdeaComment();
            if (!topic.isEmpty()) {
                if (topic.get(0).getFinalEndDate().before(new Date())) {
                    newCmt.setId(-2l);
                    return newCmt;
                }
                IdeaComment ideaComment = new IdeaComment();
                ideaComment.setUserId(userId);
                ideaComment.setParent(request.getParentId());
                ideaComment.setRootParent(request.getRootParent());
                ideaComment.setIdeaId(request.getIdeaId());
                ideaComment.setContent(request.getContent());
                ideaComment.setIsAnonymous(request.getIsAnonymous());
                ideaComment.setCreatedDate(new Date());
                ideaComment.setCreatedUser(email);
                ideaComment.setUserId(userId);
                newCmt = ideaCommentRepo.save(ideaComment);
            }
            return newCmt;
        } catch (Exception e) {
            e.printStackTrace();
            return new IdeaComment();
        }
    }

    public Integer editComment(Long id, String content, String email) {
        try {
            IdeaComment existCmt = ideaCommentRepo.findById(id).get();
            List<Topic> topic = topicRepository.findAll(topicSpec.filterTopicByIdea(existCmt.getIdeaId()));
            if (!topic.isEmpty()) {
                if (topic.get(0).getFinalEndDate().before(new Date())) {
                    return -2;
                }
                existCmt.setContent(content);
                existCmt.setModifiedUser(email);
                existCmt.setModifiedDate(new Date());
                ideaCommentRepo.save(existCmt);
            }
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public Integer deleteComt(Long id) {
        try {
            ideaCommentRepo.deleteById(id);
            ideaCommentRepo.updateParent(id);
            return 1;
        } catch (Exception e) {
            return 0;
        }
    }

    public List<IdeaCommentResponse> getComment(Long ideaId, String userId) {
        try {
            User currUser = userRepository.findById(userId).get();
            List<Role> roles = new ArrayList<>(currUser.getRoles());
            Boolean checkAdmin = false;
            if (!roles.isEmpty() && roles.get(0).getId() == 1l) {
                checkAdmin = true;
            }
            List<IdeaComment> listFinal = ideaCommentRepo.findByIdeaId(ideaId);

            Boolean finalCheckAdmin = checkAdmin;
            return listFinal.stream().map(x -> {
                IdeaCommentResponse newResponse = modelMapper.map(x, IdeaCommentResponse.class);
                if (finalCheckAdmin == false && x.getIsAnonymous() == true && !x.getUserId().equals(userId)) {
                    newResponse.setCreatedUser("anonymous");
                }
                return newResponse;
            }).collect(Collectors.toList());
        } catch (Exception e) {
            return null;
        }
    }
}
