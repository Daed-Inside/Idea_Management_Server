package web.application.IdeaManagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import web.application.IdeaManagement.dto.PageDto;
import web.application.IdeaManagement.manager.PermissionManager;
import web.application.IdeaManagement.manager.RoleManager;
import web.application.IdeaManagement.model.request.CategoryRequest;
import web.application.IdeaManagement.model.request.RoleRequest;
import web.application.IdeaManagement.model.response.PermissionResponse;
import web.application.IdeaManagement.repository.PermissionRepository;
import web.application.IdeaManagement.utils.JwtUtils;
import web.application.IdeaManagement.utils.ResponseUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/role")
public class RoleController {
    @Autowired
    ResponseUtils responseUtils;
    @Autowired
    JwtUtils jwtUtils;
    @Autowired
    RoleManager roleManager;
    @Autowired
    PermissionManager permissionManager;

    @PostMapping("/create")
    public ResponseEntity<?> createCategory(@RequestBody RoleRequest reqBody,
                                            HttpServletRequest request) {
        try {
            String jwt = jwtUtils.getJwtFromRequest(request);
            String username = jwtUtils.getUserNameFromJwtToken(jwt);
            Integer result = roleManager.create(reqBody, username);
            if (result == 1) {
                return responseUtils.getResponseEntity(null, 1, "Create Successfully", HttpStatus.OK);
            } else if (result == -2) {
                return responseUtils.getResponseEntity(null, -1, "Role name is existed, please choose another name", HttpStatus.OK);
            }
            return responseUtils.getResponseEntity(null, -1, "Failed", HttpStatus.OK);
        } catch (Exception e) {
            return responseUtils.getResponseEntity(e, -1, "Login fail!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/get")
    public ResponseEntity<?> getRole(@RequestParam(value = "searchKey", required = false) String searchKey,
                                     @RequestParam("page") Integer page,
                                     @RequestParam("limit") Integer limit,
                                     @RequestParam("sortBy") String sortBy,
                                     @RequestParam("sortType") String sortType) {
        try {
            PageDto result = roleManager.getRole(searchKey, page, limit, sortBy, sortType);
            if (result != null) {
                return responseUtils.getResponseEntity(result, 1, "Get Successfully", HttpStatus.OK);
            }
            return responseUtils.getResponseEntity(null, -1, "Failed", HttpStatus.OK);
        } catch (Exception e) {
            return responseUtils.getResponseEntity(e, -1, "Login fail!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/permission/get")
    public ResponseEntity<?> getPermission() {
        try {
            List<PermissionResponse> result = permissionManager.getPermission();
            if (result != null) {
                return responseUtils.getResponseEntity(result, 1, "Get Successfully", HttpStatus.OK);
            }
            return responseUtils.getResponseEntity(null, -1, "Failed", HttpStatus.OK);
        } catch (Exception e) {
            return responseUtils.getResponseEntity(e, -1, "Login fail!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/permission/check")
    public ResponseEntity<?> getPermission(HttpServletRequest request, @RequestParam("screen") String screen) {
        try {
            String jwt = jwtUtils.getJwtFromRequest(request);
            String userId = jwtUtils.getUserIdFromJwtToken(jwt);
            Boolean result = permissionManager.checkPermission(userId, screen);
            return responseUtils.getResponseEntity(result, 1, "Get Successfully", HttpStatus.OK);
        } catch (Exception e) {
            return responseUtils.getResponseEntity(e, -1, "Login fail!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
