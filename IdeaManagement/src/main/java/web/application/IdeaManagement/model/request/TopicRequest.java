package web.application.IdeaManagement.model.request;

import lombok.Data;

import java.util.Date;

@Data
public class TopicRequest {
    private Long academicId;
    private Long departmentId;
    private String topic;
    private String description;
    private Date endDate;
    private Date finalEndDate;
    private Date startDate;
    private String createdUser;
}
