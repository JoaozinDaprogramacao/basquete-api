package com.br.joao.basquete_api.domain.treino.dto;

import java.time.LocalDate;

public record TreinoCreateDTO(
        LocalDate data,
        String focoDoTreino,
        String observacoes
) {}