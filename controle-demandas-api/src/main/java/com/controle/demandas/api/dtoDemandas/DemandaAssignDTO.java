package com.controle.demandas.api.dtoDemandas;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DemandaAssignDTO {
    @NotNull(message = "O ID do usuário é obrigatório")
    private Long usuarioId;
}
