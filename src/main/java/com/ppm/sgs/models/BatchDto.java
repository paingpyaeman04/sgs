package com.ppm.sgs.models;

import java.util.Date;

import javax.validation.constraints.NotNull;

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
public class BatchDto {
    private Integer id;

    @NotNull(message = "{batchdto.valid.courseid.notnull}")
    private String courseId;

    @NotNull(message = "{batchdto.valid.startdate.notnull}")
    private Date startDate;

    private Date endDate;

    @NotNull(message = "{batchdto.valid.lecturerid.notnull}")
    private String lecturerId;
}
