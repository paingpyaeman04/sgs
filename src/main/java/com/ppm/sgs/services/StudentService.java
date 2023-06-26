package com.ppm.sgs.services;

import java.util.List;

import org.springframework.data.domain.Page;

import com.ppm.sgs.dtos.StudentDto;
import com.ppm.sgs.models.Student;

public interface StudentService {
    String save(Student student);
    Student convertDtoToEntity(StudentDto studentDto);
    StudentDto convertEntityToDto(Student student);
    Page<Student> getStudents(int currentPage);
    Student getById(String id);
    String update(Student student);
    void deleteOne(String id);
    List<Student> searchByIdOrNameOrCourse(String id, String name, String course);
}
