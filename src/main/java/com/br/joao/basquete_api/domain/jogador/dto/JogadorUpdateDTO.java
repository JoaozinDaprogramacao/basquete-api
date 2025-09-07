package com.br.joao.basquete_api.domain.jogador.dto;

import com.br.joao.basquete_api.domain.jogador.AtributosFisicos;
import com.br.joao.basquete_api.domain.jogador.enums.Modulo;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * DTO para atualizar os dados de um jogador existente.
 * Note que dataNascimento não é incluída, pois é considerada imutável.
 * HabilidadesTecnicas e AtributosMentais também são omitidos intencionalmente,
 * pois são calculados pelo sistema com base no histórico de desempenho.
 */
public record JogadorUpdateDTO(
        @NotBlank(message = "O nome do jogador não pode ser vazio.")
        @Size(min = 3, max = 100, message = "O nome do jogador deve ter entre 3 e 100 caracteres.")
        String nome,

        @NotBlank(message = "O nome do responsável não pode ser vazio.")
        @Size(min = 3, max = 100, message = "O nome do responsável deve ter entre 3 e 100 caracteres.")
        String nomeResponsavel,

        @NotBlank(message = "O contato do responsável é obrigatório.")
        @Pattern(regexp = "^\\(\\d{2}\\)\\d{9}$", message = "O formato do contato deve ser (XX)XXXXXXXXX.")
        String contatoResponsavel,

        @NotNull(message = "É obrigatório informar se o RG foi entregue.")
        boolean rgEntregue,

        @NotNull(message = "O módulo do jogador é obrigatório.")
        Modulo modulo,

        @NotNull(message = "Os atributos físicos são obrigatórios.")
        @Valid // Habilita a validação de campos dentro de AtributosFisicos
        AtributosFisicos atributosFisicos
) {
}