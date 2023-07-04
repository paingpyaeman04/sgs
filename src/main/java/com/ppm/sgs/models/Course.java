package com.ppm.sgs.models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.ppm.sgs.constants.Status;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Entity
@Table(name = "courses")
public class Course {
    @Id
    @Length(max = 6, message = "{course.valid.id.length}")
    private String id;

    @Length(max = 150, message = "{course.valid.name.length}")
    @NotBlank(message = "{course.valid.name.notblank}")
    private String name;

    @DecimalMin(value = "0.0", inclusive = true, message = "{course.valid.fee.min}")
    private Double fee = 0.0;

    @Length(max = 45, message = "{course.valid.duration.length}")
    @NotBlank(message = "{course.valid.duration.notblank}")
    private String duration;

    @NotNull(message = "{course.valid.status.notnull}")
    private Status status = Status.ACTIVE;

}
