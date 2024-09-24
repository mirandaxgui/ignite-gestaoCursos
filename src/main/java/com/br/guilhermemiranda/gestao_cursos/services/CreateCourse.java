package com.br.guilhermemiranda.gestao_cursos.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.br.guilhermemiranda.gestao_cursos.CourseEntity;
import com.br.guilhermemiranda.gestao_cursos.CourseRepository;

@Service
public class CreateCourse {
  @Autowired
  private CourseRepository courseRepository;

  public void create(CourseEntity courseEntity){
    this.courseRepository.save(courseEntity);
  }
}
