package web.application.IdeaManagement.model.response;

import lombok.Data;

import java.util.Date;

@Data
public class CategoryReponse {
    private Long id;
    private String tagLabel;
    private String topicLabel;
    private Date createdDate;
    private String createdUser;
}
