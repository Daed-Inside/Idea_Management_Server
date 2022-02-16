package web.application.IdeaManagement.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name = "academic_year")
public class AcademicYear {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "year", length = 10, nullable = false)
    private String year;

    @Column(name = "start_date", nullable = false)
    private Date startDate;

    @Column(name = "end_date", nullable = false)
    private Date endDate;

    @Column(name = "created_user", length = 50, nullable = false)
    private String createdUser;

    @Column(name = "create_date", nullable = false)
    private Date createDate;

    @Column(name = "updated_date")
    private Date updatedDate;



}
