package com.br.joao.basquete_api.domain.treino.dto;

import com.br.joao.basquete_api.domain.treino.enums.TipoEvento;
import jakarta.validation.constraints.NotNull;

public record RegistrarEventoDTO(
        @NotNull(message = "O tipo do evento é obrigatório.")
        TipoEvento tipo,

        // O valor é opcional, pois alguns eventos têm valor fixo (ex: CESTA_2_PONTOS vale 2)
        // Mas pode ser usado para eventos variáveis (ex: PONTO_FORCA com valor 5)
        Integer valor
) {}