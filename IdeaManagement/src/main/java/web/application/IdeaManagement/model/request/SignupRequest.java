package web.application.IdeaManagement.model.request;

import lombok.Data;

import java.util.Set;

@Data
public class SignupRequest {
    private Long id;
    private String userId;
    private Long departmentId;
    private String username;
    private String firstname;
    private String lastname;
    private String email;
    private String sex;
    private String avatar;
    private String password;
    private String phone;
    private String fullname;
    private String address;
    private Set<Long> role;
    private Long roleId;

    public void setId(String id) {
        this.id = null;
    }
}
