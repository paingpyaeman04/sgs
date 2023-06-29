package com.ppm.sgs.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ppm.sgs.constants.Status;
import com.ppm.sgs.exceptions.ResourceAlreadyExistsException;
import com.ppm.sgs.exceptions.ResourceNotFoundException;
import com.ppm.sgs.models.Batch;
import com.ppm.sgs.models.Course;
import com.ppm.sgs.repositories.BatchRepository;
import com.ppm.sgs.repositories.CourseRepository;
import com.ppm.sgs.utils.CustomBeanUtils;

@Service
public class CourseServiceImpl implements CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private BatchRepository batchRepository;

    @Override
    public List<Course> getAllActiveCourses() {
        return courseRepository.findByStatus(Status.ACTIVE);
    }

    @Override
    public List<Course> getArchivedCourses() {
        return courseRepository.findByStatus(Status.ARCHIVED);
    }

    @Override
    public Course getById(String id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + id));
    }

    @Override
    public String save(Course course) {
        String lastId = "COU001";
        Optional<Course> courseOpt = courseRepository.findFirstByOrderByIdDesc();
        if (courseOpt.isPresent()) {
            int prevId = Integer.parseInt(courseOpt.get().getId().substring(3));
            lastId = "COU" + String.format("%03d", prevId + 1);
        }
        course.setId(lastId);

        try {
            if (courseRepository.existsByName(course.getName())) {
                throw new ResourceAlreadyExistsException("Course name already exists.");
            }
            courseRepository.saveAndFlush(course);
        } catch (ResourceAlreadyExistsException e) {
            return e.getMessage();
        } catch (Exception e) {
            return "Unknown error";
        }

        return null;
    }

    @Override
    public String update(Course course) {
        try {
            Optional<Course> courseOpt = courseRepository.findByName(course.getName());
            if (courseOpt.isPresent() && !courseOpt.get().getId().equals(course.getId())) {
                throw new ResourceAlreadyExistsException("Course name already exists.");
            }

            Course oldCourse = courseRepository.findById(course.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + course.getId()));
            CustomBeanUtils.copyNonNullProperties(course, oldCourse, "id");
            courseRepository.saveAndFlush(oldCourse);
        } catch (ResourceAlreadyExistsException e) {
            return e.getMessage();
        } catch (ResourceNotFoundException e) {
            return e.getMessage();
        } catch (Exception e) {
            e.printStackTrace();
            return "Unknown error";
        }

        return null;
    }

    @Override
    public void deleteById(String id) {
        courseRepository.deleteById(id);
    }

    @Override
    public void archiveById(String id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + id));
        course.setStatus(Status.ARCHIVED);

        // set all batches under the course to archived state
        List<Batch> batches = batchRepository.findByCourse(course);
        batches.forEach(batch -> batch.setStatus(Status.ARCHIVED));
        batchRepository.saveAll(batches);

        courseRepository.saveAndFlush(course);
    }

    @Override
    public Optional<Course> findByName(String name) {
        return courseRepository.findByName(name);
    }

    @Override
    public void restoreById(String id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + id));
        course.setStatus(Status.ACTIVE);

        // set all batches under the course to active state
        List<Batch> batches = batchRepository.findByCourse(course);
        batches.forEach(batch -> batch.setStatus(Status.ACTIVE));
        batchRepository.saveAll(batches);

        courseRepository.saveAndFlush(course);
    }

    @Override
    public List<Course> searchByName(String name) {
        return courseRepository.findByNameContaining(name);
    }

}
