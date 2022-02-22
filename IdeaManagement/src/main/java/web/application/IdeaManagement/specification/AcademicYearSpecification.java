package web.application.IdeaManagement.specification;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import web.application.IdeaManagement.entity.AcademicYear;
import web.application.IdeaManagement.entity.Idea;
import web.application.IdeaManagement.model.response.AcademicYearResponseModel;
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
public class AcademicYearSpecification {

    @PersistenceContext
    EntityManager entityManager;


    public List<AcademicYearResponseModel> getAcademicYearWithModel(String searchKey) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        List<Predicate> predicates = new ArrayList<>();
        CriteriaQuery<AcademicYearResponseModel> query = cb.createQuery(AcademicYearResponseModel.class);
        Root<AcademicYear> root = query.from(AcademicYear.class);
        if (!StringUtils.isEmpty(searchKey)) {
            predicates.add(cb.like(root.get("semester"), "%" + searchKey + "%"));
        }
        query.multiselect(
                root.get("id"),
                root.get("semester"),
                root.get("year")
        ).where(cb.and(predicates.stream().toArray(Predicate[]::new)));

        List<AcademicYearResponseModel> listResult = entityManager.createQuery(query) != null ? entityManager.createQuery(query).getResultList() : new ArrayList<>();
        return listResult;
    }

    public Specification<AcademicYear> filterAcademicYear(String searchKey) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (!StringUtils.isEmpty(searchKey)) {
                predicates.add(cb.like(root.get("semester"), "%" + searchKey + "%"));
            }
            return cb.and(predicates.stream().toArray(Predicate[]::new));
        };
    }
}
