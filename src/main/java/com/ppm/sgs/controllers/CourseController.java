package com.ppm.sgs.controllers;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ppm.sgs.constants.Status;
import com.ppm.sgs.models.Batch;
import com.ppm.sgs.models.Course;
import com.ppm.sgs.services.BatchService;
import com.ppm.sgs.services.CourseService;

@Controller
@RequestMapping("/courses")
@SessionAttributes("statusType")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private BatchService batchService;

    @GetMapping("/add")
    public String addCourseForm(ModelMap map) {
        Course course = new Course();
        map.addAttribute("course", course);
        return "add-course";
    }

    @PostMapping("/add")
    public String addCourse(RedirectAttributes redirectAttributes, @Valid @ModelAttribute("course") Course course,
            BindingResult result, ModelMap map) {
        if (result.hasErrors()) {
            return "add-course";
        }
        String msg = courseService.save(course);
        if (msg != null) {
            map.addAttribute("error", msg);
            return "add-course";
        }

        redirectAttributes.addFlashAttribute("success", "Course is successfully added.");
        return "redirect:add";
    }

    @GetMapping("/all")
    public String displayAllActiveCourses(ModelMap map) {
        List<Course> courses = courseService.getAllActiveCourses();
        map.addAttribute("courses", courses);
        map.addAttribute("statusType", Status.ACTIVE);
        return "courses";
    }

    @GetMapping("/archived")
    public String displayArchivedCourse(ModelMap map) {
        List<Course> archivedCourses = courseService.getArchivedCourses();
        map.addAttribute("courses", archivedCourses);
        map.addAttribute("statusType", Status.ARCHIVED);
        return "courses";
    }

    @GetMapping("{id}")
    public String displayUpdateCourseForm(@PathVariable("id") String id, ModelMap map) {
        Course course = courseService.getById(id);
        map.addAttribute("course", course);
        return "course-update";
    }

    @PostMapping("update")
    public String updateCourse(@Valid @ModelAttribute("course") Course course,
            BindingResult result, ModelMap map) {
        if (result.hasErrors()) {
            return "course-update";
        }

        String msg = courseService.update(course);
        if (msg != null) {
			// failed to update; back to course update
			map.addAttribute("error", msg);
			return "course-update";
		}

        return "redirect:all";
    }

    @GetMapping("/search")
    public String search(ModelMap map, @RequestParam("name") String name, @RequestParam(name = "statusType", required = false) Optional<Integer> statusOpt) {
        int status = statusOpt.orElse(0);
        Status statusType =  status == 1 ? Status.ARCHIVED : Status.ACTIVE;
        List<Course> courses = courseService.searchByName(name);

        // filter acccording to type
        courses = courses.stream().filter(course -> course.getStatus().equals(statusType)).collect(Collectors.toList());

        map.addAttribute("courses", courses);
        map.addAttribute("statusType", statusType);
        return "courses";
    }

    @PostMapping("/archive")
    public String archiveCourse(@RequestParam("course-id") String id) {
        courseService.archiveById(id);
        return "redirect:all";
    }

    @PostMapping("/restore")
    public String restoreCourse(@RequestParam("course-id") String id) {
        courseService.restoreById(id);
        return "redirect:all";
    }

    // Batches
    @GetMapping("/{course-id}/batches")
    public String getBatchesInCourse(@PathVariable("course-id") String courseId, ModelMap map) {
        Status statusType = (Status) map.getAttribute("statusType");
        Course course = courseService.getById(courseId);
        List<Batch> batches = null;
        if (statusType == Status.ARCHIVED) {
            batches = batchService.getArchivedBatchesByCourse(course);
        } else {
            batches = batchService.getActiveBatchesByCourse(course);
        }

        map.addAttribute("batches", batches);
        map.addAttribute("course", course);
        return "batches";
    }

    // Model Attributes
    @ModelAttribute("statusTypes")
    public Map<Status, Integer> statusTypes() {
        Map<Status, Integer> statusTypes = new LinkedHashMap<>();
        statusTypes.put(Status.ACTIVE, 0);
        statusTypes.put(Status.ARCHIVED, 1);
        return statusTypes;
    }
}
