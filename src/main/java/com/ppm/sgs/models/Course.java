package com.ppm.sgs.models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.ppm.sgs.constants.Status;

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

    public Course() {
    }

    public Course(@Length(max = 6, message = "{course.valid.id.length}") String id,
            @Length(max = 150, message = "{course.valid.name.length}") @NotBlank(message = "{course.valid.name.notblank}") String name,
            @DecimalMin(value = "0.0", inclusive = true, message = "{course.valid.fee.min}") Double fee,
            @Length(max = 45, message = "{course.valid.duration.length}") @NotBlank(message = "{course.valid.duration.notblank}") String duration,
            @NotNull(message = "{course.valid.status.notnull}") Status status) {
        this.id = id;
        this.name = name;
        this.fee = fee;
        this.duration = duration;
        this.status = status;
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

    public Double getFee() {
        return fee;
    }

    public void setFee(Double fee) {
        this.fee = fee;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Course [id=" + id + ", name=" + name + ", fee=" + fee + ", duration=" + duration + ", status=" + status
                + "]";
    }

}
