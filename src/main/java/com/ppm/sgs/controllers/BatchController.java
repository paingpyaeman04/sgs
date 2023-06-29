package com.ppm.sgs.controllers;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ppm.sgs.dtos.StudentReportDto;
import com.ppm.sgs.models.Batch;
import com.ppm.sgs.models.BatchDto;
import com.ppm.sgs.models.Course;
import com.ppm.sgs.models.Student;
import com.ppm.sgs.models.User;
import com.ppm.sgs.services.BatchService;
import com.ppm.sgs.services.CourseService;
import com.ppm.sgs.services.UserService;
import com.ppm.sgs.utils.ReportUtils;

@Controller
@RequestMapping("/batches")
public class BatchController {

    @Autowired
    private BatchService batchService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private UserService userService;

    @GetMapping("add")
    public String displayAddBatchForm(@RequestParam(name = "course-id", required = false) String courseId,
            ModelMap map) {
        BatchDto batch = new BatchDto();
        map.addAttribute("batch", batch);
        map.addAttribute("courseId", courseId);
        return "add-batch";
    }

    @PostMapping("add")
    public String addNewBatch(ModelMap map, @Valid @ModelAttribute("batch") BatchDto batchDto, BindingResult result) {
        if (result.hasErrors()) {
            map.addAttribute("courseId", batchDto.getCourseId());
            return "add-batch";
        }

        Batch batch = batchService.convertDtoToEntity(batchDto);
        batchService.save(batch);

        return "redirect:/courses/" + batchDto.getCourseId() + "/batches";
    }

    @GetMapping("{id}/students")
    public String batchDetail(@PathVariable("id") Integer id, ModelMap map) {
        Batch batch = batchService.getById(id);
        map.addAttribute("batch", batch);
        return "batch-detail";
    }

    @GetMapping("{id}/report/batch-students")
    public void report(@PathVariable("id") Integer id, @RequestParam("type") String type,
            HttpServletResponse response) {
        Batch batch = batchService.getById(id);

        List<StudentReportDto> dtos = convertBatchToStudentReportDtos(batch);
        Map<String, Object> parameters = buildStudentListParameters(batch);

        ReportUtils.reportStudentListForBatch(response, parameters, dtos, type);
    }

    @GetMapping("{id}")
    public String displayUpdateForm(ModelMap map, @PathVariable("id") Integer id) {
        Batch batch = batchService.getById(id);
        String courseId = batch.getCourse().getId();
        BatchDto batchDto = new BatchDto(id, courseId, batch.getStartDate(),
                batch.getEndDate(), batch.getLecturer().getId());
        map.addAttribute("batch", batchDto);
        map.addAttribute("course", batch.getCourse());
        return "batch-update";
    }

    @PostMapping("update")
    public String updateBatch(ModelMap map, @Valid @ModelAttribute("batch") BatchDto batchDto, BindingResult result) {
        if (result.hasErrors()) {
            Course course = courseService.getById(batchDto.getCourseId());
            map.addAttribute("course", course);
            return "batch-update";
        }

        Batch batch = batchService.convertDtoToEntity(batchDto);
        batch.setId(batchDto.getId());
        batchService.update(batch);

        return "redirect:/courses/" + batchDto.getCourseId() + "/batches";
    }

    @GetMapping("/remove")
    public String removeStudent(@RequestParam("id") Integer batchId, @RequestParam("student-id") String studentId) {
        batchService.removeStudent(batchId, studentId);
        return "redirect:" + batchId + "/students";
    }

    @PostMapping("/delete")
    public String deleteBatch(@RequestParam("batch-id") Integer batchId, @RequestParam("course-id") String courseId) {
        batchService.deleteBatch(batchId);
        return "redirect:/courses/" + courseId + "/batches";
    }

    /* Model Attributes */

    @ModelAttribute("courses")
    public List<Course> courses() {
        List<Course> courses = courseService.getAllActiveCourses();
        return courses;
    }

    @ModelAttribute("lecturers")
    public List<User> lecturers() {
        return userService.getLecturers();
    }

    /* Helper methods */
    private List<StudentReportDto> convertBatchToStudentReportDtos(Batch batch) {
        List<Student> students = batch.getStudents();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

        return students.stream()
                .map(std -> new StudentReportDto(std.getId(), std.getName(),
                        std.getGender().equals('M') ? "Male" : "Female", std.getPhone(), sdf.format(std.getDob())))
                .collect(Collectors.toList());
    }

    private Map<String, Object> buildStudentListParameters(Batch batch) {
        String courseName = batch.getCourse().getName();
        Short batchNumber = batch.getNumber();
        String lecturerName = batch.getLecturer().getName();

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("courseDetail", courseName + " (Batch - " + batchNumber + ")");
        parameters.put("lecturerDetail", "Lecturer - " + lecturerName);
        return parameters;
    }

}
