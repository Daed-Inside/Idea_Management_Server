package web.application.IdeaManagement.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "users", uniqueConstraints = { @UniqueConstraint(columnNames = "username"),
		@UniqueConstraint(columnNames = "email") })
public class User{

    @Id
	@Column(name = "user_id", length = 50, nullable = false)
	private String userId;

	@Column(name = "department_id", nullable = false)
	private Long departmentId;

    @Column(name = "username", length = 50, nullable = false)
	private String username;

	@Column(name = "email", length = 50, nullable = false)
	private String email;

	@Column(name = "firstname", length = 50, nullable = false)
	private String firstname;

	@Column(name = "lastname", length = 50, nullable = false)
	private String lastname;

	@Column(name = "sex", length = 15, nullable = false)
	private String sex;

	@Column(name = "avatar", length = 500)
	private String avatar;

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

	@Column(name = "created_date", nullable = false)
	private Date createdDate;

	@Column(name = "updated_date")
	private Date updatedDate;

	@Column(name = "created_user", length = 50, nullable = false)
	private String createdUser;

	@Column(name = "updated_user", length = 50)
	private String updatedUser;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Long getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Long departmentId) {
		this.departmentId = departmentId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public Long getAge() {
		return age;
	}

	public void setAge(Long age) {
		this.age = age;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Boolean getVerifiedEmail() {
		return verifiedEmail;
	}

	public void setVerifiedEmail(Boolean verifiedEmail) {
		this.verifiedEmail = verifiedEmail;
	}

	public String getOtpCode() {
		return otpCode;
	}

	public void setOtpCode(String otpCode) {
		this.otpCode = otpCode;
	}

	public Date getOtpExpired() {
		return otpExpired;
	}

	public void setOtpExpired(Date otpExpired) {
		this.otpExpired = otpExpired;
	}

	public Date getCreateDate() {
		return createdDate;
	}

	public void setCreateDate(Date createDate) {
		this.createdDate = createDate;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public String getCreatedUser() {
		return createdUser;
	}

	public void setCreatedUser(String createdUser) {
		this.createdUser = createdUser;
	}

	public String getUpdatedUser() {
		return updatedUser;
	}

	public void setUpdatedUser(String updatedUser) {
		this.updatedUser = updatedUser;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public Boolean getAdmin() {
		return isAdmin;
	}

	public void setAdmin(Boolean admin) {
		isAdmin = admin;
	}

	public String getResponseMessage() {
		return responseMessage;
	}

	public void setResponseMessage(String responseMessage) {
		this.responseMessage = responseMessage;
	}

	public String getJwt() {
		return jwt;
	}

	public void setJwt(String jwt) {
		this.jwt = jwt;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

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
