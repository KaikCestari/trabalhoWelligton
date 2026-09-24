package br.com.trabalhowelligton.cursos.config;

import br.com.trabalhowelligton.cursos.model.Curso;
import br.com.trabalhowelligton.cursos.repository.CursoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataLoader {

    @Bean
    public CommandLineRunner cursosIniciais(CursoRepository cursoRepository) {
        return args -> {
            if (cursoRepository.count() == 0) {
                cursoRepository.save(criarCurso("Java com Spring", "Programacao", 80, "Mariana Alves", true));
                cursoRepository.save(criarCurso("Banco de Dados", "Tecnologia", 60, "Rafael Lima", true));
                cursoRepository.save(criarCurso("HTML, CSS e JavaScript", "Front-end", 50, "Camila Rocha", true));
            }
        };
    }

    private Curso criarCurso(String nome, String categoria, Integer cargaHoraria, String professor, boolean ativo) {
        Curso curso = new Curso();
        curso.setNome(nome);
        curso.setCategoria(categoria);
        curso.setCargaHoraria(cargaHoraria);
        curso.setProfessor(professor);
        curso.setAtivo(ativo);
        return curso;
    }
}
