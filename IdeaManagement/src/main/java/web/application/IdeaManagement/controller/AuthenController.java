package web.application.IdeaManagement.controller;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;

import java.util.*;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import web.application.IdeaManagement.entity.Role;
import web.application.IdeaManagement.entity.User;
import web.application.IdeaManagement.manager.SystemManager;
import web.application.IdeaManagement.manager.UserDetailManager;
import web.application.IdeaManagement.model.request.LoginRequest;
import web.application.IdeaManagement.model.request.SignupRequest;
import web.application.IdeaManagement.model.response.JwtResponse;
import web.application.IdeaManagement.repository.RoleRepository;
import web.application.IdeaManagement.repository.UserRepository;
import web.application.IdeaManagement.utils.JwtUtils;
import web.application.IdeaManagement.utils.ResponseUtils;

import java.util.stream.Collectors;

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

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        try {
            UserDetailManager userDetails = systemManager.login(loginRequest);
            if (userDetails != null) {
                if (userDetails.getResponseMessage().equals("user_not_exist")) {
                    return responseUtils.getResponseEntity(null, -1, "USER IS NOT EXIST", HttpStatus.BAD_REQUEST);
                }
                if (userDetails.getResponseMessage().equals("wrong_password")) {
                    return responseUtils.getResponseEntity(null, -1, "WRONG PASSWORD", HttpStatus.BAD_REQUEST);
                }
                List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
                        .collect(Collectors.toList());
                return responseUtils.getResponseEntity(
                        new JwtResponse(userDetails.getJwt(), userDetails.getId(), userDetails.getUsername(), userDetails.getEmail(), roles),1,"Login success!", HttpStatus.OK);
            }
            return responseUtils.getResponseEntity(null, -1, "SERVER ERROR", HttpStatus.BAD_REQUEST);
        }catch (Exception e) {
            e.printStackTrace();
            return responseUtils.getResponseEntity(e, -1, "Login fail!", HttpStatus.INTERNAL_SERVER_ERROR);
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
            String username = signUpRequest.getEmail().substring(0, signUpRequest.getEmail().indexOf("@"));
            User user = new User(username, signUpRequest.getEmail(),
                    encoder.encode(signUpRequest.getPassword()));

            Set<Long> strRoles = signUpRequest.getRole();
            Set<Role> roles = new HashSet<>();

            if (strRoles == null) {
                Role userRole = roleDao.findByName("USER");

                roles.add(userRole);
            } else {
            }
            String generatedString = RandomStringUtils.random(10, true, true);
            user.setUsername(username);
            user.setAddress(signUpRequest.getAddress());
            user.setPhone(signUpRequest.getPhone());
            user.setDepartmentId(signUpRequest.getDepartmentId());
            user.setFirstname(signUpRequest.getFirstname());
            user.setLastname(signUpRequest.getLastname());
            user.setRoles(roles);
            user.setUserId(userId);
            user.setCreateDate(new Date());
            user.setCreatedUser("khiem");
            user.setOtpCode(generatedString);
            user.setOtpExpired(DateUtils.addMinutes(new Date(), 10));
            userDao.save(user);
            return responseUtils.getResponseEntity(null, 1, "Success", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return responseUtils.getResponseEntity(null, -1, "Fail", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
