package com.ppm.sgs.dtos;

import java.util.List;

import javax.persistence.Column;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.Length;

import com.ppm.sgs.validators.ValidPassword;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserDto {

	private String id;

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
    @ValidPassword
	private String password;

    @Length(max = 255, message = "{user.valid.password.length}")
	@NotBlank(message = "{user.valid.password.notblank}")
    private String confirmPassword;

	private Boolean enabled;

    private List<Integer> roleIds;
    
}
