package web.application.IdeaManagement.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name = "idea_Comment")
public class IdeaComment extends BaseEntity{

    @Column(name = "root_parent", nullable = true)
    private Long rootParent;

    @Column(name = "parent", nullable = true)
    private Long parent;

    @Column(name = "idea_id", nullable = false)
    private Long ideaId;

    @Column(name = "content", length = 1000, nullable = false)
    private String content;

    @Column(name = "is_anonymous", nullable = false)
    private Boolean isAnonymous;

    @Column(name = "user_id", length = 150, nullable = false)
    private String userId;
}
