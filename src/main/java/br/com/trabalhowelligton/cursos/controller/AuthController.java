package br.com.trabalhowelligton.cursos.controller;

import br.com.trabalhowelligton.cursos.dto.LoginRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@Valid @RequestBody LoginRequest request, HttpSession session) {
        if ("admin".equals(request.getUsuario()) && "123".equals(request.getSenha())) {
            session.setAttribute("usuarioLogado", request.getUsuario());
            return ResponseEntity.ok(Map.of("usuario", request.getUsuario()));
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("mensagem", "Usuario ou senha invalidos"));
    }

    @GetMapping("/me")
    public ResponseEntity<Map<String, Object>> me(HttpSession session) {
        Object usuario = session.getAttribute("usuarioLogado");
        if (usuario == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("logado", false));
        }

        return ResponseEntity.ok(Map.of("logado", true, "usuario", usuario));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.noContent().build();
    }
}
