package com.controle.demandas.api.service;

import com.controle.demandas.api.dtoDemandas.*;
import com.controle.demandas.api.exception.DemandaException;
import com.controle.demandas.api.exception.NotFoundException;
import com.controle.demandas.api.model.Cidadao;
import com.controle.demandas.api.model.Demanda;
import com.controle.demandas.api.model.DemandaHistorico;
import com.controle.demandas.api.model.Usuario;
import com.controle.demandas.api.repository.DemandaHistoricoRepository;
import com.controle.demandas.api.repository.DemandaRepository;
import com.controle.demandas.api.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DemandaService {

    @Autowired
    private DemandaRepository demandaRepository;

    @Autowired
    private CidadaoService cidadaoService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private DemandaHistoricoRepository historicoRepository;

    // -------------------- DEMANDAS CRUD --------------------

    public Demanda criarDemanda(DemandaCreateDTO dto) {
        Cidadao cidadao = cidadaoService.buscarPorCpfEntity(dto.getCpfCidadao());

        Demanda demanda = Demanda.builder()
                .titulo(dto.getTitulo())
                .descricao(dto.getDescricao())
                .status("Aberta")
                .cidadao(cidadao)
                .assignedTo(null)
                .build();

        Demanda nova = demandaRepository.save(demanda);
        registrarHistorico(nova, null, "Aberta", "created", null, null);
        return nova;
    }

    public List<DemandaSearchDTO> listarTodasDemandas() {
        return demandaRepository.findAll().stream()
                .map(this::mapToSearchDTO)
                .collect(Collectors.toList());
    }

    public List<DemandaSearchDTO> listarPorCidadao(String cpf) {
        cidadaoService.buscarPorCpfEntity(cpf);
        return demandaRepository.findByCidadaoCpf(cpf).stream()
                .map(this::mapToSearchDTO)
                .collect(Collectors.toList());
    }

    public Demanda buscarPorIdEntity(Long id) {
        return demandaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Demanda não encontrada"));
    }

    public DemandaSearchDTO buscarPorId(Long id) {
        return mapToSearchDTO(buscarPorIdEntity(id));
    }

    public Demanda atualizarDemanda(Long id, DemandaUpdateDTO dto) {
        Demanda demanda = buscarPorIdEntity(id);
        demanda.setTitulo(dto.getTitulo());
        demanda.setDescricao(dto.getDescricao());
        registrarHistorico(demanda, null, null, "updated", null, null);
        return demandaRepository.save(demanda);
    }

    public void excluirDemanda(Long id) {
        Demanda existente = buscarPorIdEntity(id);
        demandaRepository.delete(existente);
    }

    public List<DemandaSearchDTO> filtrarDemanda(DemandaFilterDTO filter) {
        return demandaRepository.findAll().stream()
                .filter(d -> filter.getTitulo() == null || d.getTitulo().toLowerCase().contains(filter.getTitulo().toLowerCase()))
                .filter(d -> filter.getStatus() == null || d.getStatus().equalsIgnoreCase(filter.getStatus()))
                .filter(d -> filter.getCpfCidadao() == null || d.getCidadao().getCpf().equals(filter.getCpfCidadao()))
                .map(this::mapToSearchDTO)
                .collect(Collectors.toList());
    }

    public DemandaSearchDTO mapToSearchDTO(Demanda d) {
        return DemandaSearchDTO.builder()
                .id(d.getId())
                .titulo(d.getTitulo())
                .descricao(d.getDescricao())
                .status(d.getStatus())
                .cpfCidadao(d.getCidadao().getCpf())
                .build();
    }

    // -------------------- HISTÓRICO --------------------

    public List<DemandaHistorico> listarHistorico(Long demandaId) {
        return historicoRepository.findByDemandaId(demandaId);
    }

    private void registrarHistorico(Demanda demanda, String oldStatus, String newStatus,
                                    String acao, String notes, Usuario usuario) {
        DemandaHistorico historico = DemandaHistorico.builder()
                .demanda(demanda)
                .usuario(usuario)
                .acao(acao)
                .oldStatus(oldStatus)
                .newStatus(newStatus)
                .notes(notes)
                .build();
        historicoRepository.save(historico);
    }

    // -------------------- FLUXO DE ATENDIMENTO CENTRALIZADO --------------------

    /**
     * Método centralizado que permite atender, finalizar, devolver ou cancelar uma demanda,
     * atribuindo o usuário responsável e registrando o histórico.
     *
     * @param demandaId ID da demanda
     * @param usuarioId ID do usuário que realizará a ação
     * @param acao      "atender", "finalizar", "devolver" ou "cancelar"
     * @param notes     Observações opcionais para o histórico
     * @return Demanda atualizada
     */
    public Demanda atenderDemanda(Long demandaId, Long usuarioId, String acao, String notes) {
        Demanda demanda = buscarPorIdEntity(demandaId);
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado"));

        String oldStatus = demanda.getStatus();

        switch (acao.toLowerCase()) {
            case "atender":
                if (oldStatus.equals("Aberta") || oldStatus.equals("Não Concluída")) {
                    demanda.setStatus("Em Andamento");
                } else {
                    throw new DemandaException.DemandaForbiddenException(
                            "Só é possível atender uma demanda que está Aberta ou Não Concluída");
                }
                break;

            case "finalizar":
                if (!oldStatus.equals("Em Andamento")) {
                    throw new DemandaException.DemandaForbiddenException(
                            "Só é possível finalizar uma demanda que está Em Andamento");
                }
                demanda.setStatus("Concluída");
                break;

            case "cancelar":
                if (!oldStatus.equals("Aberta")) {
                    throw new DemandaException.DemandaForbiddenException(
                            "Só é possível cancelar uma demanda que está Aberta");
                }
                demanda.setStatus("Cancelada");
                break;

            case "devolver":
                if (!oldStatus.equals("Em Andamento")) {
                    throw new DemandaException.DemandaForbiddenException(
                            "Só é possível devolver uma demanda que está Em Andamento");
                }
                demanda.setStatus("Não Concluída");
                break;

            default:
                throw new DemandaException.DemandaForbiddenException("Ação de status inválida");
        }

        // Atribui o usuário responsável
        demanda.setAssignedTo(usuario);

        // Salva histórico
        registrarHistorico(demanda, oldStatus, demanda.getStatus(), "atendimento", notes, usuario);

        return demandaRepository.save(demanda);
    }
}
