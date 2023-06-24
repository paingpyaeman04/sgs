package com.ppm.sgs.controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ppm.sgs.models.Batch;
import com.ppm.sgs.models.BatchDto;
import com.ppm.sgs.models.Course;
import com.ppm.sgs.models.User;
import com.ppm.sgs.services.BatchService;
import com.ppm.sgs.services.CourseService;
import com.ppm.sgs.services.UserService;

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
        if(result.hasErrors()) {
            map.addAttribute("courseId", batchDto.getCourseId());
            return "add-batch";
        }
        
        Batch batch = batchService.convertDtoToEntity(batchDto);
        batchService.save(batch);

        return "redirect:/courses/" + batchDto.getCourseId() + "/batches";
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

}
