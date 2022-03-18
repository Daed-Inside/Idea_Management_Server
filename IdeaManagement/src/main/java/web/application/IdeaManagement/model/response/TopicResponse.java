package web.application.IdeaManagement.model.response;

import lombok.Data;

import java.util.Date;

@Data
public class TopicResponse {
    private Long id;
    private String topic;
    private String year;
    private String semester;
    private String department;
    private Date startDate;
    private Date closureDate;
    private Date finalDate;

    public TopicResponse(Long id, String topic, String year, String semester, String department, Date startDate, Date closureDate, Date finalDate) {
        this.id = id;
        this.topic = topic;
        this.year = year;
        this.semester = semester;
        this.department = department;
        this.startDate = startDate;
        this.closureDate = closureDate;
        this.finalDate = finalDate;
    }
}
