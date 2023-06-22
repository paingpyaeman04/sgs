package com.ppm.sgs.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ppm.sgs.models.Student;

public interface StudentRepository extends JpaRepository<Student, String> {
    
}
