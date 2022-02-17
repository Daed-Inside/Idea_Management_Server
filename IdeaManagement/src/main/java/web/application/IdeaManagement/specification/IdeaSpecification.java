package web.application.IdeaManagement.specification;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.apache.commons.lang3.StringUtils;
import web.application.IdeaManagement.entity.Idea;
import web.application.IdeaManagement.model.response.IdeaResponseModel;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Service
public class IdeaSpecification {
    @PersistenceContext
    EntityManager entityManager;

    public List<IdeaResponseModel> getIdeaWithModel(String searchKey) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        List<Predicate> predicates = new ArrayList<>();
        CriteriaQuery<IdeaResponseModel> query = cb.createQuery(IdeaResponseModel.class);
        Root<Idea> root = query.from(Idea.class);

        if (!StringUtils.isEmpty(searchKey)) {
            predicates.add(cb.like(root.get("ideaContent"), "%" + searchKey + "%"));
        }
        query.multiselect(
                root.get("id"),
                root.get("ideaTitle"),
                root.get("ideaContent")
        ).where(cb.and(predicates.stream().toArray(Predicate[]::new)));
        List<IdeaResponseModel> listResult = entityManager.createQuery(query) != null ? entityManager.createQuery(query).getResultList() : new ArrayList<>();
        return listResult;
    }

    public Specification<Idea> filterIdea(String searchKey) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (!StringUtils.isEmpty(searchKey)) {
                predicates.add(cb.like(root.get("ideaContent"), "%" + searchKey + "%"));
            }
            return cb.and(predicates.stream().toArray(Predicate[]::new));
        };
    }
}
