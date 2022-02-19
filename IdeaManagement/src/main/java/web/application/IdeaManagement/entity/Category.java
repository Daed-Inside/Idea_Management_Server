package web.application.IdeaManagement.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name = "categories")
public class Category extends BaseEntity{

    @Column(name = "topic_id", nullable = false)
    private Long topicId;

    @Column(name = "category",length = 50, nullable = false)
    private String category;

}
