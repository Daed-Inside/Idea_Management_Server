package web.application.IdeaManagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import web.application.IdeaManagement.dto.PageDto;
import web.application.IdeaManagement.manager.IdeaManager;
import web.application.IdeaManagement.model.request.IdeaRequestModel;
import web.application.IdeaManagement.model.response.IdeaResponseModel;
import web.application.IdeaManagement.utils.JwtUtils;
import web.application.IdeaManagement.utils.ResponseUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/idea")
public class IdeaController {
    @Autowired
    IdeaManager ideaManager;
    @Autowired
    ResponseUtils responseUtils;
    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/create")
    public ResponseEntity<?> createIdea(@RequestParam("departmentId") Long departmentId,
                                        @RequestParam("categoryId") Long categoryId,
                                        @RequestParam("topicId") Long topicId,
                                        @RequestParam("title") String ideaTitle,
                                        @RequestParam("description") String ideaContent,
                                        @RequestParam("contributor") Boolean isAnonymous,
                                        @RequestParam(value = "files", required = false) MultipartFile[] files,
                                        HttpServletRequest request) {
        try {
            String jwt = jwtUtils.getJwtFromRequest(request);
            String username = jwtUtils.getUserNameFromJwtToken(jwt);
            String userId = jwtUtils.getUserIdFromJwtToken(jwt);
            Integer result = ideaManager.createIdea(categoryId, topicId, ideaTitle, ideaContent, isAnonymous, files, username, userId);
            if (result == 1) {
                return responseUtils.getResponseEntity(null, 1, "Create Successfully", HttpStatus.OK);
            } else if (result == -2) {
                return responseUtils.getResponseEntity(null, -2, "The topic is closed", HttpStatus.OK);
            }
            return responseUtils.getResponseEntity(null, -1, "Failed", HttpStatus.OK);
        } catch (Exception e) {
            return responseUtils.getResponseEntity(e, -1, "Login fail!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateIdea(@RequestBody IdeaRequestModel req) {
        try {
            Boolean result = ideaManager.updateIdea(req);
            if (result) {
                return responseUtils.getResponseEntity(null, 1, "Create Successfully", HttpStatus.OK);
            }
            return responseUtils.getResponseEntity(null, -1, "Failed", HttpStatus.OK);
        } catch (Exception e) {
            return responseUtils.getResponseEntity(e, -1, "Login fail!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/get")
    public ResponseEntity<?> getIdeaWithModel(@RequestParam String searchKey) {
        try {
            List<IdeaResponseModel> result = ideaManager.getIdeaWithModel(searchKey);
            if (result != null) {
                return responseUtils.getResponseEntity(result, 1, "Create Successfully", HttpStatus.OK);
            }
            return responseUtils.getResponseEntity(null, -1, "Failed", HttpStatus.OK);
        } catch (Exception e) {
            return responseUtils.getResponseEntity(e, -1, "Login fail!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/get/specification")
    public ResponseEntity<?> getIdeaWithSpec(@RequestParam(value = "searchKey", required = false) String searchKey,
                                             @RequestParam("page") Integer page,
                                             @RequestParam("limit") Integer limit,
                                             @RequestParam("sortBy") String sortBy,
                                             @RequestParam("sortType") String sortType) {
        try {
            PageDto result = ideaManager.getIdeaWithSpec(searchKey, page, limit, sortBy, sortType);
            if (result != null) {
                return responseUtils.getResponseEntity(result, 1, "Create Successfully", HttpStatus.OK);
            }
            return responseUtils.getResponseEntity(null, -1, "Failed", HttpStatus.OK);
        } catch (Exception e) {
            return responseUtils.getResponseEntity(e, -1, "Login fail!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
