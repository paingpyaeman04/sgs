package com.ppm.sgs.dtos;

import java.util.Date;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class StudentDto {
    @Length(max = 70, message = "{student.valid.name.length}")
    @NotBlank(message = "{student.valid.name.notblank}")
    private String name;

    @NotNull(message = "{student.valid.dob.notnull}")
    private Date dob;

    @NotBlank(message = "{student.valid.gender.notblank}")
    @Length(max = 1)
    private String gender;

    @Length(max = 13, message = "{student.valid.phone.length}")
    @NotBlank(message = "{student.phone.valid.notblank}")
    private String phone;

    @NotNull(message = "{student.photo.valid.notnull}")
    private MultipartFile photo;

    @Length(max = 65535, message = "{student.valid.description.length}")
    private String description;
    
}
