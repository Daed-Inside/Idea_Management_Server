package web.application.IdeaManagement.entity;

import lombok.Data;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Transactional(readOnly=false)
@Table(name = "base_entity")
public class BaseEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "create_user", nullable = false)
    private String createUser;

    @Column(name = "create_time", nullable = false)
    private Date createTime;

    @Column(name = "modified_user", nullable = false)
    private String modifiedUser;

    @Column(name = "modified_time", nullable = false)
    private Date modifiedTime;

    @Column(name = "is_deleted")
    private boolean isDeleted = Boolean.FALSE;

    public BaseEntity(Long id, String createUser, Date createTime, String modifiedUser, Date modifiedTime, boolean isDeleted) {
        this.id = id;
        this.createUser = createUser;
        this.createTime = createTime;
        this.modifiedUser = modifiedUser;
        this.modifiedTime = modifiedTime;
        this.isDeleted = isDeleted;
    }
}
