package web.application.IdeaManagement.model.request;

import lombok.Data;

import java.util.Date;

@Data
public class DepartmentRequest {
    private String department;
    private String createdUser;
}
