package com.ppm.sgs.dtos;

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
public class StudentReportDto {
    private String id;
    private String name;
    private String gender;
    private String phone;
    private String dob;
}
