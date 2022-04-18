package web.application.IdeaManagement.specification;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import web.application.IdeaManagement.entity.AcademicYear;
import web.application.IdeaManagement.entity.Idea;
import web.application.IdeaManagement.model.response.AcademicYearReponse;
import web.application.IdeaManagement.model.response.IdeaResponseModel;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class AcademicYearSpecification {

    @PersistenceContext
    EntityManager entityManager;


    public Specification<AcademicYear> filterAcademicYear(String searchKey) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.greaterThan(root.get("endDate"), new Date()));
            if (!StringUtils.isEmpty(searchKey)) {
                try {
                    Long parseId = Long.parseLong(searchKey);
                    predicates.add(cb.or(cb.like(root.get("createdUser"), "%" + searchKey + "%"),
                            cb.like(root.get("year"), "%" + searchKey + "%"),
                            cb.like(root.get("semester"), "%" + searchKey + "%"),
                            cb.equal(root.get("id"), parseId)));
                } catch (Exception e) {
                    predicates.add(cb.or(cb.like(root.get("createdUser"), "%" + searchKey + "%"),
                            cb.like(root.get("year"), "%" + searchKey + "%"),
                            cb.like(root.get("semester"), "%" + searchKey + "%")));
                }
            }
            return cb.and(predicates.stream().toArray(Predicate[]::new));
        };
    }

    public List<AcademicYearReponse> getSemeserByYear(String year) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        List<Predicate> predicates = new ArrayList<>();
        CriteriaQuery<AcademicYearReponse> query = cb.createQuery(AcademicYearReponse.class);
        Root<AcademicYear> root = query.from(AcademicYear.class);
        if (!StringUtils.isEmpty(year)) {
            predicates.add(cb.like(root.get("year"), "%" + year + "%"));
        }
        query.multiselect(
                root.get("id"),
                root.get("semester"),
                root.get("year"),
                root.get("startDate"),
                root.get("endDate")
        ).where(cb.and(predicates.stream().toArray(Predicate[]::new)));

        List<AcademicYearReponse> listResult = entityManager.createQuery(query) != null ? entityManager.createQuery(query).getResultList() : new ArrayList<>();
        return listResult;
    }
}
