package web.application.IdeaManagement.model.response;

import lombok.Data;
import lombok.NoArgsConstructor;
import web.application.IdeaManagement.entity.IdeaAttachment;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
public class IdeaDetailResponse {
    private String category;
    private String topic;
    private String title;
    private String content;
    private Boolean isAnonymous;
    private String createdUser;
    private Date createdDate;
    private List<FileResponse> listAttachment;

    public IdeaDetailResponse(String category, String topic, String title, String content, Boolean isAnonymous, String createdUser, Date createdDate) {
        this.category = category;
        this.topic = topic;
        this.title = title;
        this.content = content;
        this.isAnonymous = isAnonymous;
        this.createdUser = createdUser;
        this.createdDate = createdDate;
    }
}
