package web.application.IdeaManagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import web.application.IdeaManagement.dto.PageDto;
import web.application.IdeaManagement.manager.UserManager;
import web.application.IdeaManagement.model.request.ChangePassRequest;
import web.application.IdeaManagement.model.request.IdeaRequestModel;
import web.application.IdeaManagement.model.request.SignupRequest;
import web.application.IdeaManagement.utils.JwtUtils;
import web.application.IdeaManagement.utils.ResponseUtils;

import javax.servlet.http.HttpServletRequest;

import static web.application.IdeaManagement.constant.constant.*;

@CrossOrigin("*")
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    ResponseUtils responseUtils;
    @Autowired
    JwtUtils jwtUtils;
    @Autowired
    UserManager userManager;

    @GetMapping("/get")
    public ResponseEntity<?> getUserWithSpec(@RequestParam(value = "searchKey", required = false) String searchKey,
                                             @RequestParam("page") Integer page,
                                             @RequestParam("limit") Integer limit,
                                             @RequestParam("sortBy") String sortBy,
                                             @RequestParam("sortType") String sortType) {
        try {
            PageDto result = userManager.getUser(searchKey, page, limit, sortBy, sortType);
            if (result != null) {
                return responseUtils.getResponseEntity(result, 1, "Create Successfully", HttpStatus.OK);
            }
            return responseUtils.getResponseEntity(null, -1, "Failed", HttpStatus.OK);
        } catch (Exception e) {
            return responseUtils.getResponseEntity(e, SYSTEM_ERROR_CODE, SYSTEM_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/update/{userId}")
    public ResponseEntity<?> updateIdea(@PathVariable String userId, @RequestBody SignupRequest req) {
        try {
            Integer result = userManager.updateUser(userId, req);
            if (result == 1) {
                return responseUtils.getResponseEntity(null, 1, "Create Successfully", HttpStatus.OK);
            }
            return responseUtils.getResponseEntity(null, -1, "Failed", HttpStatus.OK);
        } catch (Exception e) {
            return responseUtils.getResponseEntity(e, SYSTEM_ERROR_CODE, SYSTEM_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/change-pass")
    public ResponseEntity<?> changePass(@RequestBody ChangePassRequest req, HttpServletRequest request) {
        try {
            String jwt = jwtUtils.getJwtFromRequest(request);
            String username = jwtUtils.getUserNameFromJwtToken(jwt);
            req.setEmail(username);
            Integer result = userManager.changePassword(req);
            if (result == 1) {
                return responseUtils.getResponseEntity(null, 1, "Create Successfully", HttpStatus.OK);
            } else if (result == -2) {
                return responseUtils.getResponseEntity(null, -2, "Incorrect current password", HttpStatus.OK);
            }
            return responseUtils.getResponseEntity(null, -1, "Failed", HttpStatus.OK);
        } catch (Exception e) {
            return responseUtils.getResponseEntity(e, SYSTEM_ERROR_CODE, SYSTEM_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
