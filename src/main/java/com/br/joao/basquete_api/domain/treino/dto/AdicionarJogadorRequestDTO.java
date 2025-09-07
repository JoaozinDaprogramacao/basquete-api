package com.br.joao.basquete_api.domain.treino.dto;

import java.util.UUID;
import jakarta.validation.constraints.NotNull;

public record AdicionarJogadorRequestDTO(
        @NotNull(message = "O ID do jogador é obrigatório.")
        UUID jogadorId
) {}