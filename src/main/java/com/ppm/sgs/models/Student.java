package com.ppm.sgs.models;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

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
@Entity
@Table(name = "students")
public class Student {
    @Id
    @Length(max = 8, message = "{student.valid.id.length}")
    private String id;

    @Length(max = 70, message = "{student.valid.name.length}")
    @NotBlank(message = "{student.valid.name.notblank}")
    private String name;

    @NotNull(message = "{student.valid.dob.notnull}")
    private Date dob;

    private Character gender;

    @Length(max = 13, message = "{student.valid.phone.length}")
    @NotBlank(message = "{student.phone.valid.notblank}")
    private String phone;

    @Column(name = "photo_url")
    @Length(max = 255, message = "{student.valid.photoUrl.length}")
    private String photoUrl;

    @Length(max = 65535, message = "{student.valid.description.length}")
    private String description;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "students_batches", joinColumns = @JoinColumn(name = "student_id"), inverseJoinColumns = @JoinColumn(name = "batch_id"))
    @JsonIgnoreProperties("students")
    private List<Batch> batches;
}
