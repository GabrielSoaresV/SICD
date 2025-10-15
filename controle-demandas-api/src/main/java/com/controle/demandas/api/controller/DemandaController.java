package com.controle.demandas.api.controller;

import com.controle.demandas.api.dtoDemandas.*;
import com.controle.demandas.api.model.Demanda;
import com.controle.demandas.api.model.DemandaHistorico;
import com.controle.demandas.api.response.ApiResponse;
import com.controle.demandas.api.service.DemandaHistoricoService;
import com.controle.demandas.api.service.DemandaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/demandas")
@CrossOrigin(origins = "http://localhost:4200")
public class DemandaController {

    private final DemandaService demandaService;
    private final DemandaHistoricoService demandaHistoricoService;

    public DemandaController(DemandaService demandaService,
                             DemandaHistoricoService demandaHistoricoService) {
        this.demandaService = demandaService;
        this.demandaHistoricoService = demandaHistoricoService;
    }

    // -------------------- CRUD DEMANDAS --------------------

    @PostMapping
    public ResponseEntity<ApiResponse<DemandaSearchDTO>> criar(@Valid @RequestBody DemandaCreateDTO dto) {
        Demanda criado = demandaService.criarDemanda(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(
                        "Demanda criada com sucesso!",
                        demandaService.mapToSearchDTO(criado)
                ));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<DemandaSearchDTO>>> listarTodas() {
        List<DemandaSearchDTO> lista = demandaService.listarTodasDemandas();
        return ResponseEntity.ok(ApiResponse.success("Lista de demandas recuperada com sucesso!", lista));
    }

    @GetMapping("/cidadao/{cpf}")
    public ResponseEntity<ApiResponse<List<DemandaSearchDTO>>> listarPorCidadao(@PathVariable String cpf) {
        List<DemandaSearchDTO> lista = demandaService.listarPorCidadao(cpf);
        return ResponseEntity.ok(ApiResponse.success("Demandas do cidadão recuperadas com sucesso!", lista));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<DemandaSearchDTO>> buscarPorId(@PathVariable Long id) {
        DemandaSearchDTO dto = demandaService.buscarPorId(id);
        return ResponseEntity.ok(ApiResponse.success("Demanda encontrada com sucesso!", dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<DemandaSearchDTO>> atualizarDemanda(
            @PathVariable Long id,
            @Valid @RequestBody DemandaUpdateDTO dto) {
        Demanda atualizado = demandaService.atualizarDemanda(id, dto);
        return ResponseEntity.ok(ApiResponse.success(
                "Demanda atualizada com sucesso!",
                demandaService.mapToSearchDTO(atualizado)
        ));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> excluir(@PathVariable Long id) {
        demandaService.excluirDemanda(id);
        return ResponseEntity.ok(ApiResponse.success("Demanda excluída com sucesso!", null));
    }

    @PostMapping("/filtrar")
    public ResponseEntity<ApiResponse<List<DemandaSearchDTO>>> filtrar(@Valid @RequestBody DemandaFilterDTO filter) {
        List<DemandaSearchDTO> lista = demandaService.filtrarDemanda(filter);
        return ResponseEntity.ok(ApiResponse.success("Demandas filtradas com sucesso!", lista));
    }

    // -------------------- HISTÓRICO --------------------

    @GetMapping("/{id}/historico")
    public ResponseEntity<ApiResponse<List<DemandaHistoricoDTO>>> listarHistorico(@PathVariable Long id) {
        List<DemandaHistorico> historicos = demandaHistoricoService.listarPorDemanda(id);
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

        return ResponseEntity.ok(ApiResponse.success("Histórico da demanda recuperado com sucesso!", dtoList));
    }

    // -------------------- ATENDIMENTO CENTRALIZADO --------------------

    /**
     * Endpoint unificado para atender, finalizar, devolver ou cancelar uma demanda,
     * atribuindo automaticamente o usuário responsável e registrando histórico.
     */
    @PatchMapping("/{id}/atender")
    public ResponseEntity<ApiResponse<DemandaSearchDTO>> atenderDemanda(
            @PathVariable Long id,
            @Valid @RequestBody DemandaAtenderDTO dto) { // contém usuarioId, acao e notes
        Demanda atualizado = demandaService.atenderDemanda(id, dto.getUsuarioId(), dto.getAcao(), dto.getNotes());
        return ResponseEntity.ok(ApiResponse.success(
                "Ação de atendimento realizada com sucesso!",
                demandaService.mapToSearchDTO(atualizado)
        ));
    }
}
