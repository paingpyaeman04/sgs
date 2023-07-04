package com.ppm.sgs.services;

import java.util.List;

import com.ppm.sgs.models.Course;

public interface CourseService {
    List<Course> getAllActiveCourses();
    List<Course> getArchivedCourses();
    void restoreById(String id);
    Course getById(String id);
    String save(Course course);
    String update(Course course);
    void deleteById(String id);
    void archiveById(String id);
    List<Course> searchByName(String name);
}
