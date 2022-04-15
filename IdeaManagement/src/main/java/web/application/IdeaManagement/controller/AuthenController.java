package web.application.IdeaManagement.controller;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;

import java.util.*;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import web.application.IdeaManagement.entity.Role;
import web.application.IdeaManagement.entity.User;
import web.application.IdeaManagement.manager.MailManager;
import web.application.IdeaManagement.manager.SystemManager;
import web.application.IdeaManagement.manager.UserDetailManager;
import web.application.IdeaManagement.model.request.LoginRequest;
import web.application.IdeaManagement.model.request.MailRequest;
import web.application.IdeaManagement.model.request.SignupRequest;
import web.application.IdeaManagement.model.response.JwtResponse;
import web.application.IdeaManagement.model.response.UserInfoResponse;
import web.application.IdeaManagement.repository.RoleRepository;
import web.application.IdeaManagement.repository.UserRepository;
import web.application.IdeaManagement.utils.JwtUtils;
import web.application.IdeaManagement.utils.ResponseUtils;

import java.util.stream.Collectors;

import static web.application.IdeaManagement.constant.constant.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/authen")
public class AuthenController {
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    UserRepository userDao;
    @Autowired
    RoleRepository roleDao;
    @Autowired
    PasswordEncoder encoder;
    @Autowired
    JwtUtils jwtUtils;
    @Autowired
    ResponseUtils responseUtils;
    @Autowired
    SystemManager systemManager;
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    MailManager mailManager;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        try {
            UserDetailManager userDetails = systemManager.login(loginRequest);
            if (userDetails != null) {
                if (userDetails.getResponseMessage().equals("user_not_exist")) {
                    return responseUtils.getResponseEntity(null, -1, "USER IS NOT EXIST", HttpStatus.OK);
                }
                if (userDetails.getResponseMessage().equals("wrong_password")) {
                    return responseUtils.getResponseEntity(null, -2, "WRONG PASSWORD", HttpStatus.OK);
                }
                List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
                        .collect(Collectors.toList());
                UserInfoResponse userInfo = modelMapper.map(userDetails, UserInfoResponse.class);
                userInfo.setUserId(userDetails.getId());
                if (!roles.isEmpty()) {
                    userInfo.setRole(roles.get(0));
                }
                return responseUtils.getResponseEntity(
                        new JwtResponse(userDetails.getJwt(), userInfo, roles),1,"Login success!", HttpStatus.OK);
            }
            return responseUtils.getResponseEntity(null, -1, "SERVER ERROR", HttpStatus.OK);
        }catch (Exception e) {
            e.printStackTrace();
            return responseUtils.getResponseEntity(e, SYSTEM_ERROR_CODE, SYSTEM_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignupRequest signUpRequest) {
        try {
            if (userDao.existsByUsername(signUpRequest.getUsername())) {

                return responseUtils.getResponseEntity(null, -2, "Error: Username is already taken!", HttpStatus.BAD_REQUEST);
            }

            if (userDao.existsByEmail(signUpRequest.getEmail())) {
                return responseUtils.getResponseEntity(null, -2, "Error: Email is already in use!", HttpStatus.BAD_REQUEST);
            }

            // Create new user's account
            String userId = UUID.randomUUID().toString();
//            String username = signUpRequest.getEmail().substring(0, signUpRequest.getEmail().indexOf("@"));
            User user = modelMapper.map(signUpRequest, User.class);

//            Set<Long> strRoles = signUpRequest.getRole();
//            String randomPass = RandomStringUtils.random(10, true, true);
            String randomPass = "1234";
            Long roleId = signUpRequest.getRoleId();
            Set<Role> roles = systemManager.getSignUpRole(roleId);
            String generatedString = RandomStringUtils.random(10, true, true);
            user.setUsername(signUpRequest.getEmail());
            user.setPassword(encoder.encode(randomPass));
            user.setSex(signUpRequest.getSex());
            user.setRoles(roles);
            user.setUserId(userId);
            user.setCreateDate(new Date());
            user.setCreatedUser("khiem");
            user.setOtpCode(generatedString);
            user.setOtpExpired(DateUtils.addMinutes(new Date(), 10));
            userDao.save(user);
            MailRequest mailReq = new MailRequest();
            mailReq.setContent("Your account was created successfully, \n Your password is: " + randomPass);
            mailReq.setReceiver(signUpRequest.getEmail());
            mailReq.setSubject("New account");
            mailManager.sendMail(mailReq);
            return responseUtils.getResponseEntity(null, 1, "Success", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return responseUtils.getResponseEntity(null, -1, "Fail", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/get/roles")
    public ResponseEntity<?> registerUser(@RequestParam(value = "search", required = false) String search) {
        try {
            List<Map<String, Object>> listReturn = systemManager.getRole();
            return responseUtils.getResponseEntity(listReturn, 1, "SUCCESS", HttpStatus.OK);
        } catch (Exception e) {
            return responseUtils.getResponseEntity(null, -1, "Fail", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
