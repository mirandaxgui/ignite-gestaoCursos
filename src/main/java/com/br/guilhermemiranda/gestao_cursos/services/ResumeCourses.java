package com.br.guilhermemiranda.gestao_cursos.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.br.guilhermemiranda.gestao_cursos.CourseEntity;
import com.br.guilhermemiranda.gestao_cursos.CourseRepository;

@Service
public class ResumeCourses {

    @Autowired
    private CourseRepository courseRepository;

    public List<CourseEntity> getAllCourses() {

        var courses = courseRepository.findAll();
        return courses;

    }
}
