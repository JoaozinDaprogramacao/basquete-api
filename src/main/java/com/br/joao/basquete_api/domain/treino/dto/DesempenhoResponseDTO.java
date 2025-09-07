package com.br.joao.basquete_api.domain.treino.dto;

import com.br.joao.basquete_api.domain.jogador.DesempenhoTreino;
import java.util.UUID;

// Este é um "record", uma forma concisa de criar classes de dados em Java.
public record DesempenhoResponseDTO(
        UUID idDesempenho,
        UUID jogadorId,
        UUID treinoId,
        Integer pontos,
        Integer assistencias,
        Integer rebotes,
        Integer arremessosTentados,
        Integer arremessosConvertidos,
        String feedback
) {
    // Construtor auxiliar para facilitar a conversão da entidade para o DTO
    public DesempenhoResponseDTO(DesempenhoTreino desempenho) {
        this(
                desempenho.getId(),
                desempenho.getJogador().getId(),
                desempenho.getTreino().getId(),
                desempenho.getPontos(),
                desempenho.getAssistencias(),
                desempenho.getRebotes(),
                desempenho.getArremessosTentados(),
                desempenho.getArremessosConvertidos(),
                desempenho.getFeedbackDoTreinador()
        );
    }
}