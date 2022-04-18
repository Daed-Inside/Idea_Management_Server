package web.application.IdeaManagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import web.application.IdeaManagement.dto.PageDto;
import web.application.IdeaManagement.entity.Idea;
import web.application.IdeaManagement.entity.IdeaComment;
import web.application.IdeaManagement.entity.User;
import web.application.IdeaManagement.manager.*;
import web.application.IdeaManagement.model.request.*;
import web.application.IdeaManagement.model.response.*;
import web.application.IdeaManagement.utils.JwtUtils;
import web.application.IdeaManagement.utils.ResponseUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static web.application.IdeaManagement.constant.constant.CLIENT_CONTEXTPATH;

@CrossOrigin("*")
@RestController
@RequestMapping("/idea")
public class IdeaController {
    @Autowired
    IdeaManager ideaManager;
    @Autowired
    UserManager userManager;
    @Autowired
    IdeaReactionManager ideaReactionManager;
    @Autowired
    IdeaCommentManager ideaCommentManager;
    @Autowired
    IdeaViewCountManager ideaViewCountManager;
    @Autowired
    ResponseUtils responseUtils;
    @Autowired
    JwtUtils jwtUtils;
    @Autowired
    MailManager mailManager;

    @PostMapping("/create")
    public ResponseEntity<?> createIdea(@RequestBody IdeaRequest ideaRequest,
//                                        @RequestParam(value = "files", required = false) MultipartFile[] files,
                                        HttpServletRequest request) {
        try {
            String jwt = jwtUtils.getJwtFromRequest(request);
            String username = jwtUtils.getUserNameFromJwtToken(jwt);
            String userId = jwtUtils.getUserIdFromJwtToken(jwt);
            Long result = ideaManager.createIdea(ideaRequest, username, userId);
            if (Objects.equals(result, -2l)) {
                return responseUtils.getResponseEntity(null, -2, "The topic is closed", HttpStatus.OK);
            } else {
                List<User> QAMag = userManager.getQAManager(ideaRequest.getDepartmentId());
                if (QAMag != null) {
                    for (User qa : QAMag) {
                        MailRequest mailReq = new MailRequest();
                        mailReq.setContent("Your topic have a new idea, \n Click on this link to watch this: " + CLIENT_CONTEXTPATH + "/idea-detail/" + result);
                        mailReq.setReceiver(qa.getEmail());
                        mailReq.setSubject("New Idea for topic");
                        mailManager.sendMail(mailReq);
                    }
                }
                return responseUtils.getResponseEntity(result, 1, "Create Successfully", HttpStatus.OK);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return responseUtils.getResponseEntity(e, -1, "Login fail!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/update/files/{id}")
    public ResponseEntity<?> updateIdeaFiles(@RequestBody List<IdeaAttachmentRequest> ideaRequest,
                                             @PathVariable Long id,
                                             HttpServletRequest request) {
        try {
            String jwt = jwtUtils.getJwtFromRequest(request);
            String username = jwtUtils.getUserNameFromJwtToken(jwt);
            Long result = ideaManager.updateFile(ideaRequest, id, username);
            if (Objects.equals(result, 1l)) {
                return responseUtils.getResponseEntity(result, 1, "Create Successfully", HttpStatus.OK);
            }
            return responseUtils.getResponseEntity(null, -1, "Upload failed!", HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            return responseUtils.getResponseEntity(e, -1, "Upload failed!", HttpStatus.INTERNAL_SERVER_ERROR);
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

//    @GetMapping("/get")
//    public ResponseEntity<?> getIdeaWithModel(@RequestParam String searchKey) {
//        try {
//            List<IdeaResponseModel> result = ideaManager.getIdeaWithModel(searchKey);
//            if (result != null) {
//                return responseUtils.getResponseEntity(result, 1, "Create Successfully", HttpStatus.OK);
//            }
//            return responseUtils.getResponseEntity(null, -1, "Failed", HttpStatus.OK);
//        } catch (Exception e) {
//            return responseUtils.getResponseEntity(e, -1, "Login fail!", HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }

    @GetMapping("/get")
    public ResponseEntity<?> getIdeaWithSpec(@RequestParam(value = "searchKey", required = false) String searchKey,
                                             @RequestParam(value = "departmentId", required = false) Long deptFilter,
                                             @RequestParam(value = "academicId", required = false) Long acadFilter,
                                             @RequestParam(value = "topicId", required = false) Long topicId,
                                             @RequestParam(value = "categoryId", required = false) Long CategoryId,
                                             @RequestParam("page") Integer page,
                                             @RequestParam("limit") Integer limit,
                                             @RequestParam("sort") Integer sort) {
        try {
            PageDto result = ideaManager.getIdeaWithSpec(searchKey, acadFilter, deptFilter, topicId, CategoryId, page, limit, sort);
            if (result != null) {
                return responseUtils.getResponseEntity(result, 1, "Create Successfully", HttpStatus.OK);
            }
            return responseUtils.getResponseEntity(null, -1, "Failed", HttpStatus.OK);
        } catch (Exception e) {
            return responseUtils.getResponseEntity(e, -1, "Login fail!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<?> getIdeaWithSpec(@PathVariable Long id, HttpServletRequest request) {
        try {
            String jwt = jwtUtils.getJwtFromRequest(request);
            String username = jwtUtils.getUserNameFromJwtToken(jwt);
            String userId = jwtUtils.getUserIdFromJwtToken(jwt);
            IdeaDetailResponse result = ideaManager.getIdeaDetail(id, userId);
            if (result != null) {
                return responseUtils.getResponseEntity(result, 1, "Create Successfully", HttpStatus.OK);
            }
            return responseUtils.getResponseEntity(null, -1, "Failed", HttpStatus.OK);
        } catch (Exception e) {
            return responseUtils.getResponseEntity(e, -1, "Login fail!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/comment/create")
    public ResponseEntity<?> createIdea(@RequestBody CommentRequest commentRequest,
                                        HttpServletRequest request) {
        try {
            String jwt = jwtUtils.getJwtFromRequest(request);
            String username = jwtUtils.getUserNameFromJwtToken(jwt);
            String userId = jwtUtils.getUserIdFromJwtToken(jwt);
            IdeaComment result = ideaCommentManager.createComment(commentRequest, userId, username);
            if (result.getId() == null) {
                return responseUtils.getResponseEntity(null, -1, "Server error!!!", HttpStatus.INTERNAL_SERVER_ERROR);
            } else if (result.getId() != null && result.getId() == -2) {
                return responseUtils.getResponseEntity(null, -2, "The topic is closed", HttpStatus.OK);
            } else {
                Idea idea = ideaManager.getIdeaById(commentRequest.getIdeaId());
                MailRequest mailReq = new MailRequest();
                mailReq.setContent("Your Idea have a new comment, \n Click on this link to watch this: " + CLIENT_CONTEXTPATH + "/idea-detail/" + commentRequest.getIdeaId());
                mailReq.setReceiver(idea.getCreatedUser());
                mailReq.setSubject("New Idea for topic");
                mailManager.sendMail(mailReq);
                return responseUtils.getResponseEntity(result, 1, "Create Successfully", HttpStatus.OK);
            }
        } catch (Exception e) {
            return responseUtils.getResponseEntity(e, -1, "Server error!!!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/comment/edit/{id}")
    public ResponseEntity<?> editComment(@PathVariable Long id,
                                         @RequestBody String content,
                                        HttpServletRequest request) {
        try {
            String jwt = jwtUtils.getJwtFromRequest(request);
            String username = jwtUtils.getUserNameFromJwtToken(jwt);
            Integer result = ideaCommentManager.editComment(id, content, username);
            if (result == 0) {
                return responseUtils.getResponseEntity(null, -1, "Server error!!!", HttpStatus.INTERNAL_SERVER_ERROR);
            } else if (result == -2) {
                return responseUtils.getResponseEntity(null, -2, "The topic is closed", HttpStatus.OK);
            } else {
                return responseUtils.getResponseEntity(result, 1, "Create Successfully", HttpStatus.OK);
            }
        } catch (Exception e) {
            return responseUtils.getResponseEntity(e, -1, "Server error!!!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/comment/delete/{id}")
    public ResponseEntity<?> deleteComment(@PathVariable Long id, HttpServletRequest request) {
        try {
            String jwt = jwtUtils.getJwtFromRequest(request);
            Integer result = ideaCommentManager.deleteComt(id);
            if (result == 1) {
                return responseUtils.getResponseEntity(result, 1, "Create Successfully", HttpStatus.OK);
            }
            return responseUtils.getResponseEntity(null, -1, "Failed", HttpStatus.OK);
        } catch (Exception e) {
            return responseUtils.getResponseEntity(e, -1, "Login fail!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/comment/get/{id}")
    public ResponseEntity<?> getComment(@PathVariable Long id, HttpServletRequest request) {
        try {
            String jwt = jwtUtils.getJwtFromRequest(request);
            String userId = jwtUtils.getUserIdFromJwtToken(jwt);
            List<IdeaCommentResponse> result = ideaCommentManager.getComment(id, userId);
            if (result != null) {
                return responseUtils.getResponseEntity(result, 1, "Create Successfully", HttpStatus.OK);
            }
            return responseUtils.getResponseEntity(null, -1, "Failed", HttpStatus.OK);
        } catch (Exception e) {
            return responseUtils.getResponseEntity(e, -1, "Login fail!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/view/create/{id}")
    public ResponseEntity<?> createView(@PathVariable Long id, HttpServletRequest request) {
        try {
            String jwt = jwtUtils.getJwtFromRequest(request);
            String username = jwtUtils.getUserNameFromJwtToken(jwt);
            Integer result = ideaViewCountManager.countView(id, username);
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

    @PostMapping("/reaction")
    public ResponseEntity<?> getStatus(@RequestBody Map<String, Object> body, HttpServletRequest request) {
        try {
            String jwt = jwtUtils.getJwtFromRequest(request);
            String username = jwtUtils.getUserNameFromJwtToken(jwt);
            Long ideaId = Long.parseLong(body.get("ideaId").toString());
            Integer evaluation = Integer.parseInt(body.get("evaluation").toString());
            Integer result = ideaReactionManager.ReactIdea(ideaId, username, evaluation);
            if (result == 1) {
                return responseUtils.getResponseEntity(result, 1, "Create Successfully", HttpStatus.OK);
            }
            return responseUtils.getResponseEntity(null, -1, "Failed", HttpStatus.OK);
        } catch (Exception e) {
            return responseUtils.getResponseEntity(e, -1, "Login fail!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/get/status/{id}")
    public ResponseEntity<?> getStatus(@PathVariable Long id, HttpServletRequest request) {
        try {
            String jwt = jwtUtils.getJwtFromRequest(request);
            String username = jwtUtils.getUserNameFromJwtToken(jwt);
            IdeaStatusCountResponse result = ideaReactionManager.getIdeaStatus(id, username);
            if (result != null) {
                return responseUtils.getResponseEntity(result, 1, "Create Successfully", HttpStatus.OK);
            }
            return responseUtils.getResponseEntity(null, -1, "Failed", HttpStatus.OK);
        } catch (Exception e) {
            return responseUtils.getResponseEntity(e, -1, "Login fail!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/dashboard/get")
    public ResponseEntity<?> getStatus(@RequestParam("year") String year,
                                       @RequestParam(value = "semester", required = false) String semester,
                                       @RequestParam(value = "department", required = false) Long department,
                                       @RequestParam(value = "topic", required = false) Long topic,
                                       HttpServletRequest request) {
        try {
            String jwt = jwtUtils.getJwtFromRequest(request);
            String username = jwtUtils.getUserNameFromJwtToken(jwt);
            DashboardResponse result = ideaManager.getDashboard(year, semester, department, topic);
            if (result != null) {
                return responseUtils.getResponseEntity(result, 1, "Create Successfully", HttpStatus.OK);
            }
            return responseUtils.getResponseEntity(null, -1, "Failed", HttpStatus.OK);
        } catch (Exception e) {
            return responseUtils.getResponseEntity(e, -1, "Login fail!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
