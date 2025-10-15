package com.controle.demandas.api.dtoDemandas;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DemandaAtenderDTO {

    @NotNull(message = "ID do usuário é obrigatório")
    private Long usuarioId;

    @NotBlank(message = "A ação é obrigatória (atender, finalizar, devolver, cancelar)")
    private String acao;

    private String notes; // Observações opcionais
}
