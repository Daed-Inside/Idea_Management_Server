package web.application.IdeaManagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import web.application.IdeaManagement.entity.AcademicYear;
import web.application.IdeaManagement.manager.AcademicYearManager;
import web.application.IdeaManagement.utils.JwtUtils;
import web.application.IdeaManagement.utils.ResponseUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/academicyear")
public class AcademicYearController {
    @Autowired
    AcademicYearManager academicYearManager;
    @Autowired
    ResponseUtils responseUtils;
    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/create")
    public ResponseEntity<?> createAcademicYear (@RequestParam("semester") String semester,
                                                 @RequestParam("year") String year,
                                                 @RequestParam("startDate") Date startDate,
                                                 @RequestParam("endDate") Date endDate,
                                                 HttpServletRequest request){
        try{
            String jwt = jwtUtils.getJwtFromRequest(request);
            Integer result = academicYearManager.createAcademicYear(semester, year, startDate, endDate);
            if (result == 1) {
                return responseUtils.getResponseEntity(null, 1, "Create Successfully", HttpStatus.OK);
            } else if (result == -1) {
                return responseUtils.getResponseEntity(null, -1, "Fail to create academic year", HttpStatus.OK);
            }
            return responseUtils.getResponseEntity(null, -1, "Failed", HttpStatus.OK);
        }catch (Exception e) {
            return responseUtils.getResponseEntity(e, -1, "Login fail!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/get")
    public ResponseEntity<?> getAcademicYearWithSpec(@RequestParam String searchKey) {
        try{
            List<AcademicYear> result = academicYearManager.getAcademicYearWithSpec(searchKey);
            if(result != null){
                return responseUtils.getResponseEntity(result, 1, "Get academic year list successfully", HttpStatus.OK);
            }
            return responseUtils.getResponseEntity(null, -1, "Failed", HttpStatus.OK);
        }catch (Exception e) {
            return responseUtils.getResponseEntity(e, -1, "Login fail!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
