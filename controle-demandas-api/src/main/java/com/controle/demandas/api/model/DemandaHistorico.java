package com.controle.demandas.api.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "demanda_historico")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DemandaHistorico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "demanda_id")
    private Demanda demanda;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    private String acao;        // created, updated, assigned, completed, cancelled
    private String oldStatus;
    private String newStatus;
    private String notes;

    private LocalDateTime createdAt;

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
