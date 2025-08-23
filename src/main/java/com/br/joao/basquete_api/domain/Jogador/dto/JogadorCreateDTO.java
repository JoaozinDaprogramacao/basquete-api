package com.br.joao.basquete_api.domain.Jogador.dto;

import com.br.joao.basquete_api.domain.Jogador.Jogador;
import jakarta.validation.constraints.*;

public record JogadorCreateDTO(
        @NotBlank(message = "O nome do jogador não pode ser vazio.")
        @Size(min = 3, max = 100, message = "O nome do jogador deve ter entre 3 e 100 caracteres.")
        String nome,

        @NotNull(message = "O ano de nascimento é obrigatório.")
        @Min(value = 1990, message = "O ano de nascimento deve ser no mínimo 1990.")
        @Max(value = 2024, message = "O ano de nascimento não pode ser no futuro.")
        Integer anoNascimento,

        @NotBlank(message = "O nome do responsável não pode ser vazio.")
        @Size(min = 3, max = 100, message = "O nome do responsável deve ter entre 3 e 100 caracteres.")
        String nomeResponsavel,

        @NotBlank(message = "O contato do responsável é obrigatório.")
        @Pattern(regexp = "^\\(\\d{2}\\)\\d{9}$", message = "O formato do contato deve ser (XX)XXXXXXXXX.")
        String contatoResponsavel,

        @NotNull(message = "É obrigatório informar se o RG foi entregue.")
        Boolean rgEntregue
) {
    // Função de conversão diretamente no record
    public Jogador toJogador(int idade) {
        return new Jogador(
                null, // O ID será gerado pelo banco de dados
                this.nome(),
                this.anoNascimento(),
                idade, // A idade calculada é passada como parâmetro
                this.nomeResponsavel(),
                this.contatoResponsavel(),
                this.rgEntregue()
        );
    }
}