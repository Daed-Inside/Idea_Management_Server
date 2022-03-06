package web.application.IdeaManagement.model.request;

import lombok.Data;

import java.util.Date;

@Data
public class AcademicYearRequest {
    private String semester;
    private String year;
    private Date startDate;
    private Date endDate;
    private String createdUser;
}
