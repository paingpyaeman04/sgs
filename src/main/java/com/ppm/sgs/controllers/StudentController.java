package com.ppm.sgs.controllers;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ppm.sgs.dtos.StudentDto;
import com.ppm.sgs.models.Batch;
import com.ppm.sgs.models.Student;
import com.ppm.sgs.services.BatchService;
import com.ppm.sgs.services.StudentService;

@Controller
@RequestMapping("/students")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private BatchService batchService;

    @GetMapping("add")
    public String displayAddStudentForm(ModelMap map) {
        StudentDto student = new StudentDto();
        student.setGender("M");
        map.addAttribute("student", student);
        return "add-student";
    }

    @PostMapping("add")
    public String addNewStudent(RedirectAttributes redirectAttributes, ModelMap map,
            @Valid @ModelAttribute("student") StudentDto studentDto, BindingResult result) throws IOException {
        if (result.hasErrors()) {
            return "add-student";
        }

        Student student = studentService.convertDtoToEntity(studentDto);
        String msg = studentService.save(student);
        if (msg != null) {
            // failed to update; back to student update
            map.addAttribute("error", msg);
            return "add-student";
        }

        redirectAttributes.addFlashAttribute("success", "Student is successfully added.");
        return "redirect:add";

    }

    @GetMapping("/all")
    public String allStudents(@RequestParam("page") Optional<Integer> page, ModelMap map) {
        int currentPage = page.orElse(1);
		Page<Student> studentPage = studentService.getStudents(currentPage - 1);
		map.addAttribute("studentPage", studentPage);

		int totalPages = studentPage.getTotalPages();
		if (totalPages > 0) {
			List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
			map.addAttribute("pageNumbers", pageNumbers);
		}

        return "students";
    }

    @GetMapping("/{id}")
    public String updateForm(@PathVariable("id") String id, ModelMap map) {
        Student student = studentService.getById(id);

        StudentDto studentDto = studentService.convertEntityToDto(student);
        List<Batch> attendedBatches = batchService.getAttendedBatches(student.getBatches());

        map.addAttribute("attendedBatches", attendedBatches);
        map.addAttribute("photoUrl", student.getPhotoUrl());
        map.addAttribute("student", studentDto);
        return "student-update";
    }

    @GetMapping("/search")
    public String search(ModelMap map, @RequestParam("id") String id, @RequestParam("name") String name,
            @RequestParam("course") String course) {
        
        List<Student> students = studentService.searchByIdOrNameOrCourse(id, name, course);
        Page<Student> studentPage = new PageImpl<>(students);
        map.addAttribute("studentPage", studentPage);
        return "students";
    }

    @PostMapping("/update")
    public String update(ModelMap map,
            @Valid @ModelAttribute("student") StudentDto studentDto, BindingResult result) {

        if (result.hasErrors()) {
            return "student-update";
        }

        Student student = studentService.convertDtoToEntity(studentDto);
        String msg = studentService.update(student);
        if (msg != null) {
			// failed to update; back to student update
			map.addAttribute("error", msg);
			return "student-update";
		}

        return "redirect:all";
    }

    @GetMapping("/delete")
    public String delete(@RequestParam("id") String id) {
        studentService.deleteOne(id);
        return "redirect:all";
    }

    // ModelAttributes

    @ModelAttribute("futureBatches")
    public List<Batch> futureBatchs() {
        // List<Batch> futureBatches = batchService.getFutureBatches();
        return batchService.getFutureBatches();
    }
}
