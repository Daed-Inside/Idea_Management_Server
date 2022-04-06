package web.application.IdeaManagement.model.request;

import lombok.Data;

@Data
public class ChangePassRequest {
    private String oldPassword;
    private String newPassword;
    private String email;
}
