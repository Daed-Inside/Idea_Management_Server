package web.application.IdeaManagement.specification;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import web.application.IdeaManagement.entity.Idea;
import web.application.IdeaManagement.entity.Topic;

import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Service
public class TopicSpecification {
    public Specification<Topic> filterTopic(String searchKey) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (!StringUtils.isEmpty(searchKey)) {
                try {
                    Long parseId = Long.parseLong(searchKey);
                    predicates.add(cb.or(cb.like(root.get("createdUser"), "%" + searchKey + "%"),
                            cb.like(root.get("topic"), "%" + searchKey + "%"),
                            cb.equal(root.get("id"), parseId)));
                } catch (Exception e) {
                    predicates.add(cb.or(cb.like(root.get("createdUser"), "%" + searchKey + "%"),
                            cb.like(root.get("topic"), "%" + searchKey + "%")));
                }
            }
            return cb.and(predicates.stream().toArray(Predicate[]::new));
        };
    }

    public Specification<Topic> filterTopicByIdea(Long ideaId) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            Root<Idea> rootIdea = query.from(Idea.class);
            cb.and(cb.equal(root.get("id"), rootIdea.get("topicId")),
                    cb.equal(rootIdea.get("id"), ideaId));
            return cb.and(predicates.stream().toArray(Predicate[]::new));
        };
    }
}
