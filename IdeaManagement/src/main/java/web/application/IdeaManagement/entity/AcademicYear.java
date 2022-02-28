package web.application.IdeaManagement.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name = "academic_year")
public class AcademicYear extends BaseEntity{

    @Column(name = "semester", length = 100, nullable = false)
    private String semester;

    @Column(name = "year", length = 10, nullable = false)
    private String year;

    @Column(name = "start_date", nullable = false)
    private Date startDate;

    @Column(name = "end_date", nullable = false)
    private Date endDate;

}
