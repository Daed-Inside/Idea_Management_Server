package web.application.IdeaManagement.model.request;

import lombok.Data;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Data
public class IdeaRequest {
    private Long departmentId;
    private Long categoryId;
    private Long topicId;
    private String title;
    private String description;
    private Boolean contributor;
    private List<IdeaAttachmentRequest> files;
}
