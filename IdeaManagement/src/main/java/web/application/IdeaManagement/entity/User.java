package web.application.IdeaManagement.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Set;

@Entity
@Data
@Table(name = "users", uniqueConstraints = { @UniqueConstraint(columnNames = "username"),
		@UniqueConstraint(columnNames = "email") })
public class User{
    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

    @Column(name = "username")
	private String username;

    @Column(name = "avatar")
	private String avatar;

    @Column(name = "password")
	private String password;

    @Column(name = "email")
	private String email;

    @Column(name = "sex")
	private String sex;

    @Column(name = "phone")
	private String phone;

    @Column(name = "fullname")
	private String fullname;

    @Column(name = "address")
	private String address;

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
