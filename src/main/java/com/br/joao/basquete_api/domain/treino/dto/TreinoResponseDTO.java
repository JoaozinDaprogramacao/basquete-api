package com.br.joao.basquete_api.domain.treino.dto;

import com.br.joao.basquete_api.domain.treino.Treino;

import java.time.LocalDate;
import java.util.UUID;

public record TreinoResponseDTO(
        UUID id,
        LocalDate data,
        String focoDoTreino,
        String observacoes,
        Integer jogadoresPresentes
) {
    public TreinoResponseDTO(Treino treino) {
        this(
                treino.getId(),
                treino.getData(),
                treino.getFocoDoTreino(),
                treino.getObservacoes(),
                treino.getJogadoresPresentes()
        );
    }
}