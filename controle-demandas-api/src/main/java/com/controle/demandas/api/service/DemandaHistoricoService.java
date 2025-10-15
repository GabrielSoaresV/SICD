package com.controle.demandas.api.service;

import com.controle.demandas.api.model.DemandaHistorico;
import com.controle.demandas.api.repository.DemandaHistoricoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DemandaHistoricoService {

    @Autowired
    private DemandaHistoricoRepository historicoRepository;

    public List<DemandaHistorico> listarPorDemanda(Long demandaId) {
        // Aqui ele retorna a lista completa, com usuário e notes carregados
        return historicoRepository.findByDemandaId(demandaId);
    }
}
