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


//    public List<CategoryResponseModel> getCategoryWithModel(String searchKey) {
//        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
//        List<Predicate> predicates = new ArrayList<>();
//        CriteriaQuery<CategoryResponseModel> query = cb.createQuery(CategoryResponseModel.class);
//        Root<Category> root = query.from(Category.class);
//
//        if (!StringUtils.isEmpty(searchKey)) {
//            predicates.add(cb.like(root.get("category"), "%" + searchKey + "%"));
//        }
//
//
//
//
//    }

    public Specification<Category> filterCategory(String searchKey){
        return (root, query, cb) -> {
             List<Predicate> predicates = new ArrayList<>();
            if (!StringUtils.isEmpty(searchKey)) {
                predicates.add(cb.like(root.get("category"), "%" + searchKey + "%"));
            }
            return cb.and(predicates.stream().toArray(Predicate[]::new));
        };
    }
}
