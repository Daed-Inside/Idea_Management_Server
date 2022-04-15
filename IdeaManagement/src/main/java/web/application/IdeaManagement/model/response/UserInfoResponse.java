package web.application.IdeaManagement.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoResponse {
    private String userId;
    private String username;
    private String firstname;
    private String lastname;
    private String email;
    private String departmentName;
    private Long departmentId;
    private String role;
    private String avatar;
}
