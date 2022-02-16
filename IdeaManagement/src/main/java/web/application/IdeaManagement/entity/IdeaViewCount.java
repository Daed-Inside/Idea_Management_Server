package web.application.IdeaManagement.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name = "idea_view_count")
public class IdeaViewCount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", length = 50, nullable = false)
    private String userId;

    @Column(name = "idea_id", nullable = false)
    private Long ideaId;

    @Column(name = "latest_view_date", nullable = false)
    private Date latestViewDate;

}
