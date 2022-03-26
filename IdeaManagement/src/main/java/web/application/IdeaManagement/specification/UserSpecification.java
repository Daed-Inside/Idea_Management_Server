package web.application.IdeaManagement.specification;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import web.application.IdeaManagement.entity.Idea;
import web.application.IdeaManagement.entity.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserSpecification {
    @PersistenceContext
    EntityManager entityManager;

    public Specification<User> filter(String searchKey) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (!StringUtils.isEmpty(searchKey)) {
                predicates.add(cb.or(
                        cb.like(root.get("email"), "%" + searchKey + "%"),
                        cb.like(root.get("firstname"), "%" + searchKey + "%"),
                        cb.like(root.get("lastname"), "%" + searchKey + "%"),
                        cb.like(root.get("userId"), "%" + searchKey + "%")
                ));
            }
            return cb.and(predicates.stream().toArray(Predicate[]::new));
        };
    }
}
