package web.application.IdeaManagement.model.request;

import lombok.Data;

import java.util.List;

@Data
public class RoleRequest {
    private Long id;
    private String name;
    private String description;
    private List<Long> permission;
}
