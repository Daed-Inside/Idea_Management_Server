package web.application.IdeaManagement.specification;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import web.application.IdeaManagement.entity.Idea;
import web.application.IdeaManagement.entity.IdeaComment;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

@Service
public class CommentSpecification {
    public Specification<IdeaComment> filterComment(String searchKey) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (!StringUtils.isEmpty(searchKey)) {
                predicates.add(cb.like(root.get("content"), "%" + searchKey + "%"));
            }
            return cb.and(predicates.stream().toArray(Predicate[]::new));
        };
    }
}
