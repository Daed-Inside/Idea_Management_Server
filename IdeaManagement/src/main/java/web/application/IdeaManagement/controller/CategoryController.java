package web.application.IdeaManagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import web.application.IdeaManagement.entity.Category;
import web.application.IdeaManagement.manager.CategoryManager;
import web.application.IdeaManagement.utils.JwtUtils;
import web.application.IdeaManagement.utils.ResponseUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    CategoryManager categoryManager;
    @Autowired
    ResponseUtils responseUtils;
    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/create")
    public ResponseEntity<?> createCategory(@RequestParam("category") String category,
                                        @RequestParam("topicId") Long topicId,
                                        HttpServletRequest request) {
        try{
            Integer result = categoryManager.createCategory(category, topicId);
            if(result == 1){
                return responseUtils.getResponseEntity(null, 1, "Create category Successfully", HttpStatus.OK);
            }else if(result == 2){
                return responseUtils.getResponseEntity(null, -1, "The category already exists", HttpStatus.OK);
            }
            return responseUtils.getResponseEntity(null, -1, "Failed", HttpStatus.OK);
        }catch (Exception e) {
            return responseUtils.getResponseEntity(e, -1, "Login fail!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteCategory(@RequestParam("categoryId") Long categoryId){
        try{
            Integer result = categoryManager.deleteCategory(categoryId);
            if(result == 1){
                return responseUtils.getResponseEntity(null, 1, "Delete category Successfully", HttpStatus.OK);
            }else if (result == -2){
                return responseUtils.getResponseEntity(null, -1, "Category is being used", HttpStatus.OK);
            }
            return responseUtils.getResponseEntity(null, -1, "Failed", HttpStatus.OK);
        }catch (Exception e) {
            return responseUtils.getResponseEntity(e, -1, "Login fail!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getAll")
    public ResponseEntity<?> getAllCategory(){
        try{
            List<Category> result = categoryManager.getAllCategory();
            if(result != null){
                return responseUtils.getResponseEntity(result, 1, "Get category list successfully", HttpStatus.OK);
            }
            return responseUtils.getResponseEntity(null, -1, "Failed", HttpStatus.OK);
        }catch (Exception e) {
            return responseUtils.getResponseEntity(e, -1, "Login fail!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/get")
    public ResponseEntity<?> getCategoryWithSpec(@RequestParam String searchKey){
        try{
            List<Category> result = categoryManager.getCategoryWithSpec(searchKey);
            if(result != null){
                return responseUtils.getResponseEntity(result, 1, "Get category list successfully", HttpStatus.OK);
            }
            return responseUtils.getResponseEntity(null, -1, "Failed", HttpStatus.OK);
        }catch (Exception e) {
            return responseUtils.getResponseEntity(e, -1, "Login fail!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
