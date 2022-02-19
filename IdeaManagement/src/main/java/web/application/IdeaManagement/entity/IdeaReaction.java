package web.application.IdeaManagement.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name = "idea_reaction")
public class IdeaReaction extends BaseEntity{

    @Column(name = "user_id", length = 50, nullable = false)
    private String userId;

    @Column(name = "idea_id", nullable = false)
    private Long ideaId;

    @Column(name = "evaluation", nullable = false)
    private Boolean evaluation;

}
