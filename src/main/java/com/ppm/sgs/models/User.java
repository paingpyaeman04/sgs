package com.ppm.sgs.models;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.validator.constraints.Length;

@Entity
@Table(name = "users")
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Length(max = 70, message = "{user.valid.name.length}")
	@NotBlank(message = "{user.valid.name.notblank}")
	private String name;

	@Length(max = 45, message = "{user.valid.username.length}")
	@NotBlank(message = "{user.valid.username.notblank}")
	@Column(unique = true)
	private String username;

	@Length(max = 255, message = "{user.valid.email.length}")
	@NotBlank(message = "{user.valid.email.notblank}")
	@Email(message = "{user.valid.email.format}")
	@Column(unique = true)
	private String email;

	@Length(max = 255, message = "{user.valid.password.length}")
	@NotBlank(message = "{user.valid.password.notblank}")
	private String password;

	@ColumnDefault(value = "0")
	private Boolean enabled = false;

	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinTable(
		name = "users_roles", 
		joinColumns = @JoinColumn(name = "user_id"),
		inverseJoinColumns = @JoinColumn(name = "role_id")
	)
	private Set<Role> roles = new HashSet<>();

	public User() {
	}

	public User(Integer id,
			@Length(max = 70, message = "{user.valid.name.length}") @NotBlank(message = "{user.valid.name.notblank}") String name,
			@Length(max = 45, message = "{user.valid.username.length}") @NotBlank(message = "{user.valid.username.notblank}") String username,
			@Length(max = 255, message = "{user.valid.email.length}") @NotBlank(message = "{user.valid.email.notblank}") @Email(message = "{user.valid.email.format}") String email,
			@Length(max = 255, message = "{user.valid.password.length}") @NotBlank(message = "{user.valid.password.notblank}") String password,
			Boolean enabled) {
		this.id = id;
		this.name = name;
		this.username = username;
		this.email = email;
		this.password = password;
		this.enabled = enabled;
	}
	
	

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", username=" + username + ", email=" + email + ", password="
				+ password + ", enabled=" + enabled + ", roles=" + roles + "]";
	}

}
