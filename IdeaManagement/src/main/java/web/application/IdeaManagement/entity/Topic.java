package web.application.IdeaManagement.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name = "topics")
public class Topic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", length = 50, nullable = false)
    private String userId;

    @Column(name = "academic_id", nullable = false)
    private Long academicId;

    @Column(name = "department_id", nullable = false)
    private Long departmentId;

    @Column(name = "topic", nullable = false)
    private String topic;

    //Max = 1000
    @Column(name = "description",length = 1000)
    private String description;

    @Column(name = "start_date", nullable = false)
    private Date startDate;

    @Column(name = "end_date", nullable = false)
    private Date endDate;

    @Column(name = "final_end_date", nullable = false)
    private Date finalEndDate;

    @Column(name = "created_date", nullable = false)
    private Date createdDate;

    @Column(name = "updated_date")
    private Date updatedDate;

    @Column(name = "updated_user", length = 50)
    private String updatedUser;

}