package com.controle.demandas.api.repository;

import com.controle.demandas.api.model.Mensagem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MensagemRepository extends JpaRepository<Mensagem, Long> {
    List<Mensagem> findByDemandaIdOrderByCreatedAtAsc(Long demandaId);
    List<Mensagem> findByUsuarioId(Long usuarioId);
    List<Mensagem> findByDemandaId(Long demandaId);
}
