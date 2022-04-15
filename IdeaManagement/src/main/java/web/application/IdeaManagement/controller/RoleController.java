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

    @PostMapping("/update/{id}")
    public ResponseEntity<?> edit(@RequestBody RoleRequest reqBody,
                                            @PathVariable Long id,
                                            HttpServletRequest request) {
        try {
            reqBody.setId(id);
            String jwt = jwtUtils.getJwtFromRequest(request);
            String username = jwtUtils.getUserNameFromJwtToken(jwt);
            Integer result = roleManager.edit(reqBody, username);
            if (result == 1) {
                return responseUtils.getResponseEntity(null, 1, "Edit successfully", HttpStatus.OK);
            } else if (result == -2) {
                return responseUtils.getResponseEntity(null, -1, "Role name is existed, please choose another name", HttpStatus.OK);
            }
            return responseUtils.getResponseEntity(null, -1, "Failed", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return responseUtils.getResponseEntity(e, -1, "fail!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/get")
    public ResponseEntity<?> getRole(@RequestParam(value = "searchKey", required = false) String searchKey,
                                     @RequestParam("page") Integer page,
                                     @RequestParam("limit") Integer limit,
                                     @RequestParam("sortBy") String sortBy,
                                     @RequestParam("sortType") String sortType,
                                     @RequestParam(value = "selectBox", required = false) Boolean selectBox) {
        try {
            PageDto result = roleManager.getRole(searchKey, page, selectBox, limit, sortBy, sortType);
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

    @GetMapping("/permission/check/{flag}")
    public ResponseEntity<?> getPermission(HttpServletRequest request, @PathVariable String flag) {
        try {
            String jwt = jwtUtils.getJwtFromRequest(request);
            String userId = jwtUtils.getUserIdFromJwtToken(jwt);
            Boolean result = permissionManager.checkPermission(userId, flag);
            return responseUtils.getResponseEntity(result, 1, "Get Successfully", HttpStatus.OK);
        } catch (Exception e) {
            return responseUtils.getResponseEntity(e, -1, "Login fail!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
