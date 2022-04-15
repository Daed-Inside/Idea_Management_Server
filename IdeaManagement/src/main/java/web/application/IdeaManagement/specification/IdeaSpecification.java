package web.application.IdeaManagement.specification;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.apache.commons.lang3.StringUtils;
import web.application.IdeaManagement.entity.*;
import web.application.IdeaManagement.model.response.IdeaResponseModel;

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
public class IdeaSpecification {
    @PersistenceContext
    EntityManager entityManager;

    public Map<String, Object> getIdeaWithModel(String searchKey, Long acadId, Long deptId, Long topicId, Long categoryId, Integer sort, Integer page, Integer limit) {
        try {
            Map<String, Object> mapFinal = new HashMap<>();
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            List<Predicate> predicates = new ArrayList<>();
            CriteriaQuery<IdeaResponseModel> query = cb.createQuery(IdeaResponseModel.class);
            Root<Idea> root = query.from(Idea.class);
            Root<Topic> rootTopic = query.from(Topic.class);
            Root<AcademicYear> rootAcad = query.from(AcademicYear.class);
            Root<Department> rootDept = query.from(Department.class);
            Root<Category> rootCate = query.from(Category.class);
            predicates.add(cb.equal(root.get("topicId"), rootTopic.get("id")));
            predicates.add(cb.equal(root.get("categoryId"), rootCate.get("id")));
            predicates.add(cb.equal(rootAcad.get("id"), rootTopic.get("academicId")));
            predicates.add(cb.equal(rootDept.get("id"), rootTopic.get("departmentId")));

            //-------------------------Query count----------------------//
            CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
            List<Predicate> predCount = new ArrayList<>();
            Root<Idea> rootCount = countQuery.from(Idea.class);
            Root<Topic> rootTopicCount = countQuery.from(Topic.class);
            Root<AcademicYear> rootAcadCount = countQuery.from(AcademicYear.class);
            Root<Department> rootDeptCount = countQuery.from(Department.class);
            Root<Category> rootCateCount = countQuery.from(Category.class);
            predCount.add(cb.equal(rootCount.get("topicId"), rootTopicCount.get("id")));
            predCount.add(cb.equal(rootCount.get("categoryId"), rootCateCount.get("id")));
            predCount.add(cb.equal(rootAcadCount.get("id"), rootTopicCount.get("academicId")));
            predCount.add(cb.equal(rootDeptCount.get("id"), rootTopicCount.get("departmentId")));


            if (acadId != null) {
                predicates.add(cb.equal(rootAcad.get("id"), acadId));
                predCount.add(cb.equal(rootAcadCount.get("id"), acadId));
            }

            if (topicId != null) {
                predicates.add(cb.equal(rootTopic.get("id"), topicId));
                predCount.add(cb.equal(rootTopicCount.get("id"), topicId));
            }

            if (deptId != null) {
                predicates.add(cb.equal(rootDept.get("id"), deptId));
                predCount.add(cb.equal(rootDeptCount.get("id"), deptId));
            }

            if (categoryId != null) {
                predicates.add(cb.equal(rootCate.get("id"), categoryId));
                predCount.add(cb.equal(rootCateCount.get("id"), categoryId));
            }

            if (!StringUtils.isEmpty(searchKey)) {
                predicates.add(cb.like(root.get("ideaContent"), "%" + searchKey + "%"));
                predCount.add(cb.like(rootCount.get("ideaContent"), "%" + searchKey + "%"));
            }
            if (sort != null) {
                switch (sort) {
                    case 1:
                        query.orderBy(cb.desc(root.get("createdDate")));
                        break;
                    case 2:
                        Root<IdeaReaction> rootLike = query.from(IdeaReaction.class);
                        predicates.add(cb.equal(root.get("id"), rootLike.get("ideaId")));
                        predicates.add(cb.equal(rootLike.get("evaluation"), 1));
                        query.orderBy(cb.desc(cb.count(rootLike.get("id"))));
                        query.groupBy(root.get("id"));
                        break;
                    case 3:
                        Root<IdeaReaction> rootDislike = query.from(IdeaReaction.class);
                        predicates.add(cb.equal(root.get("id"), rootDislike.get("ideaId")));
                        predicates.add(cb.equal(rootDislike.get("evaluation"), -1));
                        query.orderBy(cb.desc(cb.count(rootDislike.get("id"))));
                        query.groupBy(root.get("id"));
                        break;
                    case 4:
                        Root<IdeaViewCount> rootIdeaCount = query.from(IdeaViewCount.class);
                        predicates.add(cb.equal(root.get("id"), rootIdeaCount.get("ideaId")));
                        query.orderBy(cb.desc(cb.count(rootIdeaCount.get("id"))));
                        query.groupBy(root.get("id"));
                        break;
                    case 5:
                        Root<IdeaComment> rootComment = query.from(IdeaComment.class);
                        predicates.add(cb.equal(root.get("id"), rootComment.get("ideaId")));
                        query.orderBy(cb.desc(rootComment.get("createdDate")));
                        break;
                }
            }
            query.multiselect(
                    root.get("id"),
                    root.get("ideaTitle"),
                    root.get("ideaContent"),
                    rootTopic.get("topic"),
                    rootCate.get("category"),
                    root.get("createdDate"),
                    root.get("createdUser")
            ).where(cb.and(predicates.stream().toArray(Predicate[]::new)));
            List<IdeaResponseModel> listResult = entityManager.createQuery(query) != null ? entityManager.createQuery(query).getResultList() : new ArrayList<>();
            countQuery.select(cb.count(rootCount)).where(cb.and(predCount.stream().toArray(Predicate[]::new)));
            Long count = entityManager.createQuery(countQuery).getSingleResult();
            mapFinal.put("data", listResult);
            mapFinal.put("count", count);
            entityManager.close();
            return mapFinal;
        } catch (Exception e) {
            return new HashMap<>();
        }
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
