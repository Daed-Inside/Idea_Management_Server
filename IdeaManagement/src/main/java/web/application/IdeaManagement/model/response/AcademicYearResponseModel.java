package web.application.IdeaManagement.model.response;

import lombok.Data;

import java.util.Date;

@Data
public class AcademicYearResponseModel {
    public String semester;
    public String year;
    public Date startDate;
    public Date endDate;

    public AcademicYearResponseModel(String semester, String year, Date startDate, Date endDate) {
        this.semester = semester;
        this.year = year;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
