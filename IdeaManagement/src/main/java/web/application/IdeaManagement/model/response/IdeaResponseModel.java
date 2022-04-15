package web.application.IdeaManagement.model.response;

import lombok.Data;

import java.util.Date;

@Data
public class IdeaResponseModel {
    public Long id;
    public String ideaTitle;
    public String ideaContent;
    public String topic;
    public String category;
    public String createdUser;
    public Date createdDate;
    public Date modifiedDate;

    public IdeaResponseModel(Long id, String ideaTitle, String ideaContent, String topic, String category, Date createdDate, String createdUser) {
        this.id = id;
        this.ideaTitle = ideaTitle;
        this.ideaContent = ideaContent;
        this.topic = topic;
        this.category = category;
        this.createdDate = createdDate;
        this.createdUser = createdUser;
    }

    public IdeaResponseModel(Long id, String ideaTitle, String ideaContent) {
        this.id = id;
        this.ideaTitle = ideaTitle;
        this.ideaContent = ideaContent;
    }
}
