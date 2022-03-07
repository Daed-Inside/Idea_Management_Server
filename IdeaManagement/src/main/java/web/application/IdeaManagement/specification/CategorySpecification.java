package web.application.IdeaManagement.specification;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import web.application.IdeaManagement.entity.Category;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

@Service
public class CategorySpecification {
    @PersistenceContext
    EntityManager entityManager;


    public Specification<Category> filterCategory(String searchKey){
        //cb.isFalse(root.get("isDeleted").as(Boolean.class))
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (!StringUtils.isEmpty(searchKey)) {
                try {
                    Long parseId = Long.parseLong(searchKey);
                    predicates.add(
                            cb.or(
                                    cb.like(root.get("createdUser"), "%" + searchKey + "%"),
                                    cb.like(root.get("category"), "%" + searchKey + "%"),
                                    cb.equal(root.get("id"), parseId)
                            )
                    );
//                    predicates.add(cb.isFalse(root.get("isDeleted").as(Boolean.class)));
                } catch (Exception e) {
                    predicates.add(
                            cb.or(
                                    cb.like(root.get("createdUser"), "%" + searchKey + "%"),
                                    cb.like(root.get("category"), "%" + searchKey + "%")
                            )
                    );
//                    predicates.add(cb.isFalse(root.get("isDeleted").as(Boolean.class)));
                }
            }
            return cb.and(predicates.stream().toArray(Predicate[]::new));
        };
    }
}
