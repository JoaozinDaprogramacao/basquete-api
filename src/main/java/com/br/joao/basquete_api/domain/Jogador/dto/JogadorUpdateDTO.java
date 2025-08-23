package com.br.joao.basquete_api.domain.Jogador.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

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
        Boolean rgEntregue
) {}