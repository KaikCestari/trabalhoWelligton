package br.com.trabalhowelligton.cursos.repository;

import br.com.trabalhowelligton.cursos.model.Curso;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CursoRepository extends JpaRepository<Curso, Long> {
}
