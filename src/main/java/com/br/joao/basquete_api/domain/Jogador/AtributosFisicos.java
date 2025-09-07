package com.br.joao.basquete_api.domain.jogador;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Embeddable // Anotação chave: indica que esta classe pode ser embutida em outra entidade
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AtributosFisicos {

    /**
     * Altura do jogador em metros. Ex: 1.85
     */
    @Column(name = "altura_m", precision = 3, scale = 2)
    private BigDecimal altura;

    /**
     * Nível de força/resistência física do jogador (escala 0-100).
     */
    @Column(name = "atributo_fisico")
    private Integer fisico;
}