package com.br.joao.basquete_api.domain.jogador.dto;

import com.br.joao.basquete_api.domain.jogador.DesempenhoTreino;

import java.util.UUID;

public record DesempenhoResponseDTO(
        UUID id,
        UUID jogadorId,
        UUID treinoId,
        Integer pontos,
        Integer assistencias,
        Integer rebotes,
        Integer roubosDeBola,
        Integer tocos,
        Integer errosDePasse,
        Integer arremessosTentados,
        Integer arremessosConvertidos,
        String feedbackDoTreinador
) {
    public DesempenhoResponseDTO(DesempenhoTreino desempenho) {
        this(
                desempenho.getId(),
                desempenho.getJogador().getId(),
                desempenho.getTreino().getId(),
                desempenho.getPontos(),
                desempenho.getAssistencias(),
                desempenho.getRebotes(),
                desempenho.getRoubosDeBola(),
                desempenho.getTocos(),
                desempenho.getErrosDePasse(),
                desempenho.getArremessosTentados(),
                desempenho.getArremessosConvertidos(),
                desempenho.getFeedbackDoTreinador()
        );
    }
}