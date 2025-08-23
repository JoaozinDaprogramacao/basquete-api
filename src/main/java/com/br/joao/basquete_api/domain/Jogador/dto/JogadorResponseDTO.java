package com.br.joao.basquete_api.domain.Jogador.dto;

import com.br.joao.basquete_api.domain.Jogador.Jogador;

import java.util.UUID;

// DTO para representar um jogador nas respostas da API.
public record JogadorResponseDTO(
        UUID id,
        String nome,
        Integer idade,
        String nomeResponsavel,
        String contatoResponsavel,
        boolean rgEntregue
) {
    // Construtor que converte uma Entidade Jogador para este DTO.
    public JogadorResponseDTO(Jogador jogador) {
        this(
                jogador.getId(),
                jogador.getNome(),
                jogador.getIdade(),
                jogador.getNomeResponsavel(),
                jogador.getContatoResponsavel(),
                jogador.isRgEntregue() // Use isRgEntregue() para boolean
        );
    }
}