package web.application.IdeaManagement.model.request;

import lombok.Data;

@Data
public class CommentRequest {
    private Long rootParent;
    private Long parentId;
    private Long ideaId;
    private String userId;
    private String content;
    private Boolean isAnonymous;
}
