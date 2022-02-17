package web.application.IdeaManagement.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name = "ideas")
public class Idea extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", length = 50, nullable = false)
    private String userId;

    @Column(name = "topic_id", nullable = false)
    private Long topicId;

    @Column(name = "category_id", nullable = false)
    private Long categoryId;

    @Column(name = "idea_title", length = 100, nullable = false)
    private String ideaTitle;

    @Column(name = "idea_content", length = 3000, nullable = false)
    private String ideaContent;

    @Column(name = "is_anonymous", nullable = false)
    private Boolean isAnonymous ;

}
