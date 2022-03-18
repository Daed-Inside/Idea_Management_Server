package web.application.IdeaManagement.model.response;

import lombok.Data;

import java.util.Date;

@Data
public class AcademicYearReponse {
    private Long id;
    private String semester;
    private String year;
    private Date startDate;
    private Date endDate;

    public AcademicYearReponse(Long id, String semester, String year, Date startDate, Date endDate) {
        this.id = id;
        this.semester = semester;
        this.year = year;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
