package web.application.IdeaManagement.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name = "departments")
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "department", length = 50, nullable = false)
    private String department;

    @Column(name = "create_date", nullable = false)
    private Date createDate;

    @Column(name = "updated_date")
    private Date updatedDate;

}
