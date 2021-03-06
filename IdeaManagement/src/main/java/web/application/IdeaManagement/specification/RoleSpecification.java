package web.application.IdeaManagement.specification;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import web.application.IdeaManagement.entity.Idea;
import web.application.IdeaManagement.entity.Role;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

@Service
public class RoleSpecification {

    public Specification<Role> filter(String searchKey, Boolean selectBox) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (selectBox == null || (selectBox != null && selectBox == false)) {
                predicates.add(cb.equal(root.get("system"), 0) );
            }
            if (!StringUtils.isEmpty(searchKey)) {
                predicates.add(cb.like(root.get("name"), "%" + searchKey + "%"));
            }
            return cb.and(predicates.stream().toArray(Predicate[]::new));
        };
    }
}
