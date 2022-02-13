package web.application.IdeaManagement.manager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import web.application.IdeaManagement.entity.User;
import web.application.IdeaManagement.model.request.LoginRequest;
import web.application.IdeaManagement.repository.UserRepository;
import web.application.IdeaManagement.utils.JwtUtils;

@Service
public class SystemManager {
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    JwtUtils jwtUtils;
    @Autowired
    UserRepository userRepository;

    public UserDetailManager login(LoginRequest request) {
        try {
            UserDetailManager userDetails = new UserDetailManager();
            User existUser = userRepository.findByEmail(request.getEmail());
            if (existUser == null) {
                userDetails.setResponseMessage("user_not_exist");
                return userDetails;
            } else {
                try {
                    Authentication authentication = authenticationManager.authenticate(
                            new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    String jwt = jwtUtils.generateJwtToken(authentication);
                    userDetails = (UserDetailManager) authentication.getPrincipal();
                    userDetails.setJwt(jwt);
                    return userDetails;
                } catch (Exception e) {
                    userDetails.setResponseMessage("wrong_password");
                    return userDetails;
                }
            }

        } catch (Exception e) {
            return null;
        }
    }
}