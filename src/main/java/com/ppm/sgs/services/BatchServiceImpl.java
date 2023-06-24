package com.ppm.sgs.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ppm.sgs.constants.Status;
import com.ppm.sgs.exceptions.ResourceNotFoundException;
import com.ppm.sgs.models.Batch;
import com.ppm.sgs.models.BatchDto;
import com.ppm.sgs.models.Course;
import com.ppm.sgs.models.User;
import com.ppm.sgs.repositories.BatchRepository;
import com.ppm.sgs.repositories.CourseRepository;
import com.ppm.sgs.repositories.UserRepository;

@Service
public class BatchServiceImpl implements BatchService {

    @Autowired
    private BatchRepository batchRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<Batch> getAllBatches() {
        return batchRepository.findAll();
    }

    @Override
    public List<Batch> getActiveBatchesByCourse(Course course) {
        return batchRepository.findByCourseAndStatusOrderByIdDesc(course, Status.ACTIVE);
    }

    @Override
    public List<Batch> getArchivedBatchesByCourse(Course course) {
        return batchRepository.findByCourseAndStatusOrderByIdDesc(course, Status.ARCHIVED);
    }

    @Override
    public Batch convertDtoToEntity(BatchDto batchDto) {
        // Get course obj
        String courseId = batchDto.getCourseId();
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + courseId));

        // Get user obj
        String lecturerId = batchDto.getLecturerId();
        User lecturer = userRepository.findById(lecturerId)
                .orElseThrow(() -> new ResourceNotFoundException("Lecturer not found with id: " + lecturerId));

        // prepare batch obj
        List<Batch> batches = batchRepository.findByCourseIdOrderByNumberDesc(courseId);
        short number = 1;
        if(batches.size() > 0) {
            number = batches.get(0).getNumber();
            number++;
        }
        
        Batch batch = new Batch(null, number++, course, batchDto.getStartDate(), batchDto.getEndDate(), lecturer, Status.ACTIVE);
        return batch;
    }

    @Override
    public void save(Batch batch) {
        batchRepository.saveAndFlush(batch);
    }

}
