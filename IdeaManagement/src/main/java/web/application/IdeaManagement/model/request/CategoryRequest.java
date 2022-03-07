package web.application.IdeaManagement.model.request;

import lombok.Data;

import java.util.Date;

@Data
public class CategoryRequest {
    private Long topicId;
    private String category;
    private String createdUser;
}
