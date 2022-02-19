package web.application.IdeaManagement.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name = "idea_Comment")
public class IdeaComment extends BaseEntity{

    @Column(name = "user_id", length = 50, nullable = false)
    private String userId;

    @Column(name = "idea_id", nullable = false)
    private Long ideaId;

    @Column(name = "content", length = 1000, nullable = false)
    private String content;

    @Column(name = "is_anonymous", nullable = false)
    private Boolean isAnonymous;

    @Column(name = "create_date", nullable = false)
    private Date createDate;

    @Column(name = "updated_date")
    private Date updatedDate;
}
