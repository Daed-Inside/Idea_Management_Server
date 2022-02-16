package web.application.IdeaManagement.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
@Data
@Table(name = "users", uniqueConstraints = { @UniqueConstraint(columnNames = "username"),
		@UniqueConstraint(columnNames = "email") })
public class User{

    @Id
	@Column(name = "user_id", length = 50, nullable = false)
	private Long userId;

	@Column(name = "department_id", nullable = false)
	private Long departmentId;

    @Column(name = "user_name", length = 50, nullable = false)
	private String username;

	@Column(name = "email", length = 50, nullable = false)
	private String email;

	@Column(name = "first_name", length = 50, nullable = false)
	private String firstName;

	@Column(name = "last_name", length = 50, nullable = false)
	private String lastName;

	@Column(name = "age")
	private Long age;

	@Column(name = "phone", length = 15, nullable = false)
	private String phone;

	@Column(name = "address", length = 250, nullable = false)
	private String address;

	@Column(name = "verified_email")
	private Boolean verifiedEmail ;

	@Column(name = "otp_code", length = 10, nullable = false)
	private String otpCode;

	@Column(name = "otp_expired", nullable = false)
	private Date otpExpired ;

	@Column(name = "create_date", nullable = false)
	private Date createDate;

	@Column(name = "updated_date")
	private Date updatedDate;

	@Column(name = "created_user", length = 50, nullable = false)
	private String createdUser;

	@Column(name = "updated_user", length = 50, nullable = false)
	private String updatedUser;


	// Chưa có trên ERD
    @Column(name = "password")
	private String password;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "bd_user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Set<Role> roles;

	@Column(name = "is_admin")
	private Boolean isAdmin = false;

	public User(String username, String email, String encode) {
		this.username = username;
		this.email = email;
		this.password = encode;
	}

	public User() {

	}

	@Transient
	private String responseMessage;

	@Transient
	private String jwt;
}
