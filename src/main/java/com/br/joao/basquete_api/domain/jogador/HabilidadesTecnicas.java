package com.br.joao.basquete_api.domain.jogador;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Agrupa as habilidades de fundamento do jogador.
 * A pontuação pode seguir uma escala, por exemplo, de 0 a 100.
 */
@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HabilidadesTecnicas {

    private Integer drible;
    private Integer arremesso; // Arremesso de média distância
    private Integer bandeja;
    private Integer passe;
    @Column(name = "arremesso_3_pontos") // Evita nomes de coluna com números no início
    private Integer arremesso3Pontos;
    private Integer lancesLivre;
}