package com.ppm.sgs.dtos;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.validation.constraints.Email;

import org.hibernate.validator.constraints.Length;

import com.ppm.sgs.constants.OperationType;
import com.ppm.sgs.validators.ValidPassword;

public class UserUpdateDto {
    private String id;

    @Length(max = 70, message = "{user.valid.name.length}")
    private String name;

    @Length(max = 45, message = "{user.valid.username.length}")
    @Column(unique = true)
    private String username;

    @Length(max = 255, message = "{user.valid.email.length}")
    @Email(message = "{user.valid.email.format}")
    @Column(unique = true)
    private String email;

    @Length(max = 255, message = "{user.valid.password.length}")
    @ValidPassword(operation = OperationType.UPDATE)
    private String password;

    @Length(max = 255, message = "{user.valid.password.length}")
    private String confirmPassword;

    private Boolean enabled = false;

    private List<Integer> roleIds = new ArrayList<>();

    public UserUpdateDto() {
    }

    public UserUpdateDto(String id, @Length(max = 70, message = "{user.valid.name.length}") String name,
            @Length(max = 45, message = "{user.valid.username.length}") String username,
            @Length(max = 255, message = "{user.valid.email.length}") @Email(message = "{user.valid.email.format}") String email,
            @Length(max = 255, message = "{user.valid.password.length}") String password,
            @Length(max = 255, message = "{user.valid.password.length}") String confirmPassword, Boolean enabled,
            List<Integer> roleIds) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.email = email;
        this.password = password;
        this.confirmPassword = confirmPassword;
        this.enabled = enabled;
        this.roleIds = roleIds;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public List<Integer> getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(List<Integer> roleIds) {
        this.roleIds = roleIds;
    }

    @Override
    public String toString() {
        return "UserDto [id=" + id + ", name=" + name + ", username=" + username + ", email=" + email + ", password="
                + password + ", confirmPassword=" + confirmPassword + ", enabled=" + enabled + ", roleIds=" + roleIds
                + "]";
    }

}
