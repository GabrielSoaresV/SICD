package com.controle.demandas.api.dtoDemandas;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class DemandaHistoricoDTO {
    private Long id;
    private String acao;
    private String oldStatus;
    private String newStatus;
    private String notes;
    private String usuarioNome;
    private LocalDateTime createdAt;
}
