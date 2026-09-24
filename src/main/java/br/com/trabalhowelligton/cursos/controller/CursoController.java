package br.com.trabalhowelligton.cursos.controller;

import br.com.trabalhowelligton.cursos.dto.CursoRequest;
import br.com.trabalhowelligton.cursos.model.Curso;
import br.com.trabalhowelligton.cursos.repository.CursoRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/cursos")
public class CursoController {

    private final CursoRepository cursoRepository;

    public CursoController(CursoRepository cursoRepository) {
        this.cursoRepository = cursoRepository;
    }

    @GetMapping
    public List<Curso> listar() {
        return cursoRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Curso> buscar(@PathVariable Long id) {
        return cursoRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Curso cadastrar(@Valid @RequestBody CursoRequest request) {
        Curso curso = new Curso();
        preencher(curso, request);
        return cursoRepository.save(curso);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Curso> atualizar(@PathVariable Long id, @Valid @RequestBody CursoRequest request) {
        return cursoRepository.findById(id)
                .map(curso -> {
                    preencher(curso, request);
                    return ResponseEntity.ok(cursoRepository.save(curso));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        if (!cursoRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        cursoRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private void preencher(Curso curso, CursoRequest request) {
        curso.setNome(request.getNome());
        curso.setCategoria(request.getCategoria());
        curso.setCargaHoraria(request.getCargaHoraria());
        curso.setProfessor(request.getProfessor());
        curso.setAtivo(request.isAtivo());
    }
}
