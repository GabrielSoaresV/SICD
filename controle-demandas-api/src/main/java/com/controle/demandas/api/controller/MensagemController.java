package com.controle.demandas.api.controller;

import com.controle.demandas.api.model.Mensagem;
import com.controle.demandas.api.response.ApiResponse;
import com.controle.demandas.api.service.MensagemService;
import jakarta.validation.Valid;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/mensagens")
@CrossOrigin(origins = "http://localhost:4200")
public class MensagemController {

    private final MensagemService mensagemService;

    public MensagemController(MensagemService mensagemService) {
        this.mensagemService = mensagemService;
    }

    // 🔹 Listar mensagens de uma demanda
    @GetMapping("/demanda/{demandaId}")
    public ResponseEntity<ApiResponse<List<Mensagem>>> listarMensagens(@PathVariable Long demandaId) {
        List<Mensagem> mensagens = mensagemService.listarMensagens(demandaId);
        return ResponseEntity.ok(ApiResponse.success(
                "Mensagens da demanda recuperadas com sucesso!",
                mensagens
        ));
    }

    // 🔹 Enviar nova mensagem
    @PostMapping
    public ResponseEntity<ApiResponse<Mensagem>> enviarMensagem(@Valid @RequestBody MensagemRequest request) {
        Mensagem msg = mensagemService.enviarMensagem(request.getDemandaId(), request.getUsuarioId(), request.getMensagem());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(
                        "Mensagem enviada com sucesso!",
                        msg
                ));
    }

    // DTO interno para requisição de mensagem
    @Data
    public static class MensagemRequest {
        private Long demandaId;
        private Long usuarioId;
        private String mensagem;
    }
}
