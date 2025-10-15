package com.controle.demandas.api.repository;

import com.controle.demandas.api.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // Busca um usuário pelo e-mail
    Optional<Usuario> findByEmail(String email);

    // Verifica se já existe um usuário com determinado e-mail
    boolean existsByEmail(String email);
}
