package com.br.guilhermemiranda.gestao_cursos.exceptions;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException() {
      super("Usuario nao encontrado");
    }
  
}
