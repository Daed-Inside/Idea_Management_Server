package web.application.IdeaManagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import web.application.IdeaManagement.dto.PageDto;
import web.application.IdeaManagement.manager.IdeaManager;
import web.application.IdeaManagement.manager.TopicManager;
import web.application.IdeaManagement.model.request.TopicRequest;
import web.application.IdeaManagement.utils.JwtUtils;
import web.application.IdeaManagement.utils.ResponseUtils;

import javax.servlet.http.HttpServletRequest;

@CrossOrigin("*")
@RestController
@RequestMapping("/topic")
public class TopicController {
    @Autowired
    TopicManager topicManager;
    @Autowired
    ResponseUtils responseUtils;
    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/create")
    public ResponseEntity<?> createIdea(@RequestBody TopicRequest reqBody,
                                        HttpServletRequest request) {
        try {
            String jwt = jwtUtils.getJwtFromRequest(request);
            String username = jwtUtils.getUserNameFromJwtToken(jwt);
            reqBody.setCreatedUser(username);
            Integer result = topicManager.createTopic(reqBody);
            if (result == 1) {
                return responseUtils.getResponseEntity(null, 1, "Create Successfully", HttpStatus.OK);
            } else if (result == -1) {
                return responseUtils.getResponseEntity(null, -1, "The topic is closed", HttpStatus.OK);
            }
            return responseUtils.getResponseEntity(null, -1, "Failed", HttpStatus.OK);
        } catch (Exception e) {
            return responseUtils.getResponseEntity(e, -1, "Login fail!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/get")
    public ResponseEntity<?> getIdeaWithSpec(@RequestParam(value = "searchKey",required = false) String searchKey,
                                             @RequestParam(value = "departmentId",required = false) Long departmentId,
                                             @RequestParam("page") Integer page,
                                             @RequestParam("limit") Integer limit,
                                             @RequestParam("sortBy") String sortBy,
                                             @RequestParam("sortType") String sortType) {
        try {
            PageDto result = topicManager.getTopic(searchKey, departmentId, page, limit, sortBy, sortType);
            if (result != null) {
                return responseUtils.getResponseEntity(result, 1, "Create Successfully", HttpStatus.OK);
            }
            return responseUtils.getResponseEntity(null, -1, "Failed", HttpStatus.OK);
        } catch (Exception e) {
            return responseUtils.getResponseEntity(e, -1, "Login fail!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
