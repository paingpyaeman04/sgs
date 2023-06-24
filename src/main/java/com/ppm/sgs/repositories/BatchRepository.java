package com.ppm.sgs.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ppm.sgs.constants.Status;
import com.ppm.sgs.models.Batch;
import com.ppm.sgs.models.Course;

@Repository
public interface BatchRepository extends JpaRepository<Batch, Integer> {
    List<Batch> findByCourse(Course course);

    List<Batch> findByCourseAndStatusOrderByIdDesc(Course course, Status status);

    List<Batch> findByCourseIdOrderByNumberDesc(String courseId);
}
