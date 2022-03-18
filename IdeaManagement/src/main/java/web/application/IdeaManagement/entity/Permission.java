package web.application.IdeaManagement.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Data
@Table(name = "permission")
public class Permission extends BaseEntity{
    @Column(name = "name")
    private String name;

    @Column(name = "url")
    private String url;
}
