package com.ppm.sgs.services;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ppm.sgs.constants.Constants;
import com.ppm.sgs.dtos.StudentDto;
import com.ppm.sgs.exceptions.PhoneAlreadyExistsException;
import com.ppm.sgs.exceptions.ResourceNotFoundException;
import com.ppm.sgs.models.Batch;
import com.ppm.sgs.models.Student;
import com.ppm.sgs.repositories.BatchRepository;
import com.ppm.sgs.repositories.StudentRepository;
import com.ppm.sgs.utils.CustomBeanUtils;
import com.ppm.sgs.utils.UploadUtils;

@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private BatchRepository batchRepository;

    @Override
    public String save(Student student) {
        // Geneate id for student
        String lastId = "STU00001";
        Optional<Student> studentOpt = studentRepository.findFirstByOrderByIdDesc();
        if (studentOpt.isPresent()) {
            int prevId = Integer.parseInt(studentOpt.get().getId().substring(3));
            lastId = "STU" + String.format("%05d", prevId + 1);
        }
        student.setId(lastId);

        try {
            if (studentRepository.existsByPhone(student.getPhone())) {
                throw new PhoneAlreadyExistsException("Phone number already exists.");
            }
            studentRepository.saveAndFlush(student);
        } catch (PhoneAlreadyExistsException e) {
            return e.getMessage();
        } catch (Exception e) {
            e.printStackTrace();
            return "Unknown error";
        }

        return null;
    }

    @Override
    public Page<Student> getStudents(int currentPage) {
        PageRequest pageable = PageRequest.of(currentPage, Constants.STUDENT_PAGE_SIZE);
        return studentRepository.findAll(pageable);
    }

    @Override
    public Student getById(String id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + id));
    }

    @Override
    public String update(Student student) {
        try {
            Optional<Student> studentOpt = studentRepository.findByPhone(student.getPhone());
            if(studentOpt.isPresent() && !studentOpt.get().getId().equals(student.getId())) {
                throw new PhoneAlreadyExistsException("Phone number already exists.");
            }

            Student oldStudent = studentRepository.findById(student.getId()).orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + student.getId()));

            CustomBeanUtils.copyNonNullProperties(student, oldStudent, "id");
            studentRepository.saveAndFlush(oldStudent);
        } catch (PhoneAlreadyExistsException e) {
            return e.getMessage();
        } catch (Exception e) {
            return "Unknown error";
        }
        return null;
    }

    @Override
    public void deleteOne(String id) {
        studentRepository.deleteById(id);
    }

    @Override
    public List<Student> searchByIdOrNameOrCourse(String id, String name, String course) {
        return studentRepository.findByIdAndName(id, name);
    }

    @Override
    public Student convertDtoToEntity(StudentDto studentDto) {
        Student student = new Student(studentDto.getId(), studentDto.getName(), studentDto.getDob(), studentDto.getGender().charAt(0), studentDto.getPhone(), null, studentDto.getDescription(), null);

        // Set batches
        List<Batch> batches = batchRepository.findByIdIn(studentDto.getBatchIds());
        student.setBatches(batches);

        // Set Photo URL
        MultipartFile photo = studentDto.getPhoto();
        if(photo == null || photo.getSize() <= 0) {
            return student;
        }

        try {
            String photoUrl = UploadUtils.dummyUpload(photo.getBytes(), photo.getContentType());
            student.setPhotoUrl(photoUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return student;
    }

    @Override
    public StudentDto convertEntityToDto(Student student) {
        StudentDto studentDto = new StudentDto(
            student.getId(), student.getName(), student.getDob(), student.getGender().toString(), student.getPhone(), null, student.getDescription(), null
        );

        List<Integer> batchIds = student.getBatches().stream().map(batch -> batch.getId()).collect(Collectors.toList());
        studentDto.setBatchIds(batchIds); 

        return studentDto;
    }

}
