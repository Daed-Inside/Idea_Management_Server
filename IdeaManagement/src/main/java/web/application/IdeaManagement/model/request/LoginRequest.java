package web.application.IdeaManagement.model.request;

import lombok.Data;

@Data
public class LoginRequest {
    private String username;
    private String password;
    private String email;
}
