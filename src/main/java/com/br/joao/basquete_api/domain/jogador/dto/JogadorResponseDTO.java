package com.br.joao.basquete_api.domain.jogador.dto;

import com.br.joao.basquete_api.domain.jogador.*;
import com.br.joao.basquete_api.domain.jogador.enums.Modulo;

import java.time.LocalDate;
import java.util.UUID;

public record JogadorResponseDTO(
        UUID id,
        String nome,
        Integer idade, // Incluímos a idade calculada
        LocalDate dataNascimento,
        String nomeResponsavel,
        String contatoResponsavel,
        boolean rgEntregue,
        Modulo modulo,
        AtributosFisicos atributosFisicos,
        HabilidadesTecnicas habilidadesTecnicas,
        AtributosMentais atributosMentais
) {
    public JogadorResponseDTO(Jogador jogador) {
        this(
                jogador.getId(),
                jogador.getNome(),
                jogador.getIdade(), // O método da entidade é chamado aqui!
                jogador.getDataNascimento(),
                jogador.getNomeResponsavel(),
                jogador.getContatoResponsavel(),
                jogador.isRgEntregue(),
                jogador.getModulo(),
                jogador.getAtributosFisicos(),
                jogador.getHabilidadesTecnicas(),
                jogador.getAtributosMentais()
        );
    }
}