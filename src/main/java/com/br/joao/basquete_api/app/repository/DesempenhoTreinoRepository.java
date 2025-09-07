package com.br.joao.basquete_api.app.repository;

import com.br.joao.basquete_api.domain.jogador.DesempenhoTreino;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface DesempenhoTreinoRepository extends JpaRepository<DesempenhoTreino, UUID> {

    // Método essencial para encontrar o "placar" de um jogador específico em um treino específico
    Optional<DesempenhoTreino> findByTreinoIdAndJogadorId(UUID treinoId, UUID jogadorId);
}