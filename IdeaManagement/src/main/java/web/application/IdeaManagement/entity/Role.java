package web.application.IdeaManagement.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name",length = 50)
    private String name;

    @ManyToMany(mappedBy = "roles")
    private List<User> users;
}
