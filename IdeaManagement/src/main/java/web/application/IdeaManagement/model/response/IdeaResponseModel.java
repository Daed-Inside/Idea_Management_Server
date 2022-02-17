package web.application.IdeaManagement.model.response;

import lombok.Data;

import java.util.Date;

@Data
public class IdeaResponseModel {
    public Long id;
    public String ideaTitle;
    public String ideaContent;
    public Date createdDate;
    public Date modifiedDate;

    public IdeaResponseModel(Long id, String ideaTitle, String ideaContent) {
        this.id = id;
        this.ideaTitle = ideaTitle;
        this.ideaContent = ideaContent;
    }
}
