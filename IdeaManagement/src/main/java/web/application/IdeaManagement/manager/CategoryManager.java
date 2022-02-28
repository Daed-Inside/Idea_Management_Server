package web.application.IdeaManagement.manager;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import web.application.IdeaManagement.entity.Category;
import web.application.IdeaManagement.repository.CategoryRepository;
import web.application.IdeaManagement.repository.IdeaRepository;
import web.application.IdeaManagement.specification.CategorySpecification;
import web.application.IdeaManagement.utils.ResponseUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class CategoryManager {
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    IdeaRepository ideaRepository;
    @Autowired
    ResponseUtils responseUtils;
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    CategorySpecification categorySpecification;
    @PersistenceContext
    EntityManager entityManager;

    public Integer createCategory(String category, Long topicId) {
        try{
            if(categoryRepository.existsByCategory(category)){
                return -2;
            }
            Category newCategory = new Category();
            newCategory.setCategory(category);
            newCategory.setTopicId(topicId);
            newCategory.setCreatedDate(new Date());
            categoryRepository.save(newCategory);
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public Integer deleteCategory(Long categoryId) {
        try{
            if(ideaRepository.existsByCategoryId(categoryId)){
                return -2;
            }
            Category deleleCategory = categoryRepository.findById(categoryId).get();
            deleleCategory.setIsDeleted(true);
            deleleCategory.setModifiedDate(new Date());
            categoryRepository.save(deleleCategory);
            return 1;
        }catch (Exception e) {
            return -1;
        }
    }

    public List<Category> getCategoryWithSpec(String searchKey) {
        try{
            List<Category> listResult = categoryRepository.findAll(categorySpecification.filterCategory(searchKey));
            return listResult;
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Category> getAllCategory() {
        try{
            List<Category> listResult = categoryRepository.findAll();
            return listResult;
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
