package web.application.IdeaManagement.model.request;

import lombok.Data;

@Data
public class IdeaRequestModel {
    public Long id;
    public String userId;
    public Long categoryId;
    public Long topicId;
    public String ideaTitle;
    public String ideaContent;
    public Boolean isAnonymous;
}
