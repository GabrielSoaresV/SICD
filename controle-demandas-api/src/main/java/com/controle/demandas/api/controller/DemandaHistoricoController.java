package com.controle.demandas.api.controller;

import com.controle.demandas.api.dtoDemandas.DemandaHistoricoDTO;
import com.controle.demandas.api.model.DemandaHistorico;
import com.controle.demandas.api.response.ApiResponse;
import com.controle.demandas.api.service.DemandaHistoricoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/historico")
@CrossOrigin(origins = "http://localhost:4200")
public class DemandaHistoricoController {

    private final DemandaHistoricoService historicoService;

    public DemandaHistoricoController(DemandaHistoricoService historicoService) {
        this.historicoService = historicoService;
    }

    // 🔹 Listar histórico de uma demanda
    @GetMapping("/demanda/{demandaId}")
    public ResponseEntity<ApiResponse<List<DemandaHistoricoDTO>>> listarPorDemanda(@PathVariable Long demandaId) {
        List<DemandaHistorico> historicos = historicoService.listarPorDemanda(demandaId);

        List<DemandaHistoricoDTO> dtoList = historicos.stream()
                .map(h -> DemandaHistoricoDTO.builder()
                        .id(h.getId())
                        .acao(h.getAcao())
                        .oldStatus(h.getOldStatus())
                        .newStatus(h.getNewStatus())
                        .notes(h.getNotes())
                        .usuarioNome(h.getUsuario() != null ? h.getUsuario().getNome() : null)
                        .createdAt(h.getCreatedAt())
                        .build())
                .collect(Collectors.toList());

        return ResponseEntity.ok(ApiResponse.success(
                "Histórico da demanda recuperado com sucesso!",
                dtoList
        ));
    }
}
