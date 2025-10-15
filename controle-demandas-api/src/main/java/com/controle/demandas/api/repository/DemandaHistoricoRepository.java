package com.controle.demandas.api.repository;

import com.controle.demandas.api.model.DemandaHistorico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DemandaHistoricoRepository extends JpaRepository<DemandaHistorico, Long> {

    // Faz o join fetch no usuário para garantir que o nome esteja carregado
    @Query("SELECT h FROM DemandaHistorico h JOIN FETCH h.usuario u WHERE h.demanda.id = :demandaId ORDER BY h.createdAt ASC")
    List<DemandaHistorico> findByDemandaId(@Param("demandaId") Long demandaId);
}
