package web.application.IdeaManagement.specification;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import web.application.IdeaManagement.entity.AcademicYear;
import web.application.IdeaManagement.entity.Category;
import web.application.IdeaManagement.entity.Department;
import web.application.IdeaManagement.entity.Topic;
import web.application.IdeaManagement.model.response.CategoryReponse;
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
public class CategorySpecification {
    @PersistenceContext
    EntityManager entityManager;


//    public Specification<Category> filterCategory(String searchKey) {
//        //cb.isFalse(root.get("isDeleted").as(Boolean.class))
//        return (root, query, cb) -> {
//            List<Predicate> predicates = new ArrayList<>();
//            if (!StringUtils.isEmpty(searchKey)) {
//                try {
//                    Long parseId = Long.parseLong(searchKey);
//                    predicates.add(
//                            cb.and(
//                                    cb.or(
//                                            cb.like(root.get("createdUser"), "%" + searchKey + "%"),
//                                            cb.like(root.get("category"), "%" + searchKey + "%"),
//                                            cb.equal(root.get("id"), parseId)
//                                    ),
//                                    cb.isFalse(root.get("isDeleted").as(Boolean.class))
//                            )
//                    );
//                } catch (Exception e) {
//                    predicates.add(
//                            cb.and(
//                                    cb.or(
//                                            cb.like(root.get("createdUser"), "%" + searchKey + "%"),
//                                            cb.like(root.get("category"), "%" + searchKey + "%")
//                                    ),
//                                    cb.isFalse(root.get("isDeleted").as(Boolean.class))
//                            )
//                    );
//                }
//            }
//            return cb.and(predicates.stream().toArray(Predicate[]::new));
//        };
//    }

    public Map<String, Object> getDataCategory(String searchKey, Integer page, Integer limit, String sortBy, String sortType) {
        try {
            Map<String, Object> mapFinal = new HashMap<>();
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<CategoryReponse> query = cb.createQuery(CategoryReponse.class);
            Root<Category> root = query.from(Category.class);
            Root<Topic> rootTopic = query.from(Topic.class);
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.equal(root.get("topicId"), rootTopic.get("id")));
            if (!StringUtils.isEmpty(searchKey)) {
                try {
                    Long parseId = Long.parseLong(searchKey);
                    predicates.add(
                            cb.and(
                                    cb.or(
                                            cb.like(root.get("createdUser"), "%" + searchKey + "%"),
                                            cb.like(root.get("category"), "%" + searchKey + "%"),
                                            cb.equal(root.get("id"), parseId)
                                    ),
                                    cb.isFalse(root.get("isDeleted").as(Boolean.class))
                            )
                    );
                } catch (Exception e) {
                    predicates.add(
                            cb.and(
                                    cb.or(
                                            cb.like(root.get("createdUser"), "%" + searchKey + "%"),
                                            cb.like(root.get("category"), "%" + searchKey + "%")
                                    ),
                                    cb.isFalse(root.get("isDeleted").as(Boolean.class))
                            )
                    );
                }
            }

            //------------------CREATE COUNT QUERY-----------------------//
            CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
            Root<Category> rootCount = countQuery.from(Category.class);

            Root<Topic> rootTopicCount = query.from(Topic.class);
            List<Predicate> predicatesCount = new ArrayList<>();
            predicatesCount.add(cb.equal(rootCount.get("topicId"), rootTopicCount.get("id")));
            if (!StringUtils.isEmpty(searchKey)) {
                try {
                    Long parseId = Long.parseLong(searchKey);
                    predicatesCount.add(
                            cb.and(
                                    cb.or(
                                            cb.like(rootCount.get("createdUser"), "%" + searchKey + "%"),
                                            cb.like(rootCount.get("category"), "%" + searchKey + "%"),
                                            cb.equal(rootCount.get("id"), parseId)
                                    ),
                                    cb.isFalse(rootCount.get("isDeleted").as(Boolean.class))
                            )
                    );
                } catch (Exception e) {
                    predicatesCount.add(
                            cb.and(
                                    cb.or(
                                            cb.like(rootCount.get("createdUser"), "%" + searchKey + "%"),
                                            cb.like(rootCount.get("category"), "%" + searchKey + "%")
                                    ),
                                    cb.isFalse(rootCount.get("isDeleted").as(Boolean.class))
                            )
                    );
                }
            }




            //------------------CREATE SORT-----------------------------//
            if (sortType.equalsIgnoreCase("asc")) {
                switch (sortBy) {
                    case "id":
                        query.orderBy(cb.asc(root.get("id")));
                        break;
                    case "category":
                        query.orderBy(cb.asc(root.get("category")));
                        break;
                }
            } else {
                switch (sortBy) {
                    case "id":
                        query.orderBy(cb.desc(root.get("id")));
                        break;
                    case "category":
                        query.orderBy(cb.desc(root.get("category")));
                        break;
                }
            }

            //-------------------------QUERY DATA------------------------//
            query.multiselect(
                    root.get("id"),
                    root.get("category"),
                    rootTopic.get("topic")
            ).where(cb.and(predicates.stream().toArray(Predicate[]::new)));
            List<CategoryReponse> listResult = entityManager.createQuery(query) != null ? entityManager.createQuery(query).
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
