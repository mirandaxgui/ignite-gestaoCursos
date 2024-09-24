package com.br.guilhermemiranda.gestao_cursos;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.br.guilhermemiranda.gestao_cursos.dto.ActiveCourseDTO;
import com.br.guilhermemiranda.gestao_cursos.dto.CourseRequestDTO;
import com.br.guilhermemiranda.gestao_cursos.dto.UpdateCourseDTO;
import com.br.guilhermemiranda.gestao_cursos.exceptions.UserNotFoundException;
import com.br.guilhermemiranda.gestao_cursos.services.CreateCourse;
import com.br.guilhermemiranda.gestao_cursos.services.ResumeCourses;

@RestController
@RequestMapping("/course")
public class CourseController {

    @Autowired
    private CreateCourse createCourse;
    @Autowired
    private ResumeCourses resumeCourses;
    @Autowired
    private CourseRepository courseRepository;

    @PostMapping("/")
    public ResponseEntity<Object> execute(@RequestBody CourseEntity courseEntity) {

        createCourse.create(courseEntity);

        return ResponseEntity.ok(courseEntity);
    }

    @GetMapping("/")
    public ResponseEntity<List<CourseEntity>> getAllCourses(@RequestBody CourseRequestDTO courseRequestDTO) {
        var nameCourse = courseRequestDTO.getName();
        var categoryCourse = courseRequestDTO.getCategory();

        List<CourseEntity> courses = resumeCourses.getAllCourses();

        if (nameCourse == null) {
            courseRequestDTO.setName("");
        }

        if (categoryCourse == null) {
            courseRequestDTO.setCategory("");
        }

        nameCourse = courseRequestDTO.getName().toLowerCase();
        categoryCourse = courseRequestDTO.getCategory().toLowerCase();
        List<CourseEntity> filteredCourses = new ArrayList<>(); // Inicializa a lista

        try {
            for (CourseEntity course : courses) {
                if ((course.getName() != null && course.getName().toLowerCase().equals(nameCourse))) {
                    filteredCourses.add(course);
                }
                if (course.getCategory() != null && course.getCategory().toLowerCase().equals(categoryCourse)) {
                    filteredCourses.add(course);
                }
            }

            if (filteredCourses.isEmpty()) {
                return ResponseEntity.badRequest().body(courses);
            }
            return ResponseEntity.ok(filteredCourses); // Retorna 200 OK com a lista filtrada

        } catch (Exception e) {
            // Log do erro para diagnóstico (opcional)
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // Retorna 500 Internal Server Error
        }
    }

    @PutMapping("/update/{id}")
    public CourseEntity update(@PathVariable UUID id, @RequestBody UpdateCourseDTO updateCourseDTO) {
        var course = this.courseRepository.findById(id).orElseThrow(() -> {
            throw new UserNotFoundException();
        });
        if (updateCourseDTO.getName() == null) {
            updateCourseDTO.setName(course.getName());
        }
        if (updateCourseDTO.getCategory() == null) {
            updateCourseDTO.setCategory(course.getCategory());
        }
        course.setName(updateCourseDTO.getName());
        course.setCategory(updateCourseDTO.getCategory());
        var updatedCourse = courseRepository.save(course);
        return updatedCourse;
    }

    @PatchMapping("/active/{id}")
    public ResponseEntity<Object> patch(@PathVariable UUID id, @RequestBody ActiveCourseDTO activeCourseDTO) {
        var course = this.courseRepository.findById(id).orElseThrow(() -> {
            throw new UserNotFoundException();
        });
        var response = activeCourseDTO.getStatus().toUpperCase();

        if (response.matches("ENABLED")) {
            course.setActive(true);
        } else if (response.matches("DISABLED")){
            course.setActive(false);
        } else {
            return ResponseEntity.badRequest().body("Definição incorreta");
        }
        return ResponseEntity.ok().body(course);

    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object> delete(@PathVariable UUID id) {
        var course = this.courseRepository.findById(id).orElseThrow(() -> {
            throw new UserNotFoundException();
        });
        this.courseRepository.delete(course);
        return ResponseEntity.ok().body("Curso removido: " + course.getName() + "\nCategoria: " + course.getCategory());
    }
}
