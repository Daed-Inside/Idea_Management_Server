package web.application.IdeaManagement.model.response;

import lombok.Data;

import javax.swing.*;
import java.util.Date;

@Data
public class IdeaCommentResponse {
    private Long parent;
    private Long id;
    private String createdUser;
    private Date createdDate;
    private Long ideaId;
    private String content;
    private Boolean isAnonymous;
    private String userId;
}
