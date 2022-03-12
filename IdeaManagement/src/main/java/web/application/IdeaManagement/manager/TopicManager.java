package web.application.IdeaManagement.manager;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import web.application.IdeaManagement.dto.PageDto;
import web.application.IdeaManagement.entity.Topic;
import web.application.IdeaManagement.model.request.TopicRequest;
import web.application.IdeaManagement.model.response.TopicResponse;
import web.application.IdeaManagement.repository.TopicRepository;
import web.application.IdeaManagement.specification.TopicSpecification;
import web.application.IdeaManagement.utils.ResponseUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class TopicManager {
    @Autowired
    TopicRepository topicRepository;
    @Autowired
    TopicSpecification topicSpecification;
    @Autowired
    ResponseUtils responseUtils;
    @Autowired
    ModelMapper modelMapper;

    public Integer createTopic(TopicRequest req) {
        try {
            Topic topic = modelMapper.map(req, Topic.class);
            topic.setCreatedDate(new Date());
            topic.setStartDate(new Date());
            topicRepository.save(topic);
            return 1;
        } catch (Exception e) {
            return 0;
        }
    }

//    public PageDto getTopic(String searchKey, Integer page, Integer limit, String sortBy, String sortType) {
//        try {
//            Sort sort = responseUtils.getSort(sortBy, sortType);
//            Integer pageNum = page - 1;
//            Page<Topic> pageTopic = topicRepository.findAll(topicSpecification.filterTopic(searchKey), PageRequest.of(pageNum, limit, sort));
//            return PageDto.builder()
//                    .content(pageTopic.getContent())
//                    .numberOfElements(pageTopic.getNumberOfElements())
//                    .page(page)
//                    .size(pageTopic.getSize())
//                    .totalPages(pageTopic.getTotalPages())
//                    .totalElements(pageTopic.getTotalElements())
//                    .build();
//        } catch (Exception e) {
//            return null;
//        }
//    }

    public PageDto getTopic(String searchKey, Integer page, Integer limit, String sortBy, String sortType) {
        try {
            Map<String, Object> mapTopic = topicSpecification.getListTopic(searchKey, page, limit, sortBy, sortType);
            List<TopicResponse> listRes = (List<TopicResponse>) mapTopic.get("data");
            Long totalItems = (Long) mapTopic.get("count");
            Integer totalPage = responseUtils.getPageCount(totalItems, limit);
            return PageDto.builder()
                    .content(listRes)
                    .numberOfElements(Math.toIntExact(totalItems))
                    .page(page)
                    .size(limit)
                    .totalPages(totalPage)
                    .totalElements(totalItems)
                    .build();
        } catch (Exception e) {
            return null;
        }
    }
}
