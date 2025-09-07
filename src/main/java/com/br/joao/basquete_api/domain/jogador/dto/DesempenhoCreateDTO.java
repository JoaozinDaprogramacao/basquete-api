package com.br.joao.basquete_api.domain.jogador.dto;

// DTO com as estatísticas de um jogador em um treino
public record DesempenhoCreateDTO(
        Integer pontos,
        Integer assistencias,
        Integer rebotes,
        Integer roubosDeBola,
        Integer tocos,
        Integer errosDePasse,
        Integer arremessosTentados,
        Integer arremessosConvertidos,
        String feedbackDoTreinador
) {}
