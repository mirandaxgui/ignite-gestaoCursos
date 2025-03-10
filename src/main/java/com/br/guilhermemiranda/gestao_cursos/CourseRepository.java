package com.br.guilhermemiranda.gestao_cursos;


import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<CourseEntity, UUID>{
  Optional<CourseEntity> findByName(String name);
  Optional<CourseEntity> findByNameOrCategory(String name, String category);
  
}
