package web.application.IdeaManagement.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name = "idea_attachment")
public class IdeaAttachment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", length = 50, nullable = false)
    private String userId;

    @Column(name = "idea_id", nullable = false)
    private Long ideaId;

    @Column(name = "file_name", length = 100, nullable = false)
    private String fileName;

    @Column(name = "path", length = 250, nullable = false)
    private String path;

    @Column(name = "download_url", length = 250, nullable = false)
    private String downloadUrl;

    @Column(name = "create_date", nullable = false)
    private Date createDate;

    @Column(name = "updated_date")
    private Date updatedDate;
}
