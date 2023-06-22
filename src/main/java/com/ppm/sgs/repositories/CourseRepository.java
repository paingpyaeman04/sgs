package com.ppm.sgs.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ppm.sgs.constants.Status;
import com.ppm.sgs.models.Course;

@Repository
public interface CourseRepository extends JpaRepository<Course, String> {
    Optional<Course> findFirstByOrderByIdDesc();
    List<Course> findByName(String name);
    List<Course> findByStatus(Status status);
    boolean existsByName(String name);
}
