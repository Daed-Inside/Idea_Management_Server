package web.application.IdeaManagement.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name = "departments")
public class Department extends BaseEntity{

    @Column(name = "department", length = 50, nullable = false)
    private String department;

}
