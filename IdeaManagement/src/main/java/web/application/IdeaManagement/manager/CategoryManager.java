package web.application.IdeaManagement.manager;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import web.application.IdeaManagement.dto.PageDto;
import web.application.IdeaManagement.entity.Category;
import web.application.IdeaManagement.entity.Department;
import web.application.IdeaManagement.model.request.CategoryRequest;
import web.application.IdeaManagement.model.request.DepartmentRequest;
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

    public Integer createCategory(CategoryRequest reqBody) {
        try{
            Category newCategory = modelMapper.map(reqBody, Category.class);
            newCategory.setCreatedDate(new Date());
            categoryRepository.save(newCategory);
            return 1;
        }catch (Exception e){
            e.printStackTrace();
            return -1;
        }
    }

    public Integer deleteCategory(Long categoryId, String username) {
        try{
            if(ideaRepository.existsByCategoryId(categoryId)){
                return -1;
            }
            Category deleleCategory = categoryRepository.findById(categoryId).get();
            if(deleleCategory.getIsDeleted()){
                return -2;
            }
            deleleCategory.setIsDeleted(true);
            deleleCategory.setModifiedDate(new Date());
            deleleCategory.setModifiedUser(username);
            categoryRepository.save(deleleCategory);
            return 1;
        }catch (Exception e) {
            return -1;
        }
    }


    public PageDto getCategory(String searchKey, Integer page, Integer limit, String sortBy, String sortType) {
        try {
            Sort sort = responseUtils.getSort(sortBy, sortType);
            Integer pageNum = page - 1;
            Page<Category> pageCategory = categoryRepository.findAll(categorySpecification.filterCategory(searchKey), PageRequest.of(pageNum, limit, sort));
            return PageDto.builder()
                    .content(pageCategory.getContent())
                    .numberOfElements(pageCategory.getNumberOfElements())
                    .page(page)
                    .size(pageCategory.getSize())
                    .totalPages(pageCategory.getTotalPages())
                    .totalElements(pageCategory.getTotalElements())
                    .build();
        } catch (Exception e) {
            return null;
        }
    }
}
