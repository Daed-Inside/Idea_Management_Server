package web.application.IdeaManagement.model.request;

import lombok.Data;

import java.util.Date;

@Data
public class AcademicYearRequestModel {
    public Long id;
    public String userId;
    public String semester;
    public String year;
    public Date startDate;
    public Date endDate;
}
