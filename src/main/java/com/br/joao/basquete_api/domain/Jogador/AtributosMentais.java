package com.br.joao.basquete_api.domain.Jogador;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Agrupa os atributos mentais e táticos do jogador.
 * A pontuação pode seguir uma escala, por exemplo, de 0 a 100.
 */
@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AtributosMentais {

    private Integer visaoDeJogo;
    private Integer comunicacao;

    /**
     * Representa a criatividade, ritmo e estilo único do jogador em quadra.
     */
    private Integer flow;
}