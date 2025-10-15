package com.controle.demandas.api.controller;

import com.controle.demandas.api.model.Usuario;
import com.controle.demandas.api.response.ApiResponse;
import com.controle.demandas.api.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
@CrossOrigin(origins = "http://localhost:4200")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    // 🔹 Criar novo usuário
    @PostMapping
    public ResponseEntity<ApiResponse<Usuario>> criar(@Valid @RequestBody Usuario usuario) {
        Usuario criado = usuarioService.criar(usuario);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(
                        "Usuário criado com sucesso!",
                        criado
                ));
    }

    // 🔹 Listar todos os usuários
    @GetMapping
    public ResponseEntity<ApiResponse<List<Usuario>>> listarTodos() {
        List<Usuario> lista = usuarioService.listarTodos();
        return ResponseEntity.ok(ApiResponse.success(
                "Lista de usuários recuperada com sucesso!",
                lista
        ));
    }

    // 🔹 Buscar usuário por ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Usuario>> buscarPorId(@PathVariable Long id) {
        Usuario usuario = usuarioService.buscarPorId(id);
        return ResponseEntity.ok(ApiResponse.success(
                "Usuário encontrado com sucesso!",
                usuario
        ));
    }

    // 🔹 Atualizar usuário
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Usuario>> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody Usuario dados) {
        Usuario atualizado = usuarioService.atualizar(id, dados);
        return ResponseEntity.ok(ApiResponse.success(
                "Usuário atualizado com sucesso!",
                atualizado
        ));
    }

    // 🔹 Excluir usuário
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> excluir(@PathVariable Long id) {
        usuarioService.excluir(id);
        return ResponseEntity.ok(ApiResponse.success(
                "Usuário excluído com sucesso!",
                null
        ));
    }
}
