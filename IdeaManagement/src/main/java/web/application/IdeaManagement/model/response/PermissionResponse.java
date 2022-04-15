package web.application.IdeaManagement.model.response;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
public class PermissionResponse {
    private Long roleId;
    private Long id;
    private String name;
    private Integer count;
    private List<PermissionResponse> listPermission;


    public PermissionResponse() {
    }

    public PermissionResponse(Long roleId, Long id, String name) {
        this.roleId = roleId;
        this.id = id;
        this.name = name;
    }
}
