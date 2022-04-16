package web.application.IdeaManagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import web.application.IdeaManagement.dto.PageDto;
import web.application.IdeaManagement.manager.CategoryManager;
import web.application.IdeaManagement.model.request.CategoryRequest;
import web.application.IdeaManagement.utils.JwtUtils;
import web.application.IdeaManagement.utils.ResponseUtils;

import javax.servlet.http.HttpServletRequest;

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
    public ResponseEntity<?> createCategory(@RequestBody CategoryRequest reqBody,
                                            HttpServletRequest request) {
        try {
            String jwt = jwtUtils.getJwtFromRequest(request);
            String username = jwtUtils.getUserNameFromJwtToken(jwt);
            reqBody.setCreatedUser(username);
            Integer result = categoryManager.createCategory(reqBody);
            if (result == 1) {
                return responseUtils.getResponseEntity(null, 1, "Create Successfully", HttpStatus.OK);
            } else if (result == -1) {
                return responseUtils.getResponseEntity(null, -1, "Fail to create department", HttpStatus.OK);
            }
            return responseUtils.getResponseEntity(null, -1, "Failed", HttpStatus.OK);
        } catch (Exception e) {
            return responseUtils.getResponseEntity(e, -1, "Login fail!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable Long id, HttpServletRequest request) {
        try {
            String jwt = jwtUtils.getJwtFromRequest(request);
            String username = jwtUtils.getUserNameFromJwtToken(jwt);
            Integer result = categoryManager.deleteCategory(id, username);
            if (result == 1) {
                return responseUtils.getResponseEntity(null, 1, "Delete category Successfully", HttpStatus.OK);
            } else if (result == -1) {
                return responseUtils.getResponseEntity(null, -1, "Category is being used", HttpStatus.OK);
            } else if (result == -2) {
                return responseUtils.getResponseEntity(null, -2, "Category does not exist", HttpStatus.OK);
            }
            return responseUtils.getResponseEntity(null, -1, "Failed", HttpStatus.OK);
        } catch (Exception e) {
            return responseUtils.getResponseEntity(e, -1, "Login fail!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/get")
    public ResponseEntity<?> getCategoryWithSpec(@RequestParam(value = "searchKey", required = false) String searchKey,
                                                 @RequestParam(value = "topicId", required = false) Long topicId,
                                                 @RequestParam("page") Integer page,
                                                 @RequestParam("limit") Integer limit,
                                                 @RequestParam("sortBy") String sortBy,
                                                 @RequestParam("sortType") String sortType) {
        try {
            PageDto result = categoryManager.getCategory(searchKey, topicId, page, limit, sortBy, sortType);
            if (result != null) {
                return responseUtils.getResponseEntity(result, 1, "Get Successfully", HttpStatus.OK);
            }
            return responseUtils.getResponseEntity(null, -1, "Failed", HttpStatus.OK);
        } catch (Exception e) {
            return responseUtils.getResponseEntity(e, -1, "Login fail!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
