package com.controle.demandas.api.service;

import com.controle.demandas.api.model.Mensagem;
import com.controle.demandas.api.model.Usuario;
import com.controle.demandas.api.model.Demanda;
import com.controle.demandas.api.repository.MensagemRepository;
import com.controle.demandas.api.repository.UsuarioRepository;
import com.controle.demandas.api.repository.DemandaRepository;
import com.controle.demandas.api.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MensagemService {

    @Autowired
    private MensagemRepository mensagemRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private DemandaRepository demandaRepository;

    public Mensagem enviarMensagem(Long demandaId, Long usuarioId, String conteudo) {
        Demanda demanda = demandaRepository.findById(demandaId)
                .orElseThrow(() -> new NotFoundException("Demanda não encontrada"));

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado"));

        Mensagem msg = Mensagem.builder()
                .demanda(demanda)
                .usuario(usuario)
                .mensagem(conteudo)
                .build();

        return mensagemRepository.save(msg);
    }

    public List<Mensagem> listarMensagens(Long demandaId) {
        return mensagemRepository.findByDemandaId(demandaId);
    }
}
