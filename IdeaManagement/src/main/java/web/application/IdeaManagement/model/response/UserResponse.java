package web.application.IdeaManagement.model.response;

import lombok.Data;

@Data
public class UserResponse {
    private String userId;
    private String email;
    private String firstname;
    private String lastname;
    private String address;
    private String phone;
    private String role;
    private String department;
    private Long departmentId;
    private Long roleId;
    private String sex;
}
