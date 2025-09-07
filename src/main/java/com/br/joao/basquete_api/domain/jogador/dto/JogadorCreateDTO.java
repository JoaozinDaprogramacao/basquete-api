package com.br.joao.basquete_api.domain.jogador.dto;

import com.br.joao.basquete_api.domain.jogador.AtributosFisicos;
import com.br.joao.basquete_api.domain.jogador.AtributosMentais;
import com.br.joao.basquete_api.domain.jogador.HabilidadesTecnicas;
import com.br.joao.basquete_api.domain.jogador.Jogador;
import com.br.joao.basquete_api.domain.jogador.enums.Modulo;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record JogadorCreateDTO(
        @NotBlank(message = "O nome do jogador não pode ser vazio.")
        @Size(min = 3, max = 100, message = "O nome do jogador deve ter entre 3 e 100 caracteres.")
        String nome,

        @NotNull(message = "A data de nascimento é obrigatória.")
        @Past(message = "A data de nascimento deve ser uma data no passado.")
        LocalDate dataNascimento,

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

        @NotNull(message = "Os atributos físicos iniciais são obrigatórios.")
        @Valid // Garante que as validações dentro de AtributosFisicos (se houver) sejam acionadas
        AtributosFisicos atributosFisicos,

        @NotNull(message = "As habilidades técnicas iniciais são obrigatórias.")
        @Valid
        HabilidadesTecnicas habilidadesTecnicas,

        @NotNull(message = "Os atributos mentais iniciais são obrigatórios.")
        @Valid
        AtributosMentais atributosMentais
) {
    /**
     * Converte o DTO para a entidade Jogador.
     * A idade não é mais necessária aqui, pois será calculada dinamicamente pela entidade.
     * @return Uma nova instância da entidade Jogador.
     */
    public Jogador toJogador() {
        Jogador jogador = new Jogador();
        jogador.setNome(this.nome);
        jogador.setDataNascimento(this.dataNascimento);
        jogador.setNomeResponsavel(this.nomeResponsavel);
        jogador.setContatoResponsavel(this.contatoResponsavel);
        jogador.setRgEntregue(this.rgEntregue);
        jogador.setModulo(this.modulo);
        jogador.setAtributosFisicos(this.atributosFisicos);
        jogador.setHabilidadesTecnicas(this.habilidadesTecnicas);
        jogador.setAtributosMentais(this.atributosMentais);
        // O ID e o historicoDesempenho são gerenciados pela persistência.
        return jogador;
    }
}