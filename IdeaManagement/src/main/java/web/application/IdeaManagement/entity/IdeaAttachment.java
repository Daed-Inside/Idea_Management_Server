package web.application.IdeaManagement.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name = "idea_attachment")
public class IdeaAttachment extends BaseEntity{

    @Column(name = "idea_id", nullable = false)
    private Long ideaId;

    @Column(name = "file_name", length = 100, nullable = false)
    private String fileName;

    @Column(name = "file_type", length = 250, nullable = false)
    private String fileType;

    @Column(name = "path", length = 250, nullable = false)
    private String path;

    @Column(name = "download_url", length = 250, nullable = false)
    private String downloadUrl;

}
