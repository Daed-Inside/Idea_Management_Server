package web.application.IdeaManagement.specification;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import web.application.IdeaManagement.entity.AcademicYear;
import web.application.IdeaManagement.entity.Department;
import web.application.IdeaManagement.entity.Idea;
import web.application.IdeaManagement.entity.Topic;
import web.application.IdeaManagement.model.response.TopicResponse;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TopicSpecification {
    @PersistenceContext
    private EntityManager entityManager;

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

    public Map<String, Object> getListTopic(String searchKey, Long departmentId, Integer page, Integer limit, String sortBy, String sortType) {
        try {
            Map<String, Object> mapFinal = new HashMap<>();
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<TopicResponse> query = cb.createQuery(TopicResponse.class);
            Root<Topic> root = query.from(Topic.class);
            Root<Department> rootDept = query.from(Department.class);
            Root<AcademicYear> rootAcad = query.from(AcademicYear.class);
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.equal(root.get("academicId"), rootAcad.get("id")));
            predicates.add(cb.equal(root.get("departmentId"), rootDept.get("id")));
            if (departmentId != null) {
                predicates.add(cb.equal(root.get("departmentId"), departmentId));
            }
            if (!StringUtils.isEmpty(searchKey)) {
                predicates.add(cb.or(cb.like(cb.lower(root.get("topic")), "%" + searchKey.toLowerCase() + "%"),
                        cb.like(cb.lower(rootDept.get("department")), "%" + searchKey.toLowerCase() + "%"),
                        cb.like(cb.lower(rootAcad.get("semester")), "%" + searchKey.toLowerCase() + "%")));
            }

            //----------------------SUBQUERY COUNT-----------------------------//
            CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
            Root<Topic> rootCount = countQuery.from(Topic.class);

            Root<Department> rootDeptCount = countQuery.from(Department.class);
            Root<AcademicYear> rootAcadCount = countQuery.from(AcademicYear.class);
            List<Predicate> predicatesCount = new ArrayList<>();
            predicatesCount.add(cb.equal(rootCount.get("academicId"), rootAcadCount.get("id")));
            predicatesCount.add(cb.equal(rootCount.get("departmentId"), rootDeptCount.get("id")));
            if (!StringUtils.isEmpty(searchKey)) {
                predicatesCount.add(cb.or(cb.like(cb.lower(rootCount.get("topic")), "%" + searchKey.toLowerCase() + "%"),
                        cb.like(cb.lower(rootDeptCount.get("department")), "%" + searchKey.toLowerCase() + "%"),
                        cb.like(cb.lower(rootAcadCount.get("semester")), "%" + searchKey.toLowerCase() + "%")));
            }

            //------------------------CREATE SORT-----------------------------//
            if (sortType.equalsIgnoreCase("asc")) {
                switch (sortBy) {
                    case "id":
                        query.orderBy(cb.asc(root.get("id")));
                        break;
                    case "topic":
                        query.orderBy(cb.asc(root.get("topic")));
                        break;
                }
            } else {
                switch (sortBy) {
                    case "id":
                        query.orderBy(cb.desc(root.get("id")));
                        break;
                    case "topic":
                        query.orderBy(cb.desc(root.get("topic")));
                        break;
                }
            }
            //----------------------END SORT-----------------------------//
            query.multiselect(
                    root.get("id"),
                    root.get("topic"),
                    rootAcad.get("year"),
                    rootAcad.get("semester"),
                    rootDept.get("department"),
                    root.get("startDate"),
                    root.get("endDate"),
                    root.get("finalEndDate")
            ).where(cb.and(predicates.stream().toArray(Predicate[]::new)));
            List<TopicResponse> listResult = entityManager.createQuery(query) != null ? entityManager.createQuery(query).
                    setFirstResult((page - 1) * limit)
                    .setMaxResults(limit).getResultList() : new ArrayList<>();
            countQuery.select(cb.count(rootCount)).where(cb.and(predicatesCount.stream().toArray(Predicate[]::new)));
            Long count = entityManager.createQuery(countQuery).getSingleResult();
            mapFinal.put("data", listResult);
            mapFinal.put("count", count);
            return mapFinal;
        } catch (Exception e) {
            e.printStackTrace();
            return new HashMap<>();
        }
    }
}
