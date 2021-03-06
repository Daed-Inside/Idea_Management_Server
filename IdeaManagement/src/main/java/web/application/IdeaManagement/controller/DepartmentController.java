package web.application.IdeaManagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import web.application.IdeaManagement.dto.PageDto;
import web.application.IdeaManagement.manager.AcademicYearManager;
import web.application.IdeaManagement.manager.DepartmentManager;
import web.application.IdeaManagement.model.request.AcademicYearRequest;
import web.application.IdeaManagement.model.request.DepartmentRequest;
import web.application.IdeaManagement.utils.JwtUtils;
import web.application.IdeaManagement.utils.ResponseUtils;

import javax.servlet.http.HttpServletRequest;

@CrossOrigin("*")
@RestController
@RequestMapping("/department")
public class DepartmentController {
    @Autowired
    DepartmentManager departmentManager;
    @Autowired
    ResponseUtils responseUtils;
    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/create")
    public ResponseEntity<?> createDepartment (@RequestBody DepartmentRequest reqBody,
                                                 HttpServletRequest request){
        try{
            String jwt = jwtUtils.getJwtFromRequest(request);
            String username = jwtUtils.getUserNameFromJwtToken(jwt);
            reqBody.setCreatedUser(username);
            Integer result = departmentManager.createDepartment(reqBody);
            if (result == 1) {
                return responseUtils.getResponseEntity(null, 1, "Create Successfully", HttpStatus.OK);
            } else if (result == -1) {
                return responseUtils.getResponseEntity(null, -1, "Fail to create department", HttpStatus.OK);
            } else if (result == -2) {
                return responseUtils.getResponseEntity(null, -2, "Department is exist", HttpStatus.OK);
            }
            return responseUtils.getResponseEntity(null, -1, "Failed", HttpStatus.OK);
        }catch (Exception e) {
            return responseUtils.getResponseEntity(e, -1, "Login fail!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/get")
    public ResponseEntity<?> getDepartmentWithSpec(@RequestParam(value = "searchKey",required = false) String searchKey,
                                                     @RequestParam("page") Integer page,
                                                     @RequestParam("limit") Integer limit,
                                                     @RequestParam("sortBy") String sortBy,
                                                     @RequestParam("sortType") String sortType) {
        try {
            PageDto result = departmentManager.getDepartment(searchKey, page, limit, sortBy, sortType);
            if (result != null) {
                return responseUtils.getResponseEntity(result, 1, "Get Successfully", HttpStatus.OK);
            }
            return responseUtils.getResponseEntity(null, -1, "Failed", HttpStatus.OK);
        } catch (Exception e) {
            return responseUtils.getResponseEntity(e, -1, "Login fail!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
